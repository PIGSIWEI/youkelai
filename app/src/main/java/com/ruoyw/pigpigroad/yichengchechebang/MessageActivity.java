package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by PIGROAD on 2018/3/10.
 */

public class MessageActivity extends AppCompatActivity{

    private LinearLayout back_ll;
    private LinearLayout messagelist1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.message_layout);
        back_ll=findViewById(R.id.back_ll);
        messagelist1=findViewById(R.id.messagelist1);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        messagelist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageActivity.this, GiftActivity.class);
                startActivity(intent);
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
