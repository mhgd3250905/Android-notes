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

![18-2](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdeor8o5wuj30df0i73zu)

- ```setDrawCircles(boolean enabled)```:设置是否打开圆形指示器；

**看一下打开和不打开的区别**

![18-3](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdeotd7d9fj30dg0i50u0)

- ```setCircleColor(int color)```:设置圆圈颜色；
- ```setCircleColors(List colors)```:设置圆圈颜色；
- ```setDrawCircleHole(boolean enabled)```:设置内圈颜色；

**来看一看这恶心的配色**

![18-4](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdep4s3xrqj30dm0inq4c)

- ```enableDashedLine(float lineLength, float spaceLength, float phase)```：设置虚线，这个在前面有写过

**虚线**

![18-5](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdep54md5qj30d60ieq48)

看一下代码设置：

		dataSet.setDrawCircles(true);
        dataSet.setCircleRadius(20f);
        dataSet.setCircleColor(Color.YELLOW);
        dataSet.setCircleColorHole(Color.GREEN);

        dataSet2.setDrawCircles(false);
        dataSet2.enableDashedLine(5f,5f,0f);

---
> **BarDataSet **

- ```setBarShadowColor(int color)```：设置柱形的阴影颜色；
- ```setHighLightAlpha(int alpha)```：设置高亮选中透明值，[0~255]:0为完全透明，255为全不透明；

**这里设置为255**

![高亮透明值](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfrgpj143j30ad0asaab)

- ```setStackLabels(String[] labels)```:为条形堆栈的不同值设置标签，如果有的话。
---

> **PieDataSet**

- ```setSliceSpace(float degrees)```:设置饼块之间的距离[0~20f]：最小为0没有距离，最大为20f

**这里设置为0**

![setSliceSpace=0](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfrnb0wr8j309j09pq36)

**这里设置为10f**

![setSliceSpace=10f](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfrnz3mx5j30a10a9jrp)

- ```setSelectionShift(float shift)```:设置被选中区域的激凸程度，默认为12f

**这里设置为50f**

![setSelectionShift](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfrqogi4bj309r0a93ys)