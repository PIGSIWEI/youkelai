package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by XYSM on 2018/4/4.
 */

public class MarketingActivity  extends AppCompatActivity {
    private LinearLayout back_ll;
    private LinearLayout mka_ll1;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.marketing_layout);
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        init();
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

    }

    protected boolean enableSliding() {
        return true;
    }
}
