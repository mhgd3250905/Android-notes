# Android Chart框架 MPAndroidChart学习笔记16_数据模型CharData
---
[点击这里查看项目源码](https://github.com/mhgd3250905/Android-notes)

## 1.CharData

```CharData```是所有数据模型类的父类,比如```LineData```,```BarData```等等，他通过```setData()```方法来给我们的```Char```添加数据。

	public class LineData extends ChartData { ...

作为CharData的子类，当然都可以使用```CharData```中的方法。

## 2.自定义数据样式

- ```setValueTextColor(int color)```:设置数据标签颜色。
- ```setValueTextColors(List colors)```：设置一系列的颜色。
- ```setValueTypeface(Typeface tf)```：设置数据标签字体。
- ```setValueFormatter(ValueFormatter f)```:设置数据标签的样式。
- ```setDrawValues(boolean enabled)```:是否绘制数据标签。

## 2.获取数据 ##

- ```getDataSetByIndex(int index)```：返回给定序列的```DataSet```。
- ```contains(Entry entry)```:检查数据中是否包含某个```Entry```数据，如果存在那么返回true，否则返回false。
- ```contains(T dataSet)```:检查数据中是否包含某个```DataSet```数据，如果存在那么返回true，否则返回false。

## 3.清除数据 ##

- ```clearValues()```:清除数据。

## 4.高亮突出 ##

- ```setHighlightEnabled(boolean enabled)```：是否绘制数据高亮。

![高亮突出](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fd8tlljtvmj309y0cv74r)

## 5.动态更细你数据 ##

-```notifyDataChanged()```:动态更新数据。