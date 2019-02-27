package com.ruoyw.pigpigroad.yichengchechebang.fragment;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.BuildConfig;
import com.ruoyw.pigpigroad.yichengchechebang.CenterSettingActivity;
import com.ruoyw.pigpigroad.yichengchechebang.FeedbackActivity;
import com.ruoyw.pigpigroad.yichengchechebang.InfoActivity;
import com.ruoyw.pigpigroad.yichengchechebang.OilSettingActivity;
import com.ruoyw.pigpigroad.yichengchechebang.PasswordActivity;
import com.ruoyw.pigpigroad.yichengchechebang.ProgressBar.HorizontalProgressBar;
import com.ruoyw.pigpigroad.yichengchechebang.R;
import com.ruoyw.pigpigroad.yichengchechebang.Receiver.PollingReceiver;
import com.ruoyw.pigpigroad.yichengchechebang.SQLite.MyDBOpenHelper;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.TicketSettingActivity;
import com.ruoyw.pigpigroad.yichengchechebang.Util.ApkUtils;
import com.ruoyw.pigpigroad.yichengchechebang.Util.SDCardUtils;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.Speek;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.UUID;

import ch.ielse.view.SwitchView;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;
import static com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.MainHandlerConstant.PRINT;

/**
 * Created by PIGROAD on 2018/3/6.
 */

public class Settingpage extends Fragment implements View.OnClickListener {
    private MyDBOpenHelper helper;
    private LinearLayout ticketLayout;
    private LinearLayout ticketSettingLatout,ll_oil_setting,ll_center_setting,ll_check_updata;
    private TextView tv_imei;
    private LinearLayout passwordLayout;
    private LinearLayout voiceLayout;
    private LinearLayout systemLayout;
    private LinearLayout opinionLayout;
    private SwitchView switchView2;
    private Switch switchView;
    private TextView state_tv, switchView2_tv, voice_tv;
    private MyDBOpenHelper myDBOpenHelper;
    private Boolean ticketstates = null;
    private TextView ticketstates_tv, moneystates_tv;
    private LinearLayout exit_btn;
    private RadioButton radio_1, radio_2, radio_3;
    private HorizontalProgressBar progress;
    private DownloadManager mDownloadManager;
    private Long downloadId;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View SettingpageLayout = inflater.inflate(R.layout.new_setting_layout,
                container, false);

