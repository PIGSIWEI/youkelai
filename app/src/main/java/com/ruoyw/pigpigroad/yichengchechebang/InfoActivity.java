package com.ruoyw.pigpigroad.yichengchechebang;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.ruoyw.pigpigroad.yichengchechebang.Activitystart.StartActivity;
import com.ruoyw.pigpigroad.yichengchechebang.Hotfit.SophixStubApplication;
import com.ruoyw.pigpigroad.yichengchechebang.Receiver.PollingReceiver;
import com.ruoyw.pigpigroad.yichengchechebang.SUNMI.BaseActivity;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.Util.ApkUtils;
import com.ruoyw.pigpigroad.yichengchechebang.Util.SDCardUtils;
import com.ruoyw.pigpigroad.yichengchechebang.Util.ToastUtils;
import com.ruoyw.pigpigroad.yichengchechebang.Util.UpdateStatus;
import com.ruoyw.pigpigroad.yichengchechebang.Util.UpdateVersionUtil;
import com.ruoyw.pigpigroad.yichengchechebang.Util.VersionInfo;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.MySyntherizer2;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.NonBlockSyntherizer2;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.control.InitConfig;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.listener.UiMessageListener;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.AutoCheck;
import com.ruoyw.pigpigroad.yichengchechebang.VoiceUtil.util.OfflineResource;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.Homepage;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.Menupage;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.Orderpage;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.Settingpage;
import com.taobao.sophix.SophixManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.ielse.view.SwitchView;

import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.PP_TIP;
import static com.ruoyw.pigpigroad.yichengchechebang.RuoyuAPI.MyAppApiConfig.RUOYU_URL;

/**
 * Created by PIGROAD on 2018/3/6.
 */

