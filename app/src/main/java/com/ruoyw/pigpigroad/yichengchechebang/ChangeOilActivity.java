package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.DateWidget.CustomDatePicker;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by PIGROAD on 2018/3/10.
 */

public class ChangeOilActivity extends AppCompatActivity{
    private LinearLayout back_ll;
    private RelativeLayout selectTime;
    private TextView currentTime,tv_hint;
    private CustomDatePicker customDatePicker;
    private Button btn_cancle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.changeoil_layout);
        tv_hint=findViewById(R.id.tv_hint);
        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        selectTime = (RelativeLayout) findViewById(R.id.selectTime);
        currentTime = (TextView) findViewById(R.id.currentTime);
        btn_cancle=findViewById(R.id.btn_cancel);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDatePicker.show(currentTime.getText().toString());
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initDatePicker();
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }
    protected boolean enableSliding() {
        return true;
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
       currentTime.setText(now);
      customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
           public void handle(String time) { // 回调接口，获得选中的时间
              currentTime.setText(time);
              currentTime.setVisibility(View.VISIBLE);
              tv_hint.setVisibility(View.INVISIBLE);
          }
       }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(false); // 允许循环滚动
    }

}
