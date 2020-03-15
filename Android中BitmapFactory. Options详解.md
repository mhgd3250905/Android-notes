# Android中BitmapFactory. Options详解

###  1. inJustDecodeBounds

如果将这个值设置为true，那么在解码的时候将不会反悔bitmap，只会返回bitmap的尺寸，这个属性的目的是，如果你想知道一个bitmap的尺寸，又不想将其加载到内存中时。这是一个非常有用的属性。

### 2. inSampleSize

这个值是一个int，设置之后会按照设置值来缩小bitmap的宽高，降低分辨率，比如设置为2，那么bitmap将被处理为宽高变为1/2,像素降为1/4。

```java
/**
 * 通过调整options.inSampleSize来修改大小
 */
private void optimizationBitmapByInSampleSize() {
    Log.d(TAG, "\n开始压缩图片");
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
    Log.d(TAG, String.format("压缩前\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person, options);
    Log.d(TAG, "options.inJustDecodeBounds设置为True,获取到的bitmap为：" + bitmap);
    options.inSampleSize = 2;
    options.inJustDecodeBounds = false;
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person, options);
    Log.d(TAG, String.format("压缩后\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
}
```

输出结果如下：

![inSampleSize](C:/Users/ROOT/Desktop/学习笔记/bitmap压缩/inSampleSize.png)

### 3. inPreferredConfig

这个值设置色彩模式，默认为ARGB_8888,这个模式下一个像素点占用4bytes空间，如果对透明度没有要求，一半采用RGB_565，这个模式不处理透明度，一个像素占用2bytes空间。

### 4. inPremultiplied

这个值和透明度通道相关，默认值为true，如果设置为true，那么返回的bitmap的颜色通道上会预先附加上透明度通道。

### 5. inDither

这个值和抖动解码有关，默认为false，表示不采用抖动解码。

### 6. inDensity

这个属性表示bitmap的像素密度（对应的是DisplayMetrics中的densityDpi）。

### 7. inTargetDensity

表示被画出来时的目标像素密度。

### 8. inScreenDensity

表示实际设备的像素密度。

### 9. inScaled

设置这个bitmap是否可以被缩放，默认是true，表示可以被缩放。

### 10. ~~inPurgeable和inInputShareable~~

这两个值一般是一起使用，设置为true时，前者表示空间不够是否可以被释放，后者表示是否可以共享应用。~~这两个值在Android5.0后被启用~~。

### 11. inPreferQualityOverSpeed

这个值表示是否在解码时图片有更高的品质，仅用于JPEG，如果设置为true，则图片会有更高的品质，但是解码速度会很慢。

### 12. outWidth和outHeight

表示这个这个Bitmap的宽和高，一半和inJustDecodeBounds一起使用来获得bitmap的宽高，但是不加载到内存。



