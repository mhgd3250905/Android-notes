# Android Chart框架 MPAndroidChart学习笔记16_数据模型CharData
---
[点击这里查看项目源码](https://github.com/mhgd3250905/Android-notes)

## 1.CharData

```CharData```是所有数据模型类的父类,比如```LineData```,```BarData```等等，他通过```setData()```方法来给我们的```Char```添加数据。

	public class LineData extends ChartData { ...

作为CharData的子类，当然都可以使用```CharData```中的方法。

## 2.自定义数据样式
