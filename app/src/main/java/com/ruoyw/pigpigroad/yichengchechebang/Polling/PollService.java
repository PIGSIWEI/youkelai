package com.ruoyw.pigpigroad.yichengchechebang.Polling;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.util.AidlUtil;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.MySyntherizer2;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.NonBlockSyntherizer2;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.Speek;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.InitConfig;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.listener.UiMessageListener;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.AutoCheck;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.OfflineResource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/5/15.
 * Email:920015363@qq.com
 */

public class PollService extends Service {
    private Speek speek=new Speek(MyApplication.getActivity());
    private int notificationId =0;
    private Boolean isStart = true;
    private String login_token;

    @Override
    public IBinder onBind(Intent intent) {
        new MyThread().start();
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("oncreate()");
        MyThread thread = new MyThread();
        thread.start();
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);

        super.onCreate();
    }


    private class MyThread extends Thread {
        @Override
        public void run() {
            while (isStart) {
                try {
                    // 每个5秒向服务器发送一次请求
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // do something
                //实例化通知管理器
//                Notification notification = new Notification.Builder(MyApplication.getContext())
//                        .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
//                        .setContentTitle("易成车车帮")
//                        .setContentText("你有新的订单")
//                        .build();
//                //获取Notification管理器
//                NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
//                //然后用这个Notification管理器把Notification弹出去，那个0是id，用来标识这个Notification的。
//                notificationManager.notify(notificationId, notification);
//                notificationId =notificationId+1;


                /**
                 * 查询是否有订单，有的话就打印
                 */

                CheckTicket();
            }
        }
    }

    @Override
    public void onDestroy() {
        isStart = false;
        super.onDestroy();
    }

    private void CheckTicket(){
        OkGo.<String>post(RUOYU_URL+"?request=private.ticket.no.jiaoban.order.ticket.get&token="+login_token+"&platform=android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str=response.body();
                        Log.i(PP_TIP,Str);
                        try {
                            JSONObject jsonObject=new JSONObject(Str);
                            int code =jsonObject.getInt("code");
                            String id =jsonObject.getString("id");
                            if (code == 0){
                                //语音播报操作
                                String voice=jsonObject.getString("voice");
                                speek.Speeking(voice);
                            }else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 打印支付信息
     */
    private void printTicket(String title,String who_discount,String time,String store_name,String auth_code, String oil_name,String price,String lit
                             ,String gun_id,String total,String pay_money,String id
    ){
        String titles="  "+title+"\n";
        AidlUtil.getInstance().printText(titles, 24, false, false);
        String ticket="  "+who_discount+"\n"
                +"  "+time+"\n"
                +"  "+auth_code+"\n"
                +"  "+store_name+"\n"
                +"  "+oil_name+"\n"
                +"  "+price+"\n"
                +"  "+lit+"\n";
        AidlUtil.getInstance().printText(ticket, 24, false, false);
        String info=" "+gun_id+"\n"
                +" "+total+"\n"
                +" "+pay_money+"\n\n\n";
        AidlUtil.getInstance().printText(info, 48, true, false);
        ChangeTicket(id);
    }

    /**
     * 修改订单状态
     */
    private void ChangeTicket(String id){
        OkGo.<String>post(RUOYU_URL+"?request=private.ticket.order.status.update.action&token="+login_token+"&platform=android&id="+id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String Str=response.body();
                        Log.i(PP_TIP,"设置小票消息："+Str);
                        try {
                            JSONObject jsonObject=new JSONObject(Str);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }




}