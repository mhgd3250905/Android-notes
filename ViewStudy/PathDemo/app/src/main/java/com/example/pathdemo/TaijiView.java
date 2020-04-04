package com.example.pathdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TaijiView extends View {
    Paint paint;
    Path path1;
    Path path2;

    public TaijiView(Context context) {
        super(context);
    }

    public TaijiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaijiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public TaijiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        paint = new Paint();
        path1 = new Path();
        path2 = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.save();
        path1.addCircle(0, 0, 200, Path.Direction.CW);
        path2.addRect(-200, -200, 0, 200, Path.Direction.CW);
        path1.op(path2, Path.Op.INTERSECT);//去相交的区域
        path2.reset();
        path2.addCircle(0, -100, 100, Path.Direction.CCW);
        path1.op(path2, Path.Op.UNION);//去全部的区域
        path2.reset();
        path2.addCircle(0, 100, 100, Path.Direction.CW);
        path1.op(path2, Path.Op.DIFFERENCE);//取Path1减去path2的区域
        canvas.drawPath(path1, paint);
        canvas.restore();

        paint.setColor(Color.WHITE);
        canvas.drawCircle(0, -100, 25, paint);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(0, 100, 25, paint);
    }
}
