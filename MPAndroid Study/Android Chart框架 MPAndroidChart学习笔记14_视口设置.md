# Android Chart框架 MPAndroidChart学习笔记14_视口设置
---
## 1.视图 ##
```viewPort```视图针对以下类别的chart有效：

- LineChart
- BarChart
- ScatterChart
- CandleStickChart

以下方法适用于Chart类，请注意，视图操作必须要在chart数据设置完毕再之后再调用。

## 2.修改视图 ##

- ```setVisibleXRangeMaximum(float maxXRange)```：可以显示的最大x轴范围
- ```setVisibleXRangeMinimum(float maxXRange)```：可以显示的最小x轴范围

		//设置视口可以显示的最大x范围
        mChart.setVisibleXRangeMaximum(30f);
![setVisibleXRangeMaximum](http://ww1.sinaimg.cn/large/006aPzcjgy1fd5e78jbakj30c50fnaah)

        //设置视口可以显示的最小x范围
        mChart.setVisibleXRangeMinimum(25f);

**因为最小就是25f 所以会显示出多余的空白部分**    
![setVisibleXRangeMinimum](http://ww1.sinaimg.cn/large/006aPzcjgy1fd5e83q1anj30bz0fo3z8)


-```setVisibleYRangeMaximum(float maxYRange, AxisDependency axis)```：设置第一屏显示Y轴方向显示的最大范围
		//设置Y轴可以显示的最大值

        mChart.setVisibleYRangeMaximum(500f,YAxis.AxisDependency.LEFT);   

![setVisibleYRangeMaximum](http://ww1.sinaimg.cn/large/006aPzcjgy1fd5e9idpd8j30c30dngm5)
        
-```setViewPortOffsets(float left, float top, float right, float bottom)```：设置视口在view中的偏移（默认值是有一定的偏移的）

**如果设置为setViewPortOffsets（0f,0f,0f,0f）会如下显示：**   
![setViewPortOffsets](http://ww1.sinaimg.cn/large/006aPzcjgy1fd5e9wqx5qj30cd0ggjs3)   
**如果设置为setViewPortOffsets（30f,30f,30f,30f）会如下显示：**   
![setViewPortOffsets](http://ww1.sinaimg.cn/large/006aPzcjgy1fd5eak5d0nj30c40fvwfa)

-```resetViewPortOffsets()```:重置视口偏移

**demo：**

		//设置视口可以显示的最大x范围
        mChart.setVisibleXRangeMaximum(30f);
        //设置视口可以显示的最小x范围
        mChart.setVisibleXRangeMinimum(0f);
        //设置Y轴可以显示的最大值
        mChart.setVisibleYRangeMaximum(500f,YAxis.AxisDependency.LEFT);
        //设置视口View偏移
        mChart.setViewPortOffsets(0f,0f,0f,0f);
        //添加额外的视口偏移
        mChart.setExtraOffsets(-10f,-10f,-10f,-10f);
        //重置视口偏移
        mChart.resetViewPortOffsets();

## 2.移动视口View ##

-```fitScreen()```:将左（边）的当前视口指定值。
-```moveViewToX(float xValue)```:经过缩放之后重新使左边从指定位置开始。
-```moveViewTo(float xValue, float yValue, AxisDependency axis)```:经过缩放之后重新使视口垂直中点为指定数值。
-```centerViewTo(float xValue, float yValue, AxisDependency axis)```:经过缩放之后重新使视口垂直中点为指定点。
-```moveViewToAnimated(float xValue, float yValue, AxisDependency axis, long duration)```:有动画的移动视口View
-```centerViewToAnimated(float xValue, float yValue, AxisDependency axis, long duration)```:有动画的移动视口View到指定中心点

## 3.缩放 ##

-```zoomIn()```:放大1.4f（默认）倍
-```zoomOut()```：缩小0.7f（默认）倍
-```zoom(float scaleX, float scaleY, float x, float y)```：缩放指定倍数
-```zoom(float scaleX, float scaleY, float xValue, float yValue, AxisDependency axis)```:缩放指定倍数并指定缩放的Y轴

-```zoomAndCenterAnimated(float scaleX, float scaleY, float xValue, float yValue, AxisDependency axis, long duration)```:有动画的缩放(**使用会报错~**)
