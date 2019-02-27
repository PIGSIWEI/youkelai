package com.ruoyw.pigpigroad.yichengchechebang;

/**
 * Created by PIGROAD on 2018/3/15.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

public class ExchangeAddActivity extends AppCompatActivity {

    private LinearLayout back_ll;
    private Button btn_confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.exchange_add_layout);
        back_ll = findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel, R.id.btn_confirm2)
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()) {
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_confirm2:
//                                        new TDialog.Builder(getSupportFragmentManager())
//                                                .addOnClickListener(R.id.btn_cancel2, R.id.btn_confirm3)
//                                                .setOnViewClickListener(new OnViewClickListener() {
//                                                    @Override
//                                                    public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                                        switch (view.getId()) {
//                                                            case R.id.btn_cancel2:
//                                                                tDialog.dismiss();
//                                                                break;
//                                                            case R.id.btn_confirm3:
//                                                                tDialog.dismiss();
//                                                                break;
//                                                        }
//                                                    }
//                                                })
//                                                .setLayoutRes(R.layout.success_dialog)                        //弹窗布局
//                                                .setScreenWidthAspect(ExchangeAddActivity.this, 0.9f)               //屏幕宽度比
//                                                .setDimAmount(0f)                                                    //设置焦点
//                                                .create()
//                                                .show();
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.confirm_dialog)                        //弹窗布局
//                        .setScreenWidthAspect(ExchangeAddActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
            }
        });
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }
    protected boolean enableSliding() {
        return true;
    }


}

