# Android Chart框架 MPAndroidChart学习笔记19_视口处理器
---
[点击这里查看项目源码](https://github.com/mhgd3250905/Android-notes)

# The ViewPortHandler

ViewPortHandler类负责处理图表的视图端口。这意味着它负责在图表视图中可见的内容，它是翻译和缩放/缩放级别的当前状态，图表的大小，绘图区域和当前偏移量。 ViewPortHandler允许直接访问所有上述属性并修改它们。
> 与通过Chart类修改视图端口不同，如下所述，直接修改ViewPortHandler并不总是改变可见内容的完全安全的方式，应该由熟悉API的人仔细执行。不正确的使用可能导致意外的行为。但是，ViewPortHandler提供了更高级的视图端口修改方法。
> 
## 1.获取实例

	ViewPortHandler handler = chart.getViewPortHandler();

## 2.缩放以及移动 ##

> 以下方法均使用于ViewPortHolder实例；

- ```getScaleX()```:返回当前X轴的缩放比例；
- ```getScaleY()```:返回当前Y轴的缩放比例；
- ```getTransX()```:返回当前在X轴上的平移；
- ```getTransY()```:返回当前在Y轴上的平移；

## 3.图表尺寸 ##

- ```getChartWidth()```:返回当前图表的宽度；
- ```getChartHeight()```:返回当前图表的高度；
- ```getContentRect()```:将当前图表的尺寸以```RectF```的形式进行返回；


