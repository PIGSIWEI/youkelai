package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

public class HotfixActivity extends AppCompatActivity {
    private LinearLayout ll_back;
    private TextView tv_test;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotfit_layout);
        this.init();

    }

    private void init() {
        tv_test=findViewById(R.id.tv_test);
        tv_test.setText("当前HOT FIX V0.1");
        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
        ll_back=findViewById(R.id.back_ll);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
