package com.example.pathdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class PathView_2 extends View {
    private static final String TAG = "PathView_1";

    private Paint paint;
    private Path path=new Path();
    private float start;
    private PathMeasure pathMeasure;
    private float mFloat;
    private float[] pos=new float[2];
    private float[] tan=new float[2];
    private Matrix mMatrix = new Matrix();
    private Bitmap bitmap;

    public PathView_2(Context context) {
        super(context);
    }

    public PathView_2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView_2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PathView_2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);


        //缩小图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher,options);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth()/2,getHeight()/2);

        path.reset();
        path.addCircle(0,0,200,Path.Direction.CW);
        canvas.drawPath(path,paint);

        mFloat+=0.005;
        if (mFloat>=1){
            mFloat=0;
        }

        PathMeasure pathMeasure=new PathMeasure(path,false);
        pathMeasure.getPosTan(mFloat*pathMeasure.getLength(), pos,tan);
        //计算出当前的切线与x轴的夹角的度数
        double degress=Math.atan2(tan[1],tan[0])*180/Math.PI;
        mMatrix.reset();
        //进行角度旋转
        mMatrix.postRotate((float) degress,bitmap.getWidth()/2,bitmap.getHeight()/2);
        //将图片的绘制点中心与当前点重合
        mMatrix.postTranslate(pos[0]-bitmap.getWidth()/2,pos[1]-bitmap.getHeight()/2);
        canvas.drawBitmap(bitmap,mMatrix,paint);
        invalidate();

    }
}
