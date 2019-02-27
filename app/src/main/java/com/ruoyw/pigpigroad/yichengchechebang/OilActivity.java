package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by PIGROAD on 2018/3/10.
 */

public class OilActivity extends AppCompatActivity{

    private LinearLayout back_ll,changeoil_record_iv;
    private Button changeoil_btn;
    private int width;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.oil_layout);
        back_ll=findViewById(R.id.back_ll);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
         width = (wm.getDefaultDisplay().getWidth())/2;
        changeoil_record_iv=findViewById(R.id.changeoil_record_iv);
        changeoil_btn=findViewById(R.id.changeoil_btn);
        changeoil_record_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OilActivity.this,ChangeOilRecordActivity.class);
                startActivity(intent);
            }
        });
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        changeoil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OilActivity.this,ChangeOilActivity.class);
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
