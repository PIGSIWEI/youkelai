package com.ruoyw.pigpigroad.yichengchechebang.Receiver;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.MySyntherizer2;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.NonBlockSyntherizer2;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.InitConfig;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.listener.UiMessageListener;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.AutoCheck;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.OfflineResource;
import com.ruoyw.pigpigroad.yichengchechebang.ZxingUtils.QrCreat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/6/12.
 * Email:920015363@qq.com
 */

public class PollingReceiver extends Service {
    int ticket_code=0;
    private String login_token, imei;
    private int time;
    private Notification notification;
    private NotificationManager nm;
    private Handler handler = new Handler();
    protected Handler mainHandler;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.i(PP_TIP, "我在运行中,TIME:" + System.currentTimeMillis() + ",IMEI:" + imei);
            CheckTicket(imei);
            handler.postDelayed(runnable, time);
        }

        ;
    };

    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    protected String appId = "15319348";

    protected String appKey = "rGYExjNdCoE62AvtiXS8aZGY";

    protected String secretKey = "KuRGopFbrBdZaUcj6ZGH3WwqMtunxeSi";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.MIX;

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    protected String offlineVoice = OfflineResource.VOICE_FEMALE;

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================

    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer2 synthesizer;
    protected static String DESC = "请先看完说明。之后点击“合成并播放”按钮即可正常测试。\n"
            + "测试离线合成功能需要首次联网。\n"
            + "纯在线请修改代码里ttsMode为TtsMode.ONLINE， 没有纯离线。\n"
            + "本Demo的默认参数设置为wifi情况下在线合成, 其它网络（包括4G）使用离线合成。 在线普通女声发音，离线男声发音.\n"
            + "合成可以多次调用，SDK内部有缓存队列，会依次完成。\n\n";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable, time);
        sendMessage();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //拿token
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        time = sp.getInt("time", 5000);
        if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        imei = ((TelephonyManager) MyApplication.getContext().getSystemService(TELEPHONY_SERVICE)).getDeviceId();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }

        };

        initialTts();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        nm.cancel(0);
        synthesizer.release();
        Log.i(PP_TIP, "我被o(ﾟДﾟ)っ！");
    }


    public void CheckTicket(String imei) {
        OkGo.<String>post(RUOYU_URL + "?request=private.ticket.no.jiaoban.order.ticket.get&token=" + login_token + "&platform=android" + "&imei=" + imei)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Toast.makeText(MyApplication.getContext(),"获取数据异常！请检查网络设置或者联系管理员",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str = response.body();
                        Log.i(PP_TIP, Str);
                        try {
                            JSONObject jsonObject = new JSONObject(Str);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                //语音播报操作
                                String id = jsonObject.getString("id");
                               if (ChangeTicket(id) == true){
                                   String voice = jsonObject.getString("voice");
                                   Speek(voice);
                                   //打印小票操作
                                   JSONArray ticket = jsonObject.getJSONArray("ticket");
                                   for (int i = 0; i < ticket.length(); i++) {
                                       JSONObject temp = ticket.getJSONObject(i);
                                       JSONObject style = temp.getJSONObject("style");
                                       printServerText(temp.getString("value"), style.getInt("font_size"), style.getInt("is_bold"), style.getInt("is_underline"));
                                   }
                                   AidlUtil.getInstance().printText("----------------", 48, true, false);
                                   String url=jsonObject.getString("url");
                                   if (url.equals("null")){

                                   }else {
                                       AidlUtil.getInstance().printBitmap(QrCreat.createQRImage(url));
                                       AidlUtil.getInstance().centerPrint("\n"+jsonObject.getString("qrcode_tip"), 32, true, false);
                                       AidlUtil.getInstance().print3Line();
                                   }
                                   AidlUtil.getInstance().print3Line();
                               }else {

                               }
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyApplication.getContext(),"获取数据异常！请检查网络设置或者联系管理员",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * 从服务器获取打印信息
     */
    private void printServerText(String value, int font_size, int isBold, int is_underline) {
        Boolean b_isBolde;
        Boolean b_is_underline;
        if (isBold == 0) {
            b_isBolde = false;
        } else {
            b_isBolde = true;
        }
        if (is_underline == 0) {
            b_is_underline = false;
        } else {
            b_is_underline = true;
        }
        AidlUtil.getInstance().printText(value, font_size, b_isBolde, b_is_underline);
    }


    /**
     * 修改订单状态
     */
    private boolean ChangeTicket(String id) {
        OkGo.<String>post(RUOYU_URL + "?request=private.ticket.order.status.update.action&token=" + login_token + "&platform=android&id=" + id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str = response.body();
                        Log.i(PP_TIP, "设置小票消息：" + Str);
                        try {
                            JSONObject jsonObject = new JSONObject(Str);
                            ticket_code=jsonObject.getInt("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
        if (ticket_code == 0){
            return true;
        }else {
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage() {
        //创建大图标的Bitmap
        Bitmap LargeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_setting_logo);
        //设置图片,通知标题,发送时间,提示方式等属性
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle(getString(R.string.app_name))                        //标题
                .setContentText("正在进行小票轮询")      //内容
                .setTicker("小票轮询已开启")             //收到信息后状态栏显示的文字信息
                .setWhen(System.currentTimeMillis())           //设置通知时间
                .setSmallIcon(R.drawable.icon_setting_logo)
//                .setLargeIcon(LargeBitmap)                     //设置大图标
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
//                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beep))  //设置自定义的提示音
                .setAutoCancel(false);                           //设置点击后取消Notification
        notification = mBuilder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        nm.notify(0, notification);
    }



    ///////////////////////////////百度语音离线处理///////////////////////////////////////
    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);
        Map<String, String> params = getParams();

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        synthesizer = new NonBlockSyntherizer2(this, initConfig, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "7");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }

    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            Log.i(PP_TIP,"【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    public void Speek(String string){
        int result = synthesizer.speak(string);
        checkResult(result,"speek");
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            Log.i(PP_TIP,"error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

}
