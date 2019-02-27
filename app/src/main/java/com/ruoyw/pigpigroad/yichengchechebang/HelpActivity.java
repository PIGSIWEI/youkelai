package com.ruoyw.pigpigroad.yichengchechebang;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.SlidingLayout;

import java.lang.reflect.Field;

/**
 * Created by PIGROAD on 2018/3/9.
 */

public class HelpActivity  extends AppCompatActivity{
    private LinearLayout back_ll;
    private SearchView mSearchView;
    private LinearLayout helplist1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.help_layout);
        back_ll=findViewById(R.id.back_ll);
        mSearchView=findViewById(R.id.search);
        helplist1=findViewById(R.id.helplist1);
        back_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        helplist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new TDialog.Builder(getSupportFragmentManager())
//                        .setLayoutRes(R.layout.help_message1)
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
//                        .setScreenWidthAspect(HelpActivity.this, 0.9f)
//                        .setScreenHeightAspect(HelpActivity.this,0.8f)
//                        .setDimAmount(0f)
//                        .create()
//                        .show();
            }
        });
        InitSearch();//自定义搜索框
        if (enableSliding()) {
            SlidingLayout rootView = new SlidingLayout(this);
            rootView.bindActivity(this);
        }
    }
    protected boolean enableSliding() {
        return true;
    }
    /**
     * 自定义搜索框样式
     */
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


}
