package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

public class CardIcActivity extends AppCompatActivity {

    private LinearLayout ll_card_recharge,ll_card_consume,ll_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_ic_layout);
        this.init();
    }

    /**
     * 初始化
     */
    private void init() {
        ll_back=findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_card_recharge=findViewById(R.id.ll_card_recharge);
        ll_card_consume=findViewById(R.id.ll_card_consume);
        ll_card_consume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardIcActivity.this,CardConsumeActivity.class));
            }
        });
        ll_card_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CardIcActivity.this,CardReChargeActivity.class));
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
