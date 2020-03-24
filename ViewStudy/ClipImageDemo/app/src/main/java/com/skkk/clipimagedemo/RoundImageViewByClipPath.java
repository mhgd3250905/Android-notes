package com.skkk.clipimagedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


/**
 * 使用ClipPath方法来实现圆角图片
 * @author shengk
 */
public class RoundImageViewByClipPath extends ImageView {
    private Path mPath;
    private PaintFlagsDrawFilter mPaintFlagDrawFilter;
    private RectF mRectF;
    private float[] mRadius=new float[]{100,100,100,100,100,100,100,100};


    public RoundImageViewByClipPath(Context context) {
        super(context);
    }

    public RoundImageViewByClipPath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundImageViewByClipPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundImageViewByClipPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPath=new Path();
        mPaintFlagDrawFilter=new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRectF==null){
            mRectF=new RectF();
            mRectF.set(0,0,getMeasuredWidth(),getMeasuredHeight());
        }
        mPath.addRoundRect(mRectF,mRadius,Path.Direction.CW);
        canvas.setDrawFilter(mPaintFlagDrawFilter);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }
}
