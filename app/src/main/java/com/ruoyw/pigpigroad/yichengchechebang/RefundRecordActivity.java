package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.ruoyw.pigpigroad.yichengchechebang.DateWidget.CustomDatePicker;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by PIGROAD on 2018/3/15.
 */

public class RefundRecordActivity extends AppCompatActivity {
    private LinearLayout back_ll,select_btn;
    private TextView start_time, end_time, start_time_hint, end_time_hint;
    private CustomDatePicker customDatePicker, customDatePicker2;
    private Date dt1, dt2;
    private SimpleDateFormat df;
    private CustomPopWindow PayPopWindow;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.refund_record_layout);
        init();
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }



    }

    private void init() {
        back_ll = findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        select_btn=findViewById(R.id.select_btn);
        df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(RefundRecordActivity.this).inflate(R.layout.refund_record_dialog, null);
                //给时间选择页面绑定窗口
                timeLayout(contentView);
                //弹出时间选择页面
                PayPopWindow = new CustomPopWindow.PopupWindowBuilder(RefundRecordActivity.this)
                        .setView(contentView)
                        .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        //支付页面消失时候回调
                        .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {

                            }
                        })
                        .create()
                        .showAsDropDown(select_btn);
            }
        });
    }

    private void timeLayout(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_cancel:
                        PayPopWindow.dissmiss();
                        break;
                    case R.id.select_start_time:
                        initDatePicker();
                        customDatePicker.show(start_time.getText().toString());
                        break;
                    case R.id.select_end_time:
                        initDatePicker2();
                        customDatePicker2.show(end_time.getText().toString());
                        break;
                    case R.id.btn_time_select:
                        String starttime = start_time.getText().toString();
                        String endtime = end_time.getText().toString();
                        try {
                            dt1 = df.parse(starttime);
                            dt2 = df.parse(endtime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (starttime.equals("") || endtime.equals("")) {
                            Toast.makeText(view.getContext(), "请正确选择时间！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dt1.getTime() > dt2.getTime()) {
                                Toast.makeText(view.getContext(), "结束时间不能大于开始时间！请正确选择时间！", Toast.LENGTH_SHORT).show();
                            } else {
                                //确定时间后开始允许
                                Toast.makeText(view.getContext(), "succes！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            }
        };
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(listener);
        contentView.findViewById(R.id.select_end_time).setOnClickListener(listener);
        contentView.findViewById(R.id.select_start_time).setOnClickListener(listener);
        contentView.findViewById(R.id.btn_time_select).setOnClickListener(listener);
        start_time = contentView.findViewById(R.id.start_time);
        end_time = contentView.findViewById(R.id.end_time);
        start_time_hint = contentView.findViewById(R.id.start_time_hint);
        end_time_hint = contentView.findViewById(R.id.end_time_hint);
    }
    protected boolean enableSliding() {
        return true;
    }
    /**
     * 时间选择器的初始化
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        start_time.setText(now);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                start_time.setText(time);
                start_time.setVisibility(View.VISIBLE);
                start_time_hint.setVisibility(View.INVISIBLE);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 显示时和分
        customDatePicker.setIsLoop(false); // 允许循环滚动
    }

    private void initDatePicker2() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        end_time.setText(now);
        customDatePicker2 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                end_time.setText(time);
                end_time.setVisibility(View.VISIBLE);
                end_time_hint.setVisibility(View.INVISIBLE);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker2.showSpecificTime(false); // 显示时和分
        customDatePicker2.setIsLoop(false); // 允许循环滚动
    }
}

