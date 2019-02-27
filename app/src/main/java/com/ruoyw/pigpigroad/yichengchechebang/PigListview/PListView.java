package com.ruoyw.pigpigroad.yichengchechebang.PigListview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * Created by XYSM on 2018/3/30.
 */

public class PListView extends ListView {

    // 这个值控制可以把ListView拉出偏离顶部或底部的距离。
    private static final int MAX_OVERSCROLL_Y =50;

    private Context mContext;
    private int newMaxOverScrollY;

    public PListView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public PListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }


    private void init() {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float density = metrics.density;
        newMaxOverScrollY = (int) (density * MAX_OVERSCROLL_Y);
    }

    // 最关键的地方。
    //支持到SDK8需要增加@SuppressLint("NewApi")。
    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX, newMaxOverScrollY,
                isTouchEvent);
    }

}
