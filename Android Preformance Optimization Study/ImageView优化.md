# ImageView优化

### 1. ImageView内存释放

使用完ImageView后，获取imageViewd对应的drawable，然后获取起哄的bitmap：

```java
bitmap.recycle();
bitmap = null;
```

在列表类型布局中，清除之后还需要：

```java
adapter.notification();
```

### 2. 各种格式设置分析

设置图片的方式：

- 在布局文件中设置属性```android:src="@drawable/resId"```加载图片
- ```setImageResource(int resId)```：加载drawable文件夹中的资源文件
- ```setImageURI(Uri)```：加载手机内存卡中的图片格式文件
- ```setImageBitmap(Bitmap)```：加载Bitmap
- ```setImageDrawable(Drawable)```：加载Drawable

分析可知：

- 所有图片格式，不管是资源还是Uri还是Bitmap，都会转化为Drawable；
- ```setImageURI(Urri)```方法，设计到文件转化为文件刘，然后将文件流解析为Bitmap操作，需要注意的是在主线程中做这些操作可能会造成延时；
- 针对性能方面给上述加载图片的方法排序，从劣到优：```setImageURI() ```< ```setImageBitmap()``` < ```setImageRecource()``` < 属性设置 < ```setImageDrawable()```，可以肯定，```setImageDrawable()```是最优设置方式。

![image_ScaleType](.\bitmap压缩\image_ScaleType.png)

### 3. 总结

- ```setImageURI()```方法存在对Uri代表的图片文件转化为文件流而后解析为Bitmap的操作，可能会存在延时，需要注意一下；
- 设置图片的最优方式是```setImageDrawable()```方法；
- 在不居中通过src属性展示图片实际调用的并非```setImageResource```方法，二十先将resId转化为Drawable，然后通过```setImageDrawable()```方法加载；
- Drawable是Android系统中图片绘制前的最终形式，图片如何会知道canvas上，也是通过Drawable来的，区别在于在内存中存在的最终形态为Bitmap；
- 自定义View时，绘制图片，使用```Drawable.setBounds()```确定边界后，再调用```drawable.draw(canvas)```方法绘制会比```canvas.drawBitmap()```更简单强大，前者还能绘制shape.xml文件转化的图片；
- ImageView默认缩放类型为FIX_CENTER;
- ImageView缩放内存对家在图片本身占用的内存大小并没有关系，仅仅是缩放图片内容展示的边界而已；
- 图片占用内存大小，软弱围资源文件，则同drawable文件夹代表的密度和设备屏幕密度有关；若为网络或文件，则为图片本身大小；
- 图片的压缩方式分两种：质量压缩和分辨率压缩，前者能减少图片质量，但对图片分辨率并没有影响，图片占用的内存大小不会改变，常常用于上传网络图片时上传直接大小的限制。后者压缩分辨率，会改变图片的清晰度，占用内存也会减少，详情请见[Android 性能优化 - bitmap优化](Android 性能优化 - bitmap优化.md)；