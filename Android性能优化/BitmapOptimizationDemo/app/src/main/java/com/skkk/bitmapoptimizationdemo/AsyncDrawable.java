package com.skkk.bitmapoptimizationdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class AsyncDrawable extends BitmapDrawable {
    private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
    private Bitmap mPlaceHolderBitmap;//占位符
    private Resources res;

    public AsyncDrawable(Resources res, Bitmap mPlaceHolderBitmap, BitmapWorkerTask bitmapWorkerTask) {
        super(res, mPlaceHolderBitmap);
        bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        this.mPlaceHolderBitmap=mPlaceHolderBitmap;
    }

    public void loadBitmap(int resId, ImageView imageView) throws Exception {
        if (imageView.getWidth()<=0||imageView.getHeight()<=0){
         throw new Exception("image width and height can not be <= 0!");
        }
        if (cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, imageView.getWidth(), imageView.getHeight());
            final AsyncDrawable asyncDrawable = new AsyncDrawable(res, mPlaceHolderBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    /**
     * 判断是否已经有一个任务正在加载图片
     *
     * @param data
     * @param imageView
     * @return
     */
    private boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkTask();

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            //判断是否已经有一个任务正在执行
            //如果没有，那么直接通过
            //如果有但是记载的是其他图片，那么直接切换
            //还是同样的图片资源，那么继续加载即可
            if (bitmapData == 0 || bitmapData != data) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    public BitmapWorkerTask getBitmapWorkTask() {
        return bitmapWorkerTaskReference.get();
    }

    public static BitmapWorkerTask getBitmapWorkTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkTask();
            }
        }
        return null;
    }


}
