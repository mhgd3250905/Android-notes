# Android Chart框架 MPAndroidChart学习笔记17_数据模型DataSet
---
[点击这里查看项目源码](https://github.com/mhgd3250905/Android-notes)

## 1.DataSet

DataSet是所有数据集类的父类，比如LineDataSet、BarDataSet...

	public class LineDataSet extends DataSet { ...

```DataSet```代表了类型一致的数据集合，它的设计是为了从逻辑上区分```Chart```中不同类型的数据。
> 当然，作为DataSet的子类自然会享有父类的一系列方法。

## 2.自定义数据样式

- ```setValueTextColor(int color)```:设置数据标签颜色。
- ```setValueTextColors(List colors)```：设置一系列的颜色。
- ```setValueTypeface(Typeface tf)```：设置数据标签字体。
- ```setValueTextSize(float size)```：设置数据标签字体大小。
- ```setValueFormatter(ValueFormatter f)```:设置数据标签的样式。
- ```setDrawValues(boolean enabled)```:是否绘制数据标签。

## 2.获取数据 ##


- ```contains(Entry entry)```:检查数据中是否包含某个```Entry```数据，如果存在那么返回true，否则返回false。



## 4.高亮突出 ##

- ```setHighlightEnabled(boolean enabled)```：是否绘制数据高亮。

![高亮突出](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fd8tlljtvmj309y0cv74r)

> 很多方法和ChartData达到的效果是一致的...


