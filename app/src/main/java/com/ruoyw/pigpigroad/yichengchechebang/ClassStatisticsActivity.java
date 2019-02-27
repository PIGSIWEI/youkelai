package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;
import com.ruoyw.pigpigroad.yichengchechebang.TimeServer.LocationService;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.OilStatisticspage;
import com.ruoyw.pigpigroad.yichengchechebang.fragment.VipStatisticspage;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by PIGROAD on 2018/3/16.
 */

public class ClassStatisticsActivity extends AppCompatActivity{
    private LinearLayout back_ll,oilgun_btn,oil_ll,vip_ll;
    private TextView nowTime;
    private ViewPager viewpager;
    private ClassStatisticsActivity.FragAdapter1 adapter;
    private List<Fragment> list;
    private TextView vip_tv, oil_tv;
    private ImageView line_iv;
    private int currentIndex;
    private int screenWidth;
    private Button class_dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.class_statistics_layout);
        initData();
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        onRealTime();
        list = new ArrayList<Fragment>();
        list.add(new OilStatisticspage());
        list.add(new VipStatisticspage());
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line_iv
                .getLayoutParams();
        lp.width = screenWidth / 2;
        adapter = new FragAdapter1(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line_iv
                        .getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));

                } else if (currentIndex == 1 && position == 0) // 1->0
                {
                    lp.leftMargin = (int) (-(1 - offset)
                            * (screenWidth * 1.0 / 2) + currentIndex
                            * (screenWidth / 2));

                }
                line_iv.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        class_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel)
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.class_dialog)                        //弹窗布局
//                        .setScreenWidthAspect(ClassStatisticsActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();

            }
        });
        oilgun_btn=findViewById(R.id.oilgun_btn);
        oilgun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ClassStatisticsActivity.this,OilStatisticsSituationActivity.class);
                startActivity(intent);
            }
        });
        oil_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(0);
            }
        });
        vip_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(1);
            }
        });
    }


    private void initData() {
        back_ll = findViewById(R.id.back_ll);
        nowTime = findViewById(R.id.nowtime_tv);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        vip_tv = findViewById(R.id.vip_tv);
        oil_tv = findViewById(R.id.oil_tv);
        line_iv = findViewById(R.id.line_iv);
        class_dialog=findViewById(R.id.class_dialog);
        oil_ll=findViewById(R.id.oil_ll);
        vip_ll=findViewById(R.id.vip_ll);
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

    class FragAdapter1 extends FragmentPagerAdapter {

        private List<Fragment> list;

        public FragAdapter1(FragmentManager fm) {
            super(fm);
        }

        public FragAdapter1(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int postion) {
            return list.get(postion);
        }

        @Override
        public int getCount() {
            return list.size();
        }

    }
}
