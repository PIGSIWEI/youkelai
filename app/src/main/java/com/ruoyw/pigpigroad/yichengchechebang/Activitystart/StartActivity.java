package com.ruoyw.pigpigroad.yichengchechebang.Activitystart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.ruoyw.pigpigroad.yichengchechebang.InfoActivity;
import com.ruoyw.pigpigroad.yichengchechebang.MainActivity;
import com.ruoyw.pigpigroad.yichengchechebang.Receiver.PollingReceiver;
import com.ruoyw.pigpigroad.yichengchechebang.Util.ToastUtils;
import com.ruoyw.pigpigroad.yichengchechebang.Util.UpdateStatus;
import com.ruoyw.pigpigroad.yichengchechebang.Util.UpdateVersionUtil;
import com.ruoyw.pigpigroad.yichengchechebang.Util.VersionInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by PIGROAD on 2018/3/17.
 */

public class StartActivity extends AppCompatActivity {
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注意：此处将setContentView()方法注释掉
        // setContentView(R.layout.activity_start);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoLogin();
            }
        }, 2000);

        initOkGO();
    }

    /**
     * 初始化OKGO
     */
    private void initOkGO() {

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
        //----------------------------------------------------------------------------------------//
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(getApplication())                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数


    }

    /**
     * 前往注册、登录主页
     */
    private void gotoLogin() {

        SharedPreferences sp = getSharedPreferences("LoginUser", MODE_PRIVATE);
        String login_token=sp.getString("user_token",null);

        if (login_token ==null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            //取消界面跳转时的动画，使启动页的logo图片与注册、登录主页的logo图片完美衔接
            overridePendingTransition(0, 0);
        }else {
            Intent intent = new Intent(StartActivity.this, InfoActivity.class);
            startActivity(intent);
            finish();
            //取消界面跳转时的动画，使启动页的logo图片与注册、登录主页的logo图片完美衔接
            overridePendingTransition(0, 0);
        }

    }

    /**
     * 屏蔽物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            //If token is null, all callbacks and messages will be removed.
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }



}