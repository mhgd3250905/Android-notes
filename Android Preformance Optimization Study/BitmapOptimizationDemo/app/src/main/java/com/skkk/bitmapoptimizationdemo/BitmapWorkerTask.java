package com.skkk.bitmapoptimizationdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private Context context;
    private final WeakReference<ImageView> imageViewReference;
    private int reqWidth;
    private int reqHeight;
    public int data = 0;

    public BitmapWorkerTask(ImageView imageView,int reqWidth,int reqHeight) {
        imageViewReference = new WeakReference<>(imageView);
        this.reqWidth = reqWidth;
        this.reqHeight =reqHeight;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        return BitmapUtils.decodeSampleBitmapFromResource(context.getResources(), data,
                reqWidth, reqHeight, true);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = AsyncDrawable.getBitmapWorkTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
