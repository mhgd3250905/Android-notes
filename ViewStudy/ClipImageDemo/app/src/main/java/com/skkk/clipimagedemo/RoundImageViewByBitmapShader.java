package com.skkk.clipimagedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class RoundImageViewByBitmapShader extends ImageView {
    private Shader mShader;
    private Paint mPaint;

    public RoundImageViewByBitmapShader(Context context) {
        super(context);
    }

    public RoundImageViewByBitmapShader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageViewByBitmapShader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoundImageViewByBitmapShader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapDrawable drawable= (BitmapDrawable) getDrawable();
        if (mShader==null){
            mShader=new BitmapShader(drawable.getBitmap(),
                    Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP);
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        }
        mPaint.setShader(mShader);
        canvas.drawRoundRect(0,0,getMeasuredWidth(),getMeasuredHeight(),100,100,mPaint);
    }
}
