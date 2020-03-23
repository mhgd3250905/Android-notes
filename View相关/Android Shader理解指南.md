## Andrid Shader理解指南

### 概念

Android中的shader，其实基本都是自定义view的时候会用到，而且是将shader赋予给paint，类似这样：

```java
Paint paint = new Paint();
Shader shader = new Shader();
paint.setShader(shader);
```

但是大家一般不会直接使用Shader，而是使用Shader的子类，Shader的子类有5个，如图：

<img src=".\img\shader_1.jpg" alt="shader_1" style="zoom:50%;" />

在讲这些子类之前，我希望大家先理解了shader的实质，然后再去学习这些子类，我想，这样会轻松很多。

shader按照官方的解释是：

> Shader是Object的子类，这些对象在绘制过程中返回颜色的水平跨度。通过Paint.setShader(shader)来使用Shader。调用了该方法后，用该paint绘制的任何对象(bitmap除外)都将从shader获得其颜色。

这个时候，我们需要换一种理解方式来理解手机屏幕上面的字，如图：

<img src=".\img\shader_2.jpg" alt="shader_2" style="zoom:50%;" />

这幅图上的东西是什么，白底？黑字？再也别这样理解了，现在在学习shader呢，换一种理解方式。

这张图片是由2张大小相同的纸构成的，第一张纸，纯黑色，第二张纸，纯白色的，不过第二张纸不是完整的，中间被扣走了部分，被扣走的部分，刚好看着有点像“演示文字”4个大字。然后将这两张纸重叠后，最终形成了如图所示的效果。

类似这种理解：

<img src=".\img\shader_3.gif" alt="shader_3" style="zoom:50%;" />

好的，现在我们已经丢掉了传统的思维方式：在白纸上写字。

OK，我现在要来讲shader了，shader其实就和两张纸的思维方式一样，可以这样强行理解shader，shader就是下面那张黑纸，画笔在白纸上画，或者说划？划破白纸，扣出某个形状，再搭在黑纸上，就能看到那个形状了。

比如我们把上图的那4个字的底下那层黑纸给换成一张图片，按照我们的最新的理解方式，文字就会是这样的:

<img src=".\img\shader_4.jpg" alt="shader_4" style="zoom:50%;" />

我们把上面那层白纸换成有点透明度的白纸，来看看全貌：

<img src=".\img\shader_5.jpg" alt="shader_5" style="zoom:50%;" />

好了，跟着思路，我们开始，shader现在我们已经强行理解成下面那层的图片了，也就是说，当我们在作出如下行为的时候：

```java
Paint paint = new Paint();
Shader shader = new Shader();
paint.setShader(shader);
```

实际上是修改了底层的图层，并且绘图模式变成了在上面那层扣形状，然后重叠，将下面那层的颜色通过被扣除的部分显示出来，赋予这个形状不一样的颜色。

所以，当我们为画笔设置shader的时候，使用`paint.setColor()`这种方法就显的没有意义了。

### BitmapShader

首先我们先讲BitmapShader，容易跟上节奏，BitmapShader是Shader的子类，在使用他的时候，要传递一个bitmap，可想而知，这个bitmap就成了这个图像的底层。
我们拿这张图片作为例子底层

<img src=".\img\shader_6.jpg" alt="shader_6" style="zoom:50%;" />

```java
Paint paint = new Paint();
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shader);
BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
paint.setShader(shader);
```

待会再讲里面的Shader.TileMode是干嘛的，现在先这样填写，现在这个paint就被赋予了魔力，我们在onDraw中使用这个paint写几个字：

```java
@Override
protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	paint.setTextSize(230);
	paint.setStyle(Paint.Style.FILL);
	canvas.drawText("演示文字", 100, 500, paint);
}
```

没错，效果就是之前看到的：

<img src=".\img\shader_4.jpg" alt="shader_4" style="zoom:50%;" />

然后画圆画方：

```java
@Override
protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	paint.setStyle(Paint.Style.FILL);
	canvas.drawCircle(200, 200, 100, paint);
	canvas.drawRect(400, 100, 800, 300, paint);
}
```

<img src=".\img\shader_7.jpg" alt="shader_7" style="zoom:50%;" />



接下来就来解释一下`BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);`中后面哪两个参数具体是做什么的。

首先`Shader.TileMode`顾名思义肯定是一种模式，到这里就可以理解为是一种显示模式，既然怒这个类，我们会发现一共有CLAMP，REPEAT，MIRROR三种显示模式。而方法中第一个`Shader.TileMode`是X轴的显示模式，第二个是Y轴上的显示模式。

- `BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);`

  当图片所不能及的区域，就取图片对应轴上最后一个像素对齐复制；

<img src=".\img\shader_8.jpg" alt="shader_8" style="zoom:50%;" />

- `BitmapShader(bitmap1, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);`

  这个就是镜面对称；

<img src=".\img\shader_9.jpg" alt="shader_9" style="zoom:50%;" />



- `BitmapShader(bitmap1, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);`

  这个就是无限重复；

  <img src=".\img\shader_10.jpg" alt="shader_10" style="zoom:50%;" />

### LinearGradient

这里开始介绍LinearGradient。

```java
LinearGradient linearGradient = new LinearGradient(100, 100, 500, 500, Color.RED, Color.GREEN, Shader.TileMode.CLAMP);
paint.setShader(linearGradient);
```

在onDraw方法中：

```java
paint.setStyle(Paint.Style.FILL);
canvas.drawRect(new Rect(0, 0, 1080, 1920), paint);
        
// 为了展现上面的100, 100, 500, 500到底在哪个区域
paint.setStyle(Paint.Style.STROKE);
paint.setShader(null);
paint.setStrokeWidth(2);
canvas.drawRect(new Rect(100, 100, 500, 500), paint);
```

<img src=".\img\shader_11.jpg" alt="shader_11" style="zoom:50%;" />

### SweepGradient

```java
SweepGradient sweepGradient = new SweepGradient(200, 200, Color.RED, Color.GREEN);
paint.setShader(sweepGradient);
```

```java
@Override
protected void onDraw(Canvas canvas) {
	super.onDraw(canvas);
	paint.setStyle(Paint.Style.FILL);
	canvas.drawCircle(200, 200, 100, paint);
}
```

<img src=".\img\shader_12.jpg" alt="shader_12" style="zoom:50%;" />