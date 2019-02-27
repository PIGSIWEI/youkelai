package com.ruoyw.pigpigroad.yichengchechebang.SUNMI;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ruoyw.pigpigroad.yichengchechebang.SlideBack.MyApplication;

/**
 * Created by PIGROAD on 2018/4/10.
 * Email:920015363@qq.com
 */

public abstract class BaseActivity extends AppCompatActivity {
    MyApplication baseApp;
    private BaseActivity oContext;
    public static Activity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseApp = (MyApplication) getApplication();
        activity=this;
        if (baseApp == null) {
            // 得到Application对象
            baseApp = (MyApplication) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
    }
    // 添加Activity方法
    public void addActivity() {
        baseApp.addActivity_(oContext);// 调用myApplication的添加Activity方法
    }
    //销毁所有Activity方法
    public void removeALLActivity() {
        baseApp.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }
    public static Activity getActivity() {
        return activity;
    }

}
