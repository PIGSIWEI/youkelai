package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by XYSM on 2018/4/4.
 */

public class SpecialcarQRcodeActivity extends AppCompatActivity {
    private LinearLayout back_ll;
    private ImageView sc_im1,sc_im2;
    private TextView sc_tv1,sc_tv2;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.specialcar_layout2);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        init();
        //获取传递过来的选择
        getSelect();
    }

    /**
     * 获取activity得到选择
     */
    private void getSelect() {
        String getcar=getIntent().getStringExtra("select_car");
        switch (getcar){
            case "1":
                sc_tv1.setText("网约车");
                sc_tv2.setText("网约车");
                sc_im1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag1));
                sc_im2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag1));
                break;
            case "2":
                sc_tv1.setText("出租车");
                sc_tv2.setText("出租车");
                sc_im1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag2));
                sc_im2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag2));
                break;
            case "3":
                sc_tv1.setText("教练车");
                sc_tv2.setText("教练车");
                sc_im1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag3));
                sc_im2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag3));
                break;
            case "4":
                sc_tv1.setText("小贷车");
                sc_tv2.setText("小贷车");
                sc_im1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag4));
                sc_im2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag4));
                break;
            case "5":
                sc_tv1.setText("港澳车");
                sc_tv2.setText("港澳车");
                sc_im1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag5));
                sc_im2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.specialcar_tag5));
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void init() {
        back_ll = findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sc_im1=findViewById(R.id.sc_im1);
        sc_im2=findViewById(R.id.sc_im2);
        sc_tv1=findViewById(R.id.sc_tv1);
        sc_tv2=findViewById(R.id.sc_tv2);
    }

    protected boolean enableSliding() {
        return true;
    }
}
