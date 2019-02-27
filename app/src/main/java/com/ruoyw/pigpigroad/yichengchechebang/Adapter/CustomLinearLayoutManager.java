package com.ruoyw.pigpigroad.yichengchechebang.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by PIGROAD on 2018/10/9
 * Email:920015363@qq.com
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}

