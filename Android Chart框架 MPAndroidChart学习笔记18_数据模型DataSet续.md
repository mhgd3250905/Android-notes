# Android Chart框架 MPAndroidChart学习笔记18_数据模型DataSet续
---
[点击这里查看项目源码](https://github.com/mhgd3250905/Android-notes)

## 1.DataSet续

> 适用于**Line-, Bar-, Scatter-, Bubble- & CandleDataSet**

- ```setHightColor(int color)```:给选择高亮进行颜色设置

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdb849k58qj30br0eot9f)

##

> **Line-, Bar-, Scatter-, Candle- & RadarDataSet**

- ```setDrawHighlightIndicators(boolean enabled)```：设置水平或垂直方向的高亮选择器，如```setDrawVerticalHighlightIndicator(...)```为设置垂直方向的高亮选择器，而```setDrawHorizontalHighlightIndicator(...)```：设置水平方向的高亮选择器

- ```setHighlightLineWidth(float width)```：设置高亮选择器的宽度

##
> **Line- & RadarDataSet**

- ```setFillColor(int color)```:设置线条的颜色
- ```setFillAlpha(int apha)```:设置线条的透明度（0~255），默认值为85,0表示全部透明，255表示全不透明。 

> **LineDataSet**

- ```setCircleRadius(float size)```：设置圆形指示器的半径；

**这里把半径设置的比较大：**
![17-2](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdeor8o5wuj30df0i73zu)

- ```setDrawCircles(boolean enabled)```:设置是否打开圆形指示器；

**看一下打开和不打开的区别**
![17-3](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdeotd7d9fj30dg0i50u0)