public class InfoActivity extends BaseActivity implements View.OnClickListener {
    private Fragment menupage_fragment;
    private Fragment homepage_fragment;
    private Fragment orderpage_fragment;
    private Fragment settingpage_fargment;
    private List<View> bottomTabs;
    private View homepage, settingpage, orderpage;
    private ImageView home_iv, order_iv, setting_iv;
    private TextView home_tv, order_tv, setting_tv, tv_username, tv_store_id;
    private FragmentManager fragmentManager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String login_token;
    private View HeadView;
    private DownloadManager mDownloadManager;
    private Long downloadId;
    private String store_name = "";
    private CloudPushService pushService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getSupportActionBar().hide();
        //在setContentView之前添加,未添加的话home键监听无效，设置窗体属性
        this.getWindow().setFlags(0x80000000, 0x80000000);
        setContentView(R.layout.info_acitivity);
        pushService = PushServiceFactory.getCloudPushService();
        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        login_token = sp.getString("user_token", null);
        if (login_token == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        initViews();
        fragmentManager = getSupportFragmentManager();
        setSelectTab(0);
        Log.i(PP_TIP, "Start polling service...");
        GetUser();
        if (isServiceWork(InfoActivity.this, "com.ruoyw.pigpigroad.yichengchechebang.Receiver.PollingReceiver") == true) {
            Log.i(PP_TIP, "运行中");
        } else {
            //没有就 显示弹窗
            checkDialog();
            Log.i(PP_TIP, "没有运行");
        }
        //权限申请

        Grant();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        pushService.addAlias(((TelephonyManager) MyApplication.getContext().getSystemService(getActivity().TELEPHONY_SERVICE)).getDeviceId(), new CommonCallback() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailed(String s, String s1) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    /**
     * 返回键的调用
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = LayoutInflater.from(InfoActivity.this);
            View view = inflater.inflate(R.layout.exit_dialog, null);
            builder.setView(view);
            final Dialog dialog=builder.create();
            TextView textView=view.findViewById(R.id.exit_text);
            textView.setText("确定退出吗？");
            Button btn_cancel,btn_exit;
            btn_cancel=view.findViewById(R.id.btn_cancel);
            btn_exit=view.findViewById(R.id.btn_exit);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(InfoActivity.this, PollingReceiver.class);
        stopService(intent);
        Log.i(PP_TIP, "Stop polling service...");
        if (SophixStubApplication.isRelaunch) {
            Log.i(PP_TIP, "如果是冷启动，则杀死App进程，从而加载补丁:" );
            SophixStubApplication.isRelaunch = false;
            SophixManager.getInstance().killProcessSafely();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 软件返回的操作
         * 1.检测 更新版本 如果 有新版本 强制更新
         * 2.检测 小票轮循 是否在 线程中 工作
         */
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        initPermissions();
        home_iv = (ImageView) findViewById(R.id.home_iv);
        order_iv = (ImageView) findViewById(R.id.order_iv);
        setting_iv = (ImageView) findViewById(R.id.setting_iv);
        home_tv = (TextView) findViewById(R.id.home_tv);
        order_tv = (TextView) findViewById(R.id.order_tv);
        setting_tv = (TextView) findViewById(R.id.setting_tv);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        homepage = findViewById(R.id.homepage);
        orderpage = findViewById(R.id.orderpage);
        settingpage = findViewById(R.id.settingpage);
        homepage.setOnClickListener(this);
        orderpage.setOnClickListener(this);
        settingpage.setOnClickListener(this);
        bottomTabs = new ArrayList<>(3);
        bottomTabs.add(homepage);
        bottomTabs.add(orderpage);
        bottomTabs.add(settingpage);
        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);
        HeadView = mNavigationView.inflateHeaderView(R.layout.nav_header_main);
        tv_username = HeadView.findViewById(R.id.tv_username);
        tv_store_id = HeadView.findViewById(R.id.tv_store_id);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.printer_test:
                        Intent intent = new Intent(InfoActivity.this, PrinterTestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_home1:
                        Intent intent1 = new Intent(InfoActivity.this, ClassActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_home2:
//                        Intent intent2 = new Intent(InfoActivity.this, StatisticsActivity.class);
//                        startActivity(intent2);
                        Intent intent2 = new Intent(getActivity(), AllOrderActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_home3:
                        Intent intent3 = new Intent(InfoActivity.this, GroupBuyingActivity.class);
                        intent3.putExtra("chose_type", "1");
                        startActivity(intent3);
                        break;
                    case R.id.nav_home4:
                        Intent intent4 = new Intent(InfoActivity.this, OilPriceActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_home5:
                        Intent intent5 = new Intent(InfoActivity.this, PasswordActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.nav_home6:
                        AlertDialog.Builder builder=new AlertDialog.Builder(InfoActivity.this);
                        builder.setCancelable(false);
                        LayoutInflater inflater = LayoutInflater.from(InfoActivity.this);
                        View view = inflater.inflate(R.layout.exit_dialog, null);
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
                                MyApplication.ExitClear(InfoActivity.this);
                                Toast.makeText(InfoActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                        break;
                }
                return false;
            }
        });

    }

    /**
     * Fragment选择方法
     */
    public void setSelectTab(int index) {
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        switch (index) {
            case 0:
                home_iv.setImageResource(R.drawable.home_select_bg);
                home_tv.setTextColor(Color.parseColor("#6dc77f"));

                if (menupage_fragment == null) {
                    menupage_fragment = new Menupage();
                    transaction.add(R.id.content, menupage_fragment);

                } else {
                    transaction.show(menupage_fragment);
                }
                break;
            case 1:
                order_iv.setImageResource(R.drawable.order_select_bg);
                order_tv.setTextColor(Color.parseColor("#6dc77f"));
                if (orderpage_fragment == null) {
                    orderpage_fragment = new Orderpage();
                    transaction.add(R.id.content, orderpage_fragment);
                } else {
                    transaction.show(orderpage_fragment);
                }
                break;
            case 2:
                setting_iv.setImageResource(R.drawable.setting_select_bg);
                setting_tv.setTextColor(Color.parseColor("#6dc77f"));
                if (settingpage_fargment == null) {
                    settingpage_fargment = new Settingpage();
                    transaction.add(R.id.content, settingpage_fargment);
                } else {
                    transaction.show(settingpage_fargment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 开启fragment事物
     */
    private void clearSelection() {
        home_iv.setImageResource(R.drawable.home_bg);
        home_tv.setTextColor(Color.parseColor("#000000"));
        order_iv.setImageResource(R.drawable.order_bg);
        order_tv.setTextColor(Color.parseColor("#000000"));
        setting_iv.setImageResource(R.drawable.setting_bg);
        setting_tv.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (menupage_fragment == null && fragment instanceof Menupage)
            menupage_fragment = fragment;
        if (orderpage_fragment == null && fragment instanceof Orderpage)
            orderpage_fragment = fragment;
        if (settingpage_fargment == null && fragment instanceof Settingpage)
            settingpage_fargment = fragment;
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (menupage_fragment != null) {
            transaction.hide(menupage_fragment);
        }
        if (orderpage_fragment != null) {
            transaction.hide(orderpage_fragment);
        }
        if (settingpage_fargment != null) {
            transaction.hide(settingpage_fargment);
        }

    }

    /**
     * 底部栏切换
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homepage:
                setSelectTab(0);
                break;
            case R.id.orderpage:
                setSelectTab(1);
                break;
            case R.id.settingpage:
                setSelectTab(2);
                break;
        }
    }

    /**
     * 获取门店管理员信息
     */
    private void GetUser() {
        OkGo.<String>post(RUOYU_URL + "?request=private.admin.my.admin.info.get&token=" + login_token + "&platform=android")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseStr = response.body();
                        Log.i(PP_TIP, responseStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            int code = jsonObject.getInt("code");
                            if (code == 0) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                tv_username.setText(data.getString("admin_name"));
                                tv_store_id.setText(data.getString("store_id") + "号门店");
                                SharedPreferences mSharedPreferences = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                editor.putString("store_id", data.getString("store_id"));
                                //  editor.putString("store_name",data.getString("name"));
                                editor.commit();
                            } else if (code == 999) {
                                Toast.makeText(InfoActivity.this, "你的身份已过期，请重新登录", Toast.LENGTH_SHORT).show();
                                SharedPreferences sp = activity.getSharedPreferences("LoginUser", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(activity, MainActivity.class);
                                activity.startActivity(intent);
//                                Intent intentz=new Intent("android.intent.action.PollingReceiver");
//                                intentz.putExtra("PPP","stop");
//                                getActivity().sendBroadcast(intentz);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 判断某个服务是否正在运行的方法
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
     * 小票轮训 弹窗
     */
    private void showTicket() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.ticket_layout, null);
        builder.setCancelable(false);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();
        Switch switchView = view.findViewById(R.id.switchView);
        Button btn_confirm = view.findViewById(R.id.btn_confirm);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Intent intent = new Intent(InfoActivity.this, PollingReceiver.class);
                    startService(intent);
                    //Toast.makeText(InfoActivity.this, "小票轮训已打开", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(InfoActivity.this, PollingReceiver.class);
                    stopService(intent);
                    //Toast.makeText(InfoActivity.this, "小票轮训已关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 检测 轮训 是否 打开 弹窗
     */
    private void checkDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("检测到小票轮循没有打开，是否打开小票轮循？");
        builder1.setTitle("提示");
        builder1.setCancelable(false);
        builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                showTicket();
            }
        });
        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder1.create().show();
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
                           int clientVersionCode = ApkUtils.getVersionCode(InfoActivity.this);
                           if (versionCode>clientVersionCode){
                               showUpdata(
                                       "最新版本："+temp.getString("versionName"),
                                       "版本大小："+temp.getString("versionSize"),
                                       temp.getString("versionDesc"),
                                       temp.getString("downloadUrl")
                               );
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               });
    }

    /**
     * 权限申请
     */
    private void Grant() {

        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permission = ActivityCompat.checkSelfPermission(InfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    InfoActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (ContextCompat.checkSelfPermission(InfoActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) InfoActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
        checkUpdata();

    }

    private void showUpdata(String versionName, String versionSize, String versionDesc, final String url){
        final Dialog dialog = new android.app.AlertDialog.Builder(this).create();
        final File file = new File(SDCardUtils.getRootDirectory()+"/updateVersion/YKL.apk");
        dialog.setCancelable(false);// 可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(false);//
        dialog.show();
        final View view = LayoutInflater.from(this).inflate(R.layout.version_update_dialog, null);
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
            Toast.makeText(this, "正在下载···", Toast.LENGTH_SHORT).show();
            String downPath = url;//下载路径 根据服务器返回的apk存放路径
            //使用系统下载类
            mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
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
            registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

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
                    Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
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
            Uri uri = FileProvider.getUriForFile(getApplication(), BuildConfig.APPLICATION_ID+".InfoActivity", apkFile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    /**
     * 配置Android 6.0 以上额外的权限
     */
    private void initPermissions() {
        //配置微信登录和6.0权限
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,//读取储存权限
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入储存权限
            };
            if (checkPermissionAllGranted(mPermissionList)) {
                /*查询是否有新补丁需要载入*/
                SophixManager.getInstance().queryAndLoadNewPatch();
            } else {

                ActivityCompat.requestPermissions(this, mPermissionList, 0);
            }
        } else {
            /*查询是否有新补丁需要载入*/
            SophixManager.getInstance().queryAndLoadNewPatch();
        }

    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(PP_TIP, "成功获得权限");
                    /*查询是否有新补丁需要载入*/
                    SophixManager.getInstance().queryAndLoadNewPatch();
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("未获得权限，无法获得补丁升级功能")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + getPackageName()));
                                    startActivity(intent);
                                }
                            }).setNegativeButton("取消", null).show();
                }
            default:
                break;
        }
    }
}

