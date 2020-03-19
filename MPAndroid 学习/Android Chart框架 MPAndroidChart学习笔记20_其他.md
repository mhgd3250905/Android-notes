# Android Chart框架 MPAndroidChart学习笔记20_其他
---
[点击这里查看项目源码](https://github.com/mhgd3250905/Android-notes)

## 1.图表内容 ##

- ```clear()```:清除```chart```中所有的数据，然后使用方法```invalidate()```进行刷新；

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfse4b1q8j30a10cna9z)

- ```clearValues()```:清除所有```DataSet```对象的图表，从而清除所有条目。不从图表中删除提供的x值。调用```invalidate（）```刷新图表；
- ```isEmpty()```：返回```chart```是否为null或是不包含任何数据；

		mChart.clearValues();
        mChart.invalidate();
        if (mChart.isEmpty()) {
            Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
        }
**这里可以看到```clearValues()```和```clear()```的区别，以及```isEmpty()```的返回结果**

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfsejfcm3j30a20clq2w)

---

## 2.有用的get方法 ##

- ```getData()```：返回```chart```填充的data对象；
- ```getViewPortHandler```:返回视口处理器对象；
- ```getRenderer()```:返回负责绘制图表数据的主```DataRenderer```;
- ```getCenter```:返回整个图表的中心点；

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfsqqpwr9j309s0bm74r)

- ```getCenterOffset```:返回图表绘制区域的中心点；

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfsqxmqy4j30a30bg3yz)

- ```getYMin()```:返回Y值的最小值；
- ```getYMax()```:返回Y值的最大值；

		Log.d("BarChartFragment", "mChart.getYMin():" + mChart.getYMin());
        Log.d("BarChartFragment", "mChart.getYMax():" + mChart.getYMax());

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfswht0pjj30bv01sdfp)

- ```getLowestVisibleXIndex()```:返回在图表上仍然可见的最低x-index（x轴上的值）；
- ```getHighestVisibleXIndex()```：返回在图表上仍然可见的最高x-index（x轴上的值）;

		Log.d("BarChartFragment", "mChart.getLowestVisibleX():" + mChart.getLowestVisibleX());
        Log.d("BarChartFragment", "mChart.getHighestVisibleX():" + mChart.getHighestVisibleX());

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdft2912p4j30a20awwet)

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdft2fdmotj30dn01la9y)

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdft2o429gj30a009bjrg)

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdft2wimgej30eo01k0sn)

## 3.其他方法 ##

- ```saveToGallery(String title，int quality)```：将当前图表状态作为图像保存到图库。不要忘记添加权限```WRITE_EXTERNAL_STORAGE```权限，参数1标题，参数2图片质量[0~100]

		if (mChart.saveToGallery("test",100)) {
            Toast.makeText(getContext(), "图片保存成功", Toast.LENGTH_SHORT).show();
        }

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fdfte24b5uj30ak0cqabe)

- ```saveToPath(String title, String pathOnSD)```:将图表保存到指定位置；

- ```getChartBitmap()```:返回表示图表的Bitmap对象，此Bitmap始终包含图表的最新绘图状态；

- ```setHardwareAccelerationEnabled(boolean enabled)```:允许启用/禁用图表的硬件加速，**仅API级别11**；











