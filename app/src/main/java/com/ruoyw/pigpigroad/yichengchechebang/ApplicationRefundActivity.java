package com.ruoyw.pigpigroad.yichengchechebang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import java.lang.reflect.Field;

/**
 * Created by PIGROAD on 2018/3/16.
 */

public class  ApplicationRefundActivity extends AppCompatActivity {
private SearchView mSearchView;
private LinearLayout apprefundlist1,refund_record_btn;
private LinearLayout back_ll;
@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.application_refund_layout);
        if (enableSliding()) {
                SlidingLayout rootView = new SlidingLayout(this);
                rootView.bindActivity(this);
        }
        mSearchView=findViewById(R.id.search);
        InitSearch();//自定义搜索框
        back_ll=findViewById(R.id.back_ll);
        back_ll.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        finish();
        }
        });
        refund_record_btn=findViewById(R.id.refund_record_btn);
        refund_record_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent intent=new Intent(ApplicationRefundActivity.this,RefundRecordActivity.class);
                        startActivity(intent);
                }
        });
        apprefundlist1=findViewById(R.id.apprefundlist1);
        apprefundlist1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                        new TDialog.Builder(getSupportFragmentManager())
//                                .addOnClickListener(R.id.backorder_back)
//                                .setOnViewClickListener(new OnViewClickListener() {
//                                        @Override
//                                        public void onViewClick(BindViewHolder viewHolder, View view, TDialog tDialog) {
//                                                switch (view.getId()){
//                                                        case R.id.backorder_back:
//                                                                tDialog.dismiss();
//                                                                break;
//                                                }
//                                        }
//                                })
//                                .setLayoutRes(R.layout.app_refund_info)                        //弹窗布局
//                                .setScreenWidthAspect(ApplicationRefundActivity.this, 1.0f)               //屏幕宽度比
//                                .setGravity(Gravity.BOTTOM)                                    //设置屏幕底部
//                                .setDimAmount(0f)                                                    //设置焦点
//                                .create()
//                                .show();
//                        AlertDialog.Builder builder=new AlertDialog.Builder(ApplicationRefundActivity.this);
//                        LayoutInflater inflater = LayoutInflater.from(ApplicationRefundActivity.this);
//                        builder.setCancelable(false);
//                        View view1=inflater.inflate(R.layout.app_refund_info,null);

                }
        });
        }
        private void InitSearch() {
                if (mSearchView == null) {
                        return;
                } else {
                        int imgId = mSearchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
                        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
                        TextView textView = (TextView) mSearchView.findViewById(id);
                        ImageView searchButton = (ImageView) mSearchView.findViewById(imgId);
                        searchButton.setImageResource(R.drawable.setting_search_bg);
                        mSearchView.setIconifiedByDefault(false);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//14sp
                        //设置提示字体颜色
                        textView.setHintTextColor(this.getResources().getColor(R.color.search_hint_color));
                        //设置字体颜色
                        textView.setTextColor(this.getResources().getColor(R.color.titlecolor));
                        try {        //--拿到字节码
                                Class<?> argClass = mSearchView.getClass();
                                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                                Field ownField = argClass.getDeclaredField("mSearchPlate");
                                //--暴力反射,只有暴力反射才能拿到私有属性
                                ownField.setAccessible(true);
                                View mView = (View) ownField.get(mSearchView);
                                //--设置背景
                                mView.setBackgroundColor(Color.TRANSPARENT);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }
        protected boolean enableSliding() {
                return true;
        }
        }