        return SettingpageLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InintDate();
        myDBOpenHelper = new MyDBOpenHelper(getContext());
    }


    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:
                Log.i(PP_TIP, String.valueOf(msg));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isServiceWork(getContext(), "com.ruoyw.pigpigroad.yichengchechebang.Receiver.PollingReceiver") == true) {
            Log.i(PP_TIP, "运行中");
            ticketstates_tv.setText("开启");
        } else {
            //没有就 显示弹窗
            Log.i(PP_TIP, "没有运行");
            ticketstates_tv.setText("关闭");
        }
    }

    private void InintDate() {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("time", 5000);
        editor.commit();
        exit_btn = getActivity().findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(this);
        ticketstates_tv = getActivity().findViewById(R.id.ticketstates_tv);
        moneystates_tv = getActivity().findViewById(R.id.moneystates_tv);
        ticketLayout = getActivity().findViewById(R.id.settinglist4);
        ll_oil_setting = getActivity().findViewById(R.id.ll_oil_setting);
        ll_center_setting = getActivity().findViewById(R.id.ll_center_setting);
        ll_check_updata = getActivity().findViewById(R.id.ll_check_updata);
        ticketLayout.setOnClickListener(this);
        ll_center_setting.setOnClickListener(this);
        ll_check_updata.setOnClickListener(this);
        ll_oil_setting.setOnClickListener(this);
        ticketSettingLatout = getActivity().findViewById(R.id.settinglist5);
        ticketSettingLatout.setOnClickListener(this);
        passwordLayout = getActivity().findViewById(R.id.settinglist7);
        passwordLayout.setOnClickListener(this);
        systemLayout = getActivity().findViewById(R.id.settinglist10);
        systemLayout.setOnClickListener(this);
        opinionLayout = getActivity().findViewById(R.id.settinglist11);
        tv_imei = getActivity().findViewById(R.id.tv_imei);
        opinionLayout.setOnClickListener(this);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        tv_imei.setText(((TelephonyManager) MyApplication.getContext().getSystemService(getActivity().TELEPHONY_SERVICE)).getDeviceId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settinglist4:
                if (isServiceWork(getContext(), "com.ruoyw.pigpigroad.yichengchechebang.Receiver.PollingReceiver") == true){
                    Log.i(PP_TIP,"运行中");
                    ticketstates_tv.setText("开启");
                }else {
                    //没有就 显示弹窗
                    Log.i(PP_TIP,"没有运行");
                    ticketstates_tv.setText("关闭");
                }
                android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(getActivity());
                LayoutInflater inflater2 = LayoutInflater.from(getActivity());
                view = inflater2.inflate(R.layout.ticket_layout, null);
                builder2.setCancelable(false);
                builder2.setView(view);
                final Dialog dialog2 = builder2.create();
                dialog2.show();
                Switch switchView = view.findViewById(R.id.switchView);
                Button btn_confirm = view.findViewById(R.id.btn_confirm);
                if (ticketstates_tv.getText().equals("开启")) {
                    switchView.setChecked(true);
                } else {
                    switchView.setChecked(false);
                }
                switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            Intent intent = new Intent(getActivity(), PollingReceiver.class);
                            getActivity().startService(intent);
                            ticketstates_tv.setText("开启");
                        } else {
                            Intent intent = new Intent(getActivity(), PollingReceiver.class);
                            getActivity().stopService(intent);
                            ticketstates_tv.setText("关闭");
                        }
                    }
                });
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });
                break;
            case R.id.settinglist5:
                Intent intent5 =new Intent(getActivity(),TicketSettingActivity.class);
                startActivity(intent5);
                break;

            case R.id.settinglist7:
                Intent intent7=new Intent(getActivity(), PasswordActivity.class);
                startActivity(intent7);
                break;
            case R.id.settinglist10:
                break;
            case R.id.exit_btn:
                android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.exit_dialog, null);
                builder.setView(view);
                Button btn_cancel,btn_exit;
                btn_cancel=view.findViewById(R.id.btn_cancel);
                btn_exit=view.findViewById(R.id.btn_exit);
                final Dialog dialog=builder.create();
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyApplication.ExitClear(getActivity());
                        Toast.makeText(getActivity(), "退出登录成功", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

                break;
            case R.id.settinglist11:
                Intent intent11=new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent11);
                break;
            case R.id.ll_oil_setting:
                startActivity(new Intent(getActivity(),OilSettingActivity.class));
                break;
            case R.id.ll_center_setting:
                startActivity(new Intent(getActivity(),CenterSettingActivity.class));
                break;
            case R.id.ll_check_updata:
                //检测 版本 更新
                showDialog();
                break;
        }
    }

    /**
     * 延迟 操作
     */
    private void showDialog() {
        final ZLoadingDialog dialog = new ZLoadingDialog(getActivity());
        dialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#32cd32"))//颜色
                .setHintText("检测新版本")
                .setCanceledOnTouchOutside(false)
                .setHintTextSize(16f)
                .setCancelable(false)
                .show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                dialog.dismiss();
                checkUpdata();
            }
        }, 2000);
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 检查更新
     */
    private void checkUpdata() {
        OkGo.<String>post(RUOYU_URL+"?request=public.auth.version_check_action&platform=android&id=1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            JSONArray data=jsonObject.getJSONArray("data");
                            JSONObject temp=data.getJSONObject(0);
                            int versionCode=temp.getInt("versionCode");
                            int clientVersionCode = ApkUtils.getVersionCode(getActivity());
                            if (versionCode>clientVersionCode){
                                showUpdata(
                                        "最新版本："+temp.getString("versionName"),
                                        "版本大小："+temp.getString("versionSize"),
                                        temp.getString("versionDesc"),
                                        temp.getString("downloadUrl")
                                );
                            }else {
                                Toast.makeText(getActivity(), "当前已经是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showUpdata(String versionName, String versionSize, String versionDesc, final String url){
        final Dialog dialog = new android.app.AlertDialog.Builder(getActivity()).create();
        final File file = new File(SDCardUtils.getRootDirectory()+"/updateVersion/YKL.apk");
        dialog.show();
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.version_update_dialog, null);
        dialog.setContentView(view);
        final Button btnOk = (Button) view.findViewById(R.id.btn_update_id_ok);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_update_content);
        TextView tvUpdateTile = (TextView) view.findViewById(R.id.tv_update_title);
        final TextView tvUpdateMsgSize = (TextView) view.findViewById(R.id.tv_update_msg_size);

        tvContent.setText(versionName);
        tvUpdateTile.setText(versionSize);
        tvUpdateMsgSize.setText(versionDesc);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.btn_update_id_ok){
                    showDownloadDialog(url);
                }
            }
        });

    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog(String url) {
        Toast.makeText(getActivity(), "正在下载···", Toast.LENGTH_SHORT).show();
        String downPath = url;//下载路径 根据服务器返回的apk存放路径
        //使用系统下载类
        mDownloadManager = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedOverRoaming(false);
        File apkFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "油客来.apk");
        if (apkFile.isFile()){
            apkFile.delete();
        }
        //创建目录下载
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "油客来.apk");
        // 把id保存好，在接收者里面要用
        downloadId = mDownloadManager.enqueue(request);
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //机型适配
        request.setMimeType("application/vnd.android.package-archive");
        //通知栏显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("下载");
        request.setDescription("正在下载中...");
        request.setVisibleInDownloadsUi(true);
        getActivity().registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        cursor.close();
    }

    /**
     * 7.0兼容
     */
    private void installAPK() {
        File apkFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "油客来.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //7.0以上需要
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID+".InfoActivity", apkFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

}