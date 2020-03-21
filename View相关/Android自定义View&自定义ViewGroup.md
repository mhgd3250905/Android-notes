# Android自定义View&自定义ViewGroup

一半自定义View需要重写的方法：

```java
void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
void onSizeChanged(int w, int h, int oldw, int oldh)
void onDraw(Canvas canvas)
```

一般自定义ViewGroup需要重写的方法：

```java
void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
void onSizeChanged(int w, int h, int oldw, int oldh)
void onLayout(boolean changed, int left, int top, int right, int bottom)
void onDraw(Canvas canvas)
```

可以看出，自定义ViewGroup时必须要重写onLayout()方法（依次排列子View），而自定义View没有子View，所以不需要onLayout()，后面会分别给出自定义View和自定义ViewGroup的例子。

在网上看到一张View的生命周期图：

![自定义View_6](.\img\自定义View_6.webp)

先来看一下自定义View的作用：

1. 在onMeasure中根据测量模式和ViewGroup给出的建议宽和高，计算出自己的宽和高。
2. 在onDraw中绘制自己的形态。



这里不考虑其他，主要学习布局相关的方法。



## View常用重写方法

### onMeasure

MeasureSpec是View的一个内部类，一般用到它的MeasureSpecMode（测量模式）和Size（测量大小），其中MeasureSpecMode有以下三种模式：

#### UNSPECIFIED

父View对子View没有任何限制，子View可以是任意大小；

#### EXACTLY

父View已经强制设置了子View的大小，一般是MATCH_PARENT和固定值；

#### AT_MOST

子View限制在一个最大范围内，一般是WRAP_CENTENT，最后测量完成后通过```setMeasureDimension(int measureWidth, int measureHeight)```将测量的高度回传给父View；

### onSizeChanged

在onMeasure()后执行，只有大小发生了变化才会执行onSizeChange。

### onDraw

系统给我们提供了空白的Canvas空白画布，我们可以通过Canvas和Paint来绘制我们想要的图形。

**注： onDraw()函数可能会多次调用，所以避免在onDraw()函数中去new对象**



## ViewGroup常用重写方法

### onMeasure

measureChildren方法触发所有子View的onMeasure方法测量自己并把测量交过回传给ViewGroup（ViewGroup传递给子View的建议宽高和测量模式，如果子View的宽高是wrap_content，那么只有子View测量出自己的宽高），当所有子View测量完毕后，再调用setMeasureDimension将ViewGroup自身的宽高传给它的父View。

### onSizeChanged

在onMeasure()后执行，只有大小发生了变化会执行onSizeChanged；

### onLayout

排列所有的子View的位置；

通过getChildCount()获得所有的子View，getChildAt获取childView调用各自的layout(int, int, int, int)方法来排列自己；

### onDraw

自定义ViewGroup默认不会触发onDraw方法，需要设置背景色或者setWillNotDraw(false)来手动触发；

