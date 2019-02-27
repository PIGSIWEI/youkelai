package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.TimeServer.LocationService;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by PIGROAD on 2018/3/16.
 */

public class OilStatisticsSituationActivity extends AppCompatActivity{
    private LinearLayout back_ll;
    private TextView nowTime;
    private TextView vip_tv, oil_tv;
    private LinearLayout ticket_style_explain;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.oil_statistics_situation_layout);
        initData();
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        onRealTime();
        ticket_style_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_know)
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.btn_know:
//                                        tDialog.dismiss();
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.oil_statistics_dialog)                        //弹窗布局
//                        .setScreenWidthAspect(OilStatisticsSituationActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();

            }
        });
    }


    private void initData() {
        back_ll = findViewById(R.id.back_ll);
        nowTime = findViewById(R.id.nowtime_tv);
        vip_tv = findViewById(R.id.vip_tv);
        oil_tv = findViewById(R.id.oil_tv);
        ticket_style_explain=findViewById(R.id.ticket_style_explain);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }
    protected boolean enableSliding() {
        return true;
    }
    /**
     * 通过网络及定位获取真实的本地时间
     */
    public void onRealTime() {
        final int TIMEOUT = 10;
        final Intent service = new Intent(this, LocationService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                long gmtTime = 0;
                try {
                    URL url = new URL("https://www.baidu.com");
                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);
                    connection.setConnectTimeout(TIMEOUT * 1000);
                    connection.setReadTimeout(TIMEOUT * 1000);
                    String gmtStr = connection.getHeaderField("Date");
                    Log.i("pigpigroad", "get time from network succeed: " + gmtStr);
                    SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.UK);
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));//0时区时间
                    gmtTime = sdf.parse(gmtStr).getTime();
                    Log.i("pigpigroad", "gmt timestamp: " + gmtTime);
                } catch (Exception e) {
                    Log.e("pigpigroad", "get time from network error: " + e);
                    return;
                }
                Location location = null;
                int i = 0;
                while (true) {
                    if (i >= TIMEOUT) {
                        Log.w("pigpigroad", "location timeout");
                        break;
                    }
                    location = LocationService.getLocation();
                    if (location != null) {
                        Log.i("pigpigroad", "location succeed i=" + i);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.e("pigpigroad", "location InterruptedException");
                    }
                    i++;
                }
                int timeZoneInt;
                int deviceZone = Calendar.getInstance(Locale.getDefault())
                        .get(Calendar.ZONE_OFFSET) / 60 / 60 / 1000;//本机设置的时区
                if (location != null) {
                    double longitude = location.getLongitude();//经度
                    timeZoneInt = (int) Math.round(longitude / 15);//基于经度算出的实际时区
                    Log.i("pigpigroad", "provider=" + location.getProvider() + ",longitude=" + longitude
                            + ",location zone=" + timeZoneInt + ",device zone=" + deviceZone);
                } else {
                    Log.w("pigpigroad", "location failed, use device timezone");
                    timeZoneInt = deviceZone;
                }
                long realTime = gmtTime + timeZoneInt * 60 * 60 * 1000;
                long beijingTime = gmtTime + 8 * 60 * 60 * 1000;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
                final String timeReal = sdf.format(realTime);//当地真实时间
                String timeBeijing = sdf.format(beijingTime);//北京时间
                sdf.setTimeZone(TimeZone.getDefault());
                String timeDevice = sdf.format(System.currentTimeMillis());//本机时间
                Log.i("pigpigroad", "timeReal=" + timeReal + ",timeBeijing=" + timeBeijing + ",timeDevice=" + timeDevice);
                stopService(service);
                class MyThread extends Thread {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //延迟两秒更新
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                nowTime.setText("现在时间: " + timeReal);
                            }
                        });
                    }
                }
                new MyThread().start();
            }

        }.start();
    }

}
