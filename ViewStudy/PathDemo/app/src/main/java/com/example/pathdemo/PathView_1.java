package com.example.pathdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class PathView_1 extends View {
    private static final String TAG = "PathView_1";

    private Paint paint;
    private Path path;
    private float start;
    private PathMeasure pathMeasure;

    public PathView_1(Context context) {
        super(context);
    }

    public PathView_1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView_1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PathView_1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);

        paint.setStrokeWidth(30.0f);
        path = new Path();
        path.moveTo(100, 100);
        path.lineTo(600, 100);
        path.lineTo(600, 600);
        path.lineTo(100, 600);
        path.lineTo(100,100);

        pathMeasure = new PathMeasure(path, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path dst1 = new Path();
        pathMeasure.getSegment(0, start, dst1, true);
        paint.setColor(Color.YELLOW);
        canvas.drawPath(dst1, paint);

        paint.setColor(Color.BLACK);
        Path dst2 = new Path();
        if (start > pathMeasure.getLength() - 100) {
            pathMeasure.getSegment(start, pathMeasure.getLength(), dst2, true);
            pathMeasure.getSegment(0, 100 - pathMeasure.getLength() + start, dst2, true);
        } else {
            pathMeasure.getSegment(start, start + 100, dst2, true);
            paint.setColor(Color.BLACK);
        }
        canvas.drawPath(dst2, paint);


        Path dst3 = new Path();
        pathMeasure.getSegment(start + 100, pathMeasure.getLength(), dst3, true);
        paint.setColor(Color.YELLOW);
        canvas.drawPath(dst3, paint);

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: start = " + start);
                start += 10;
                if (start >= pathMeasure.getLength()) {
                    start = start - pathMeasure.getLength();
                }
                invalidate();
            }
        }, 10);
    }
}
