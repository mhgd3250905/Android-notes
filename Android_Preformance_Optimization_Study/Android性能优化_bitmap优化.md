# Bitmap优化

### 1. 主动释放Bitmap资源

单例确定这个Bitmap资源不再会被使用的时候，建议手动调用```recycle()```方法，释放其Native内存：

```java
if(bitmap != null && !bitmap.isRecycled()){
    bitmap.recycle();
    bitmap = null;
}
```

调用```birmap.recycle()```方法后，这个Bitmap如果没有被应用到，那么就会被垃圾回收器回收。

### 2. 主动释放ImageView的资源

由于我们再实际开发中，很多情况是在xml中设置ImageView的src或者在代码中设置图像，下面的代码可以回收ImageView所对应的资源：

```java
private static void recycleImageViewBitMap(ImageView imageView) {
    if (imageView != null) {
        BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
        rceycleBitmapDrawable(bd);
    }
}

private static void rceycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
	if (bitmapDrawable != null) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		rceycleBitmap(bitmap);
	}
	bitmapDrawable = null;
}

private static void rceycleBitmap(Bitmap bitmap) {
	if (bitmap != null && !bitmap.isRecycled()) {
		bitmap.recycle();
		bitmap = null;
	}
}
```

### 3. 主动释放ImageView的背景资源

以下代码可以主动释放ImageView设置的Background(如果有设置的话)：

```java
public static void recycleBackgroundBitMap(ImageView view) {
	if (view != null) {
		BitmapDrawable bd = (BitmapDrawable) view.getBackground();
		rceycleBitmapDrawable(bd);
	}
}

public static void recycleImageViewBitMap(ImageView imageView) {
	if (imageView != null) {
		BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
	}
}

private static void rceycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
	if (bitmapDrawable != null) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		rceycleBitmap(bitmap);
	}
	bitmapDrawable = null;
}
```

###  4. 尽量少用Png图，多用NinePatch图

Android 2D渲染器对点9图有优化，其次较大的Png图很占资源；

Android中的NinePatch指的是一种拉伸后不会变形的特殊png图，NinePatch的拉伸区域可以自己定义。这种图的优点是体积小，拉伸不变形，可以适配多机型。Android SDK中有自带NinePatch资源制作工具，Android-Studio中在普通png图片点击右键可以将其转换为NinePatch资源，使用起来非常方便。



### 5. 使用大图之前，尽量先对其进行压缩

#### 5.1 图片大小压缩

直接使用ImageView显示Bitmap会占用较多资源，特别是图片较大时，可能导致崩溃。

使用```BitmapFactory.Options```设置```inSampleSize```，这样做可以减少对系统资源的要求。

属性```inSampleSize```表示缩略图为原始图片大小的几分之一，如果设置为2，那么取出的缩略图的宽高均为原始图片的1/2，图片大小就位原始图片的1/4

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

![inSampleSize](.\bitmap压缩\inSampleSize.png)

#### 5.2 图片像素压缩

Android中图片有四种属性，分别是：

- ALPHA_8：每个像素占用1byte内存
- ARGB_4444: 每个像素占用2byte内存
- ARGB_8888：每个像素占用4byte内存**（默认）**
- RGB_565：每个像素占用2byte内存

Android默认颜色模式为ARGB_8888，这个模式色彩最细腻，显示质量最高，占用的内存也最大，所以在对图片效果不是特别高的情况下使用RGB_565（565没有透明度属性），如下:

```java
![inPreferredConfig](C:\Users\ROOT\Desktop\学习笔记\bitmap压缩\inPreferredConfig.png)/**
 * 通过调整options.inSampleSize来修改大小
 */
private void optimizationBitmapByInPreferredConfig() {
    Log.d(TAG, "\n开始压缩图片");
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
    Log.d(TAG, String.format("压缩前\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
    BitmapFactory.Options options=new BitmapFactory.Options();
    options.inPreferredConfig= Bitmap.Config.RGB_565;
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person, options);
    Log.d(TAG, String.format("压缩后\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
}
```

输出结果：

![inPreferredConfig](.\bitmap压缩\inPreferredConfig.png)

#### 5.4 复用

图片复用指的是```inBitmap```这个属性。

不使用这个属性，加载三张图片，系统给你分配三份内存，分别存储。

如果用了```inBitmap```这个属性，加载三张图片，这三张图片会指向同一块内存，而不用开辟新的内存。

**```inBitmap```的限制**

- 3.0-4.3
  - 复用的图片大小必须相同
  - 编码必须相同
  
- 4.4以上
  - 复用的空间大于等于即可
  - 编码不必相同
  
- 不支持WebP
  
- 图片复用，这个属性必须设置为true：
  
```java
  options.inMutable = true;
```
#### 5.5 匿名共享内存（Ashmen）

Android 5.0之后就限制了匿名共享内存的使用

图片到底存储在哪里：

|    2.3-    |   3.0-4.4   | 5.0-7.1                   | 8.0                       |
| :--------: | :---------: | ------------------------- | ------------------------- |
| Bitmap对象 |  java Heap  | java Heap                 | java Heap                 |
|  像素数据  | Native Heap | java Heap                 | Native Heap               |
|  迁移原因  |             | 解决Native Bitmap内存泄漏 | 共享整个系统的内存减伤OOM |

Android8.0 Bitmap的像素数据存储在Native，为什么又改为Native存储呢？

因为8.0共享了整个系统的内存，测试8.0手机如果一直创建Bitmap，如果手机内存有1G，那么你的应用加载1G也不会oom

  

#### 5.6 LRU管理Bitmap

可以利用LRU管理Bitmap，设置内存最大值，即使回收



#### 5.7 图片的压缩

图片的而压缩有两种：

- 通过采样压缩，如上面的方法

- 质量压缩

```java
/**
 * 通过调整bitmap.compress来修改图片质量
 */
private void optimizationBitmapByCompress(Bitmap bitmap) {
    Log.d(TAG, "\n开始压缩图片");
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
    Log.d(TAG, String.format("压缩前\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG,20, baos);
    bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
    Log.d(TAG, String.format("压缩后\tw: %d\th: %d\tsize: %d", bitmap.getWidth(), bitmap.getHeight(), bitmap.getByteCount()));
}
```

这个压缩是保持像素的前提下高边图片的位深以及透明度，达到压缩的目的，不过这种压缩不会改变图片在内存中的大小，而且这种压缩会导致图片失真。

输出结果：

![compress](.\bitmap压缩\compress.png)

[压缩且不失真的方法](https://www.jianshu.com/p/06a1cae9c153)



#### 5.8 如何加载高清图

如果有需要我们既不能压缩图片，又不能发生oom怎么办，这种情况我们需要加载图片的一部分区域来显示，使用```BitmapRegionDecoder```这个类，加载图片的一部分区域：

```java
//支持传入图片的路径，流，和图片修饰符等
BitmaRegionDecoder mDecoder = BitmapRegionDecoder.newInstance(path,false);
//需要显示的区域就由rect控制，options来控制图片的属性
Bitmap bitmap=mDecoder.decodeRegion(mRect,options);
```

由于要显示一部分区域，所以要有手势的控制，方便上下滑动，需要自定义控件，而自定义控件思路也很简单：

1. 提供图片的入口

2. 重写onTouchEvent，根据收拾的移动更新显示区域的参数

3. 更新区域参数后，刷新控件重新绘制

