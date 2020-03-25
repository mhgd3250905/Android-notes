package com.skkk.bitmapoptimizationdemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {

    public static Bitmap decodeSampleBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight,boolean inPreferredConfigChange) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //第一次解码，只为获取宽高
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds=false;
        if (inPreferredConfigChange){
            options.inPreferredConfig= Bitmap.Config.RGB_565;
        }
        Bitmap src=BitmapFactory.decodeResource(res,resId,options);
        return createScaleBitmap(src,reqWidth,reqHeight);
    }

    /**
     * 将bitmap等比列缩小
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;

            while ((halfWidth / inSampleSize) > reqWidth && (halfHeight / inSampleSize) > reqHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 调用Bitmap.createScaleBitmap方法来调整bitmap的宽高
     * @param src
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static Bitmap createScaleBitmap(Bitmap src, int reqWidth, int reqHeight) {
        //如果是放大图片，filter决定是否平缓，如果是缩小，那么filter无影响
        Bitmap dst=Bitmap.createScaledBitmap(src,reqWidth,reqHeight,false);
        if (src!=dst){
            src.recycle();
        }
        return dst;
    }
}
