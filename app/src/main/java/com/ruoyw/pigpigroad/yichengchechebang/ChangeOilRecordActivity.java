package com.ruoyw.pigpigroad.yichengchechebang;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

/**
 * Created by PIGROAD on 2018/3/12.
 */

public class ChangeOilRecordActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout changeoilrecordlist1;
    private LinearLayout back_ll;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.changeoil_record_layout);

        changeoilrecordlist1=findViewById(R.id.changeoilrecordlist1);
        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
        changeoilrecordlist1.setOnClickListener(this);
    }
    protected boolean enableSliding() {
        return true;
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.changeoilrecordlist1:
//                new TDialog.Builder(getSupportFragmentManager())
//                        .setLayoutRes(R.layout.changeoilrecord_list_layout)
//                        .addOnClickListener(R.id.back)
//                        .setOnViewClickListener(new OnViewClickListener() {
//                            @Override
//                            public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                switch (view.getId()){
//                                    case R.id.back:
//                                        tDialog.dismiss();
//                                        break;
//                                }
//                            }
//                        })
//                        .setScreenWidthAspect(ChangeOilRecordActivity.this, 1.0f)       //屏幕宽度比
//                        .setGravity(Gravity.BOTTOM)                                  //设置屏幕底部
//                        .setDimAmount(0f)                                              //设置焦点
//                        .create()
//                        .show();
                break;
        }
    }

}
