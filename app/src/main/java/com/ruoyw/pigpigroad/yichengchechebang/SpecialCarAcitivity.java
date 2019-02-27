package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by XYSM on 2018/4/4.
 */

public class SpecialCarAcitivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton sc_rb1, sc_rb2, sc_rb3, sc_rb4, sc_rb5;
    private LinearLayout sc_ll1, sc_ll2, sc_ll3, sc_ll4, sc_ll5;
    private LinearLayout back_ll;
    private Button sc_btn;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.specialcar_layout);
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
        sc_ll1 = findViewById(R.id.sc_ll1);
        sc_ll2 = findViewById(R.id.sc_ll2);
        sc_ll3 = findViewById(R.id.sc_ll3);
        sc_ll4 = findViewById(R.id.sc_ll4);
        sc_ll5 = findViewById(R.id.sc_ll5);
        sc_rb1 = findViewById(R.id.sc_rb1);
        sc_rb2 = findViewById(R.id.sc_rb2);
        sc_rb3 = findViewById(R.id.sc_rb3);
        sc_rb4 = findViewById(R.id.sc_rb4);
        sc_rb5 = findViewById(R.id.sc_rb5);
        sc_ll1.setOnClickListener(this);
        sc_ll2.setOnClickListener(this);
        sc_ll3.setOnClickListener(this);
        sc_ll4.setOnClickListener(this);
        sc_ll5.setOnClickListener(this);
        sc_btn=findViewById(R.id.sc_btn);
        sc_btn.setOnClickListener(this);
        intent=new Intent(this,SpecialcarQRcodeActivity.class);
    }

    protected boolean enableSliding() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sc_ll1:
                sc_rb1.setChecked(true);
                sc_rb2.setChecked(false);
                sc_rb3.setChecked(false);
                sc_rb4.setChecked(false);
                sc_rb5.setChecked(false);
                break;
            case R.id.sc_ll2:
                sc_rb1.setChecked(false);
                sc_rb2.setChecked(true);
                sc_rb3.setChecked(false);
                sc_rb4.setChecked(false);
                sc_rb5.setChecked(false);
                break;
            case R.id.sc_ll3:
                sc_rb1.setChecked(false);
                sc_rb2.setChecked(false);
                sc_rb3.setChecked(true);
                sc_rb4.setChecked(false);
                sc_rb5.setChecked(false);
                break;
            case R.id.sc_ll4:
                sc_rb1.setChecked(false);
                sc_rb2.setChecked(false);
                sc_rb3.setChecked(false);
                sc_rb4.setChecked(true);
                sc_rb5.setChecked(false);
                break;
            case R.id.sc_ll5:
                sc_rb1.setChecked(false);
                sc_rb2.setChecked(false);
                sc_rb3.setChecked(false);
                sc_rb4.setChecked(false);
                sc_rb5.setChecked(true);
                break;
            case R.id.sc_btn:
                if (sc_rb1.isChecked()) {
                    intent.putExtra("select_car","1");
                    startActivity(intent);
                    this.finish();
                } else if (sc_rb2.isChecked()) {
                    intent.putExtra("select_car","2");
                    startActivity(intent);
                    this.finish();
                } else if (sc_rb3.isChecked()) {
                    intent.putExtra("select_car","3");
                    startActivity(intent);
                    this.finish();
                } else if (sc_rb4.isChecked()) {
                    intent.putExtra("select_car","4");
                    startActivity(intent);
                    this.finish();
                } else if (sc_rb5.isChecked()) {
                    intent.putExtra("select_car","5");
                    startActivity(intent);
                    this.finish();
                } else {
                    Toast.makeText(SpecialCarAcitivity.this, "请选择要认证的车辆类型！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
