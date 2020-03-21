package com.skkk.clipimagedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class RoundImageViewByXfermode extends ImageView {
    private Paint mPaint;
    private Xfermode mXfermode=new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private Bitmap mBitmap;
    private RectF mRectF;

    public RoundImageViewByXfermode(Context context) {
        super(context);
    }

    public RoundImageViewByXfermode(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundImageViewByXfermode(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RoundImageViewByXfermode(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(0,0,getMeasuredWidth(),getMeasuredHeight(),
                null,Canvas.ALL_SAVE_FLAG);
        Drawable drawable=getDrawable();
        if (mBitmap==null){
             mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
             Canvas drawCanvas=new Canvas(mBitmap);
             drawable.draw(drawCanvas);
        }

        if (mRectF==null){
            mRectF=new RectF(0,0,getMeasuredWidth(),getMeasuredHeight());
        }

        mPaint.setXfermode(null);
        canvas.drawRoundRect(mRectF,100,100,mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(mBitmap,0,0,mPaint);
        canvas.restore();
    }
}
