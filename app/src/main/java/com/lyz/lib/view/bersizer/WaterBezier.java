package com.lyz.lib.view.bersizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lyz on 2018/9/28.
 *
 */
public class WaterBezier extends View {

    private Paint paint = new Paint();

    private Path path = new Path();

    private int width;
    private int height;

    private int waveLength;
    private int waveHeight;
    private int waveWidth;

    private int dx;

    private int originY;
    private Region region;

    private Bitmap mBitmap;


    public WaterBezier(Context context) {
        super(context);
    }

    public WaterBezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterBezier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (originY == 0) {
            originY = height;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPathData();

        canvas.drawPath(path, paint);

        Rect bounds = region.getBounds();

        canvas.drawBitmap(mBitmap, bounds.right - mBitmap.getWidth(), bounds.top - mBitmap.getHeight(), paint);
    }

    public void setPathData() {
        path.reset();

        int halfWaveLength = waveLength / 2;

        path.moveTo( -waveLength + dx, 0);

        for (int i = -waveLength; i < width; i += waveLength) {
            path.rQuadTo(halfWaveLength / 2, -waveHeight , halfWaveLength, 0);
            path.rQuadTo(halfWaveLength / 2, waveHeight , halfWaveLength, 0);
        }

        region = new Region();

        Region region = new Region(width / 2 - -1, 0, width / 2, height);
    }
}
