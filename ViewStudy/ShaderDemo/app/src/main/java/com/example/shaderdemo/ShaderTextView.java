package com.example.shaderdemo;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class ShaderTextView extends View {
    private static final String TAG = "ShaderTextView";

    private Paint paint;
    int offset = 0;
    LinearGradient linearGradient;
    String text = "演示文字";

    int[] colors = {Color.parseColor("#4C4D48"), Color.parseColor("#D9D6DA"), Color.parseColor("#4C4D4B")};
    float[] posittions = {0.2f, 0.5f, 0.8f};

    public ShaderTextView(Context context) {
        super(context);
    }

    public ShaderTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化画笔以及动画
     */
    private void init(Context context) {
        paint = new Paint();
        paint.setTextSize(170);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(-1000, 1000);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                linearGradient = new LinearGradient(offset, 300, 1000 + offset, 600,
                        colors, posittions, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = getMySize(widthMeasureSpec, getTextWidth(text));
        final int height = getMySize(heightMeasureSpec, getTextHeight(text));
        setMeasuredDimension(width, height);
    }

    /**
     * 获取测量大小
     *
     * @param measureSpec
     * @return
     */
    private int getMySize(int measureSpec, int widthDefault) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(widthDefault, specSize);
        } else {
            result = widthDefault;
        }
        return result;
    }

    //获取文字宽度
    private int getTextWidth(String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width() + getPaddingLeft() + getPaddingRight();
    }

    //获取文字高度
    private int getTextHeight(String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height() + getPaddingTop() + getPaddingBottom();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#232423"));
        paint.setAlpha(255);

        //计算基线
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int dy=(fontMetricsInt.descent-fontMetricsInt.ascent)/2-fontMetricsInt.descent;
        int baseLine=getHeight()/2+dy;
        Log.d(TAG, "baseLine: "+baseLine);
        int x=getPaddingLeft();
        //x: 开始的位置 y: 基线
        canvas.drawText(text, x, baseLine, paint);
    }
}
