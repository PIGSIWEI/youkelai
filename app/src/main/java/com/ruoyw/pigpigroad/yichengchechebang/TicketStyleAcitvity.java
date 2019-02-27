package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by PIGROAD on 2018/3/14.
 */

public class TicketStyleAcitvity extends AppCompatActivity{
    private LinearLayout back_ll;
    private LinearLayout ticket_style_default;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.ticket_style_layout);
        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ticket_style_default=findViewById(R.id.ticket_style_default);
        ticket_style_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_confirm,R.id.btn_cancel)
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.btn_confirm:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.ticket_style_default_layout)                        //弹窗布局
//                        .setScreenWidthAspect(TicketStyleAcitvity.this, 0.9f)               //屏幕宽度比
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
