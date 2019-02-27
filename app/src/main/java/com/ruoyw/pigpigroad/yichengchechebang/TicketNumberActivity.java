package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by PIGROAD on 2018/3/14.
 */

public class TicketNumberActivity extends AppCompatActivity {
    private LinearLayout ticketnumberlist1, ticketnumberlist2, ticketnumberlist3, ticketnumberlist4, ticketnumberlist5;
    private TextView ticketnumbertitle;
    private RadioButton ticketnumber_1, ticketnumber_2, ticketnumber_3, ticketnumber_4, ticketnumber_5;
    private TextView ticketlist_tv1, ticketlist_tv2, ticketlist_tv3, ticketlist_tv4, ticketlist_tv5;
    private LinearLayout back_ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.ticket_number_layout);
//        back_ll=findViewById(R.id.back_ll);
//        if (enableSliding()) {
//            SlidingLayout rootView = new SlidingLayout(this);
//            rootView.bindActivity(this);
//        }
//        back_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        ticketnumberlist1=findViewById(R.id.ticketnumberlist1);
//        ticketnumberlist2=findViewById(R.id.ticketnumberlist2);
//        ticketnumberlist3=findViewById(R.id.ticketnumberlist3);
//        ticketnumberlist4=findViewById(R.id.ticketnumberlist4);
//        ticketnumberlist5=findViewById(R.id.ticketnumberlist5);
//        ticketlist_tv1=findViewById(R.id.ticketlist_tv1);
//        ticketlist_tv2=findViewById(R.id.ticketlist_tv2);
//        ticketlist_tv3=findViewById(R.id.ticketlist_tv3);
//        ticketlist_tv4=findViewById(R.id.ticketlist_tv4);
//        ticketlist_tv5=findViewById(R.id.ticketlist_tv5);
//        ticketnumberlist1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel,R.id.btn_confirm,R.id.ticketnumber_tv1,R.id.ticketnumber_tv2,R.id.ticketnumber_tv3,R.id.ticketnumber_tv4,R.id.ticketnumber_tv5,R.id.ticketnumber_1,R.id.ticketnumber_2,R.id.ticketnumber_3,R.id.ticketnumber_4,R.id.ticketnumber_5)
//                        .setOnBindViewListener(new OnBindViewListener() {
//                            @Override
//                            public void bindView(BindViewHolder viewHolder) {
//                                ticketnumbertitle=viewHolder.getView(R.id.ticketnumbertitle);
//                                ticketnumbertitle.setText("选择一键加油小票联数");
//                            }
//                        })
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_confirm:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.ticketnumber_tv1:
//                                        ticketnumber_1=tDialog.getDialog().findViewById(R.id.ticketnumber_1);
//                                        ticketnumber_1.setChecked(true);
//                                        ticketlist_tv1.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_tv2:
//                                        ticketnumber_2=tDialog.getDialog().findViewById(R.id.ticketnumber_2);
//                                        ticketnumber_2.setChecked(true);
//                                        ticketlist_tv1.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_tv3:
//                                        ticketnumber_3=tDialog.getDialog().findViewById(R.id.ticketnumber_3);
//                                        ticketnumber_3.setChecked(true);
//                                        ticketlist_tv1.setText("顾客联");
//                                        break;
//                                    case R.id.ticketnumber_tv4:
//                                        ticketnumber_4=tDialog.getDialog().findViewById(R.id.ticketnumber_4);
//                                        ticketnumber_4.setChecked(true);
//                                        ticketlist_tv1.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_tv5:
//                                        ticketnumber_5=tDialog.getDialog().findViewById(R.id.ticketnumber_5);
//                                        ticketnumber_5.setChecked(true);
//                                        ticketlist_tv1.setText("提货联");
//                                        break;
//                                    case R.id.ticketnumber_1:
//                                        ticketlist_tv1.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_2:
//                                        ticketlist_tv1.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_3:
//                                        ticketlist_tv1.setText("顾客联");
//
//                                        break;
//                                    case R.id.ticketnumber_4:
//                                        ticketlist_tv1.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_5:
//                                        ticketlist_tv1.setText("提货联");
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.ticket_number_dialog_layout)                        //弹窗布局
//                        .setScreenWidthAspect(TicketNumberActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
//
//            }
//        });
//        ticketnumberlist2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel,R.id.btn_confirm,R.id.ticketnumber_tv1,R.id.ticketnumber_tv2,R.id.ticketnumber_tv3,R.id.ticketnumber_tv4,R.id.ticketnumber_tv5,R.id.ticketnumber_1,R.id.ticketnumber_2,R.id.ticketnumber_3,R.id.ticketnumber_4,R.id.ticketnumber_5)
//                        .setOnBindViewListener(new OnBindViewListener() {
//                            @Override
//                            public void bindView(BindViewHolder viewHolder) {
//                                ticketnumbertitle=viewHolder.getView(R.id.ticketnumbertitle);
//                                ticketnumbertitle.setText("选择团购小票");
//                            }
//                        })
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_confirm:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.ticketnumber_tv1:
//                                        ticketnumber_1=tDialog.getDialog().findViewById(R.id.ticketnumber_1);
//                                        ticketnumber_1.setChecked(true);
//                                        ticketlist_tv2.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_tv2:
//                                        ticketnumber_2=tDialog.getDialog().findViewById(R.id.ticketnumber_2);
//                                        ticketnumber_2.setChecked(true);
//                                        ticketlist_tv2.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_tv3:
//                                        ticketnumber_3=tDialog.getDialog().findViewById(R.id.ticketnumber_3);
//                                        ticketnumber_3.setChecked(true);
//                                        ticketlist_tv2.setText("顾客联");
//                                        break;
//                                    case R.id.ticketnumber_tv4:
//                                        ticketnumber_4=tDialog.getDialog().findViewById(R.id.ticketnumber_4);
//                                        ticketnumber_4.setChecked(true);
//                                        ticketlist_tv2.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_tv5:
//                                        ticketnumber_5=tDialog.getDialog().findViewById(R.id.ticketnumber_5);
//                                        ticketnumber_5.setChecked(true);
//                                        ticketlist_tv2.setText("提货联");
//                                        break;
//                                    case R.id.ticketnumber_1:
//                                        ticketlist_tv2.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_2:
//                                        ticketlist_tv2.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_3:
//                                        ticketlist_tv2.setText("顾客联");
//
//                                        break;
//                                    case R.id.ticketnumber_4:
//                                        ticketlist_tv2.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_5:
//                                        ticketlist_tv2.setText("提货联");
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.ticket_number_dialog_layout)                        //弹窗布局
//                        .setScreenWidthAspect(TicketNumberActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
//
//
//            }
//        });
//        ticketnumberlist3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel, R.id.btn_confirm, R.id.ticketnumber_tv1, R.id.ticketnumber_tv2, R.id.ticketnumber_tv3, R.id.ticketnumber_tv4, R.id.ticketnumber_tv5, R.id.ticketnumber_1, R.id.ticketnumber_2, R.id.ticketnumber_3, R.id.ticketnumber_4, R.id.ticketnumber_5)
//                        .setOnBindViewListener(new OnBindViewListener() {
//                            @Override
//                            public void bindView(BindViewHolder viewHolder) {
//                                ticketnumbertitle = viewHolder.getView(R.id.ticketnumbertitle);
//                                ticketnumbertitle.setText("选择闪付小票");
//                            }
//                        })
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()) {
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_confirm:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.ticketnumber_tv1:
//                                        ticketnumber_1 = tDialog.getDialog().findViewById(R.id.ticketnumber_1);
//                                        ticketnumber_1.setChecked(true);
//                                        ticketlist_tv3.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_tv2:
//                                        ticketnumber_2 = tDialog.getDialog().findViewById(R.id.ticketnumber_2);
//                                        ticketnumber_2.setChecked(true);
//                                        ticketlist_tv3.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_tv3:
//                                        ticketnumber_3 = tDialog.getDialog().findViewById(R.id.ticketnumber_3);
//                                        ticketnumber_3.setChecked(true);
//                                        ticketlist_tv3.setText("顾客联");
//                                        break;
//                                    case R.id.ticketnumber_tv4:
//                                        ticketnumber_4 = tDialog.getDialog().findViewById(R.id.ticketnumber_4);
//                                        ticketnumber_4.setChecked(true);
//                                        ticketlist_tv3.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_tv5:
//                                        ticketnumber_5 = tDialog.getDialog().findViewById(R.id.ticketnumber_5);
//                                        ticketnumber_5.setChecked(true);
//                                        ticketlist_tv3.setText("提货联");
//                                        break;
//                                    case R.id.ticketnumber_1:
//                                        ticketlist_tv3.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_2:
//                                        ticketlist_tv3.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_3:
//                                        ticketlist_tv3.setText("顾客联");
//
//                                        break;
//                                    case R.id.ticketnumber_4:
//                                        ticketlist_tv3.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_5:
//                                        ticketlist_tv3.setText("提货联");
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.ticket_number_dialog_layout)                        //弹窗布局
//                        .setScreenWidthAspect(TicketNumberActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
//            }
//            });
//        ticketnumberlist4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel,R.id.btn_confirm,R.id.ticketnumber_tv1,R.id.ticketnumber_tv2,R.id.ticketnumber_tv3,R.id.ticketnumber_tv4,R.id.ticketnumber_tv5,R.id.ticketnumber_1,R.id.ticketnumber_2,R.id.ticketnumber_3,R.id.ticketnumber_4,R.id.ticketnumber_5)
//                        .setOnBindViewListener(new OnBindViewListener() {
//                            @Override
//                            public void bindView(BindViewHolder viewHolder) {
//                                ticketnumbertitle=viewHolder.getView(R.id.ticketnumbertitle);
//                                ticketnumbertitle.setText("选择小票提示语");
//                            }
//                        })
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_confirm:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.ticketnumber_tv1:
//                                        ticketnumber_1=tDialog.getDialog().findViewById(R.id.ticketnumber_1);
//                                        ticketnumber_1.setChecked(true);
//                                        ticketlist_tv4.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_tv2:
//                                        ticketnumber_2=tDialog.getDialog().findViewById(R.id.ticketnumber_2);
//                                        ticketnumber_2.setChecked(true);
//                                        ticketlist_tv4.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_tv3:
//                                        ticketnumber_3=tDialog.getDialog().findViewById(R.id.ticketnumber_3);
//                                        ticketnumber_3.setChecked(true);
//                                        ticketlist_tv4.setText("顾客联");
//                                        break;
//                                    case R.id.ticketnumber_tv4:
//                                        ticketnumber_4=tDialog.getDialog().findViewById(R.id.ticketnumber_4);
//                                        ticketnumber_4.setChecked(true);
//                                        ticketlist_tv4.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_tv5:
//                                        ticketnumber_5=tDialog.getDialog().findViewById(R.id.ticketnumber_5);
//                                        ticketnumber_5.setChecked(true);
//                                        ticketlist_tv4.setText("提货联");
//                                        break;
//                                    case R.id.ticketnumber_1:
//                                        ticketlist_tv4.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_2:
//                                        ticketlist_tv4.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_3:
//                                        ticketlist_tv4.setText("顾客联");
//
//                                        break;
//                                    case R.id.ticketnumber_4:
//                                        ticketlist_tv4.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_5:
//                                        ticketlist_tv4.setText("提货联");
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.ticket_number_dialog_layout)                        //弹窗布局
//                        .setScreenWidthAspect(TicketNumberActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();
//
//            }
//        });
//        ticketnumberlist5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .addOnClickListener(R.id.btn_cancel,R.id.btn_confirm,R.id.ticketnumber_tv1,R.id.ticketnumber_tv2,R.id.ticketnumber_tv3,R.id.ticketnumber_tv4,R.id.ticketnumber_tv5,R.id.ticketnumber_1,R.id.ticketnumber_2,R.id.ticketnumber_3,R.id.ticketnumber_4,R.id.ticketnumber_5)
//                        .setOnBindViewListener(new OnBindViewListener() {
//                            @Override
//                            public void bindView(BindViewHolder viewHolder) {
//                                ticketnumbertitle=viewHolder.getView(R.id.ticketnumbertitle);
//                                ticketnumbertitle.setText("选择储蓄卡支付小票");
//                            }
//                        })
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.btn_cancel:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.btn_confirm:
//                                        tDialog.dismiss();
//                                        break;
//                                    case R.id.ticketnumber_tv1:
//                                        ticketnumber_1=tDialog.getDialog().findViewById(R.id.ticketnumber_1);
//                                        ticketnumber_1.setChecked(true);
//                                        ticketlist_tv5.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_tv2:
//                                        ticketnumber_2=tDialog.getDialog().findViewById(R.id.ticketnumber_2);
//                                        ticketnumber_2.setChecked(true);
//                                        ticketlist_tv5.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_tv3:
//                                        ticketnumber_3=tDialog.getDialog().findViewById(R.id.ticketnumber_3);
//                                        ticketnumber_3.setChecked(true);
//                                        ticketlist_tv5.setText("顾客联");
//                                        break;
//                                    case R.id.ticketnumber_tv4:
//                                        ticketnumber_4=tDialog.getDialog().findViewById(R.id.ticketnumber_4);
//                                        ticketnumber_4.setChecked(true);
//                                        ticketlist_tv5.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_tv5:
//                                        ticketnumber_5=tDialog.getDialog().findViewById(R.id.ticketnumber_5);
//                                        ticketnumber_5.setChecked(true);
//                                        ticketlist_tv5.setText("提货联");
//                                        break;
//                                    case R.id.ticketnumber_1:
//                                        ticketlist_tv5.setText("两联 (默认)");
//                                        break;
//                                    case R.id.ticketnumber_2:
//                                        ticketlist_tv5.setText("三联");
//                                        break;
//                                    case R.id.ticketnumber_3:
//                                        ticketlist_tv5.setText("顾客联");
//
//                                        break;
//                                    case R.id.ticketnumber_4:
//                                        ticketlist_tv5.setText("商户联");
//                                        break;
//                                    case R.id.ticketnumber_5:
//                                        ticketlist_tv5.setText("提货联");
//                                        break;
//                                }
//                            }
//                        })
//                        .setLayoutRes(R.layout.ticket_number_dialog_layout)                        //弹窗布局
//                        .setScreenWidthAspect(TicketNumberActivity.this, 0.9f)               //屏幕宽度比
//                        .setDimAmount(0f)                                                    //设置焦点
//                        .create()
//                        .show();

//
//            }
//        });
//    }
//    protected boolean enableSliding() {
//        return true;
//    }
    }
}
