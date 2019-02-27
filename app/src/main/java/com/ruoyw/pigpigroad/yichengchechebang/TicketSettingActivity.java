package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;
import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by PIGROAD on 2018/3/13.
 */

public class TicketSettingActivity extends AppCompatActivity{
    private LinearLayout back_ll;
    private LinearLayout ticketsetting3,ll_2;
    private RadioButton ticketpause_1,ticketpause_2,ticketpause_3,ticketpause_4;
    private TextView ticket_pause_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_setting_layout);
        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ticket_pause_tv=findViewById(R.id.ticket_pause_tv);
        ll_2=findViewById(R.id.ll_2);
        ticketsetting3=findViewById(R.id.ticketsettinglist3);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("LoginUser", MODE_PRIVATE);
        int time = sp.getInt("time", 0);
        time=time/1000;
        ticket_pause_tv.setText(time+"秒");
        ticketsetting3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(TicketSettingActivity.this)
                        .setTitle("选择打印停顿秒数")
                        .setItems(new String[]{"3秒","5秒", "10秒", "15秒"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences mSharedPreferences =getSharedPreferences("LoginUser", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mSharedPreferences.edit();
                                switch (i){
                                    case 0:
                                        ticket_pause_tv.setText("3秒");
                                        editor.putInt("time",3000);
                                        break;
                                    case 1:
                                        ticket_pause_tv.setText("5秒");
                                        editor.putInt("time",5000);
                                        break;
                                    case 2:
                                        ticket_pause_tv.setText("10秒");
                                        editor.putInt("time",10000);
                                        break;
                                    case 3:
                                        ticket_pause_tv.setText("15秒");
                                        editor.putInt("time", 15000);
                                        break;
                                }
                                editor.commit();
                            }
                        })
                        .show();
            }
        });
        ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TicketSettingActivity.this,TestPrintActivity.class));
            }
        });
    }
    protected boolean enableSliding() {
        return true;
    }
}
