package com.lyz.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by lyz on 2018/9/7.
 *
 */

public class TestViewGroup extends ViewGroup {

    private Context mContext;
    private int mScreenHeight;

    public TestViewGroup(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public TestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public TestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (null != windowManager) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        mScreenHeight = displayMetrics.heightPixels;

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int count = getChildCount();
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//        }
//    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        caculateWidthAndPadding(MeasureSpec.getSize(widthMeasureSpec));
//        for (int index = 0; index < getChildCount(); index++) {
//
//            child.measure(MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childSize, MeasureSpec.AT_MOST));
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int count = getChildCount();
//        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
//        layoutParams.height = mScreenHeight * count;
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//            if (child.getVisibility() == View.VISIBLE) {
//                child.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
//            }
//        }

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.layout(l, t, r, b);
        }
    }
}
