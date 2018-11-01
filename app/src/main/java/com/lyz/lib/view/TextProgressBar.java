package com.lyz.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by lyz on 2018/9/7.
 *
 */

public class TextProgressBar extends ProgressBar {

    private final String TAG = "TextProgressBar";
    String mText;
    Paint mPaint;

    public TextProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.mText, 0, this.mText.length(), rect);

        int x = (getWidth() / 2) - rect.centerX();
        Log.d(TAG, "x = " + x + " ; centerX = " + rect.centerX());
        int y = (getHeight() / 2) - rect.centerY();
        Log.d(TAG, "y = " + y + " ; centerY = " + rect.centerY());

        canvas.drawText(this.mText, x, y, this.mPaint);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(0xff000000);
        mPaint.setTextSize(20);
    }

    public void setText(int progress) {
        this.mText = String.valueOf(progress) + "%";
    }

    @Override
    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }
}



