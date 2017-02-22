# Android Chart框架 MPAndroidChart学习笔记11_图表的设置和样式
---
## 1.刷新图表 ##

- ```invalidate()```:用来刷新图表

- ```notifyDataSetChanged()```:图表底层数据变化之后调用此方法进行刷新

## 2.Logging ##

调用```setLogEnabled(boolean enabled)```方法传入True可以设置图标logcat输出，但是考虑到性能我们一般不设置

## 3.一般图标设置 ##

- ```setBackgroundColor(int color)```设置图标背景颜色，当然也可以在```xml```文件中设置。

		/* @描述 图表的基本设置 */
	    private void setGeneralStyling(BarChart chart) {
	        //设置背景
	        chart.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
	    }

![设置背景](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fcze6slvasj30gf0qpjsf)

- ```setDescription(Description desc)```设置chart描述：

		//设置图表右下角出现的说明文本,以及说明文本的基本设置。
        //初始化一个Description对象
        Description desc=new Description();
        //设置文本内容
        desc.setText(getString(R.string.desc));
        //设置文本大小
        desc.setTextSize(20f);
        //设置文本颜色
        desc.setTextColor(Color.WHITE);
        //设置文本位置，文本右上角相对于（0，0）的坐标位置
        desc.setPosition(400f,400f);
        //引入说明文本
        chart.setDescription(desc);

![设置说明文本](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczerjs1d9j30c20hqjry)

- ```setNoDataText(String text)```设置没有数据时候显示的文本：
		
		//设置图表数据为空时候的文本
        chart.setNoDataText(getString(R.string.Nodata));
![设置图表数据为空时候的文本](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczesp5z5dj30cu0jdwev)

- 设置网格背景：

		//是否绘制图表背景的网格（开关，如果设置为false那么网格设置都失效）
        chart.setDrawGridBackground(true);
        //设置网格背景颜色
        chart.setGridBackgroundColor(Color.YELLOW);

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczeza6huej30c90injs4)

- 设置边框

		//设置边框开关
        chart.setDrawBorders(true);
        //设置边框颜色
        chart.setBorderColor(Color.CYAN);
        //设置边框宽度
        chart.setBorderWidth(20f);
![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczf1qpesoj30cm0jq0tn)

- 设置图表上最大可见绘制值标签的数目
- 
		//设置图表上最大可见绘制值标签的数目。这只需要影响setdrawvalues()时启用。
        chart.setMaxVisibleValueCount(30);

## 4.特殊图标样式设置

### Line-, Bar-, Scatter-, Candle- & BubbleChart ###
- ```setAutoScaleMinMaxEnabled(boolean enabled)```如果设置为true，自动去匹配chart数据的最小值与最大值，默认为false
- ```setKeepPositionOnRotation(boolean enabled)```设置为true那么图标在放大之后无法进行拖拽，默认为false

		//用来描述Y轴的显示，如果设置为true，那么Y轴会显示X变化范围内Y值最大的变化范围
        chart.setAutoScaleMinMaxEnabled(false);
        //设置为true那么图标在放大之后无法进行拖拽，默认为false。
        chart.setKeepPositionOnRotation(false);


### BarChart ###

- ```setDrawValueAboveBar(boolean enabled)```如果设置为true，那么所有的值都会显示在bar的上方，而不是在顶部。
-```setDrawBarShadow(boolean enabled)```是否显示bar后方的一个阴影，以降低40%的性能？

		//默认在bar的顶部，设置为true之后就会显示在bar上方
        chart.setDrawValueAboveBar(true);
        //是否显示bar后方的一个阴影，以降低40%的性能？
        chart.setDrawBarShadow(false);
![setDrawValueAboveBar](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczkw6s8y2j30dj0943yq)

### PieChart ###
	
		//设置饼快的标签
        mChart.setDrawSliceText(true);
        //设置为true，那么饼块的内容会按照相应数据的百分比显示，否则显示实际值
        mChart.setUsePercentValues(false);
        //设置中心文字
        mChart.setCenterText(getString(R.string.centerText));
        //可以理解为设置中心文字的区域宽度，如果文字超过宽度会进行换行
        // 实际计算为内径的按照百分比缩小后作为中心文字区域的宽度
        mChart.setCenterTextRadiusPercent(20f);
        //可以理解为设置内径
        mChart.setHoleRadius(40f);
        //设置半透明圈的半径
        mChart.setTransparentCircleRadius(50f);
        //设置半透明圈的颜色
        mChart.setTransparentCircleColor(getResources().getColor(R.color.colorPrimary));
        //设置半透明圈的透明度[0~255]
        mChart.setTransparentCircleAlpha(99);
        //设置饼图的最大角度：默认为360°
        mChart.setMaxAngle(90f);

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczlhgyb2kj30du0esaaw)

**当然你如果想要也可以这样：**

![](http://ww1.sinaimg.cn/mw690/006aPzcjgy1fczlhurs3rj30cl08uaag)

		//设置饼快的标签
        mChart.setDrawSliceText(true);
        //设置为true，那么饼块的内容会按照相应数据的百分比显示，否则显示实际值
        mChart.setUsePercentValues(false);
        //设置中心文字
        //mChart.setCenterText(getString(R.string.centerText));
        //可以理解为设置中心文字的区域宽度，如果文字超过宽度会进行换行
        // 实际计算为内径的按照百分比缩小后作为中心文字区域的宽度
        mChart.setCenterTextRadiusPercent(50f);
        //可以理解为设置内径
        mChart.setHoleRadius(10f);
        //设置半透明圈的半径
        mChart.setTransparentCircleRadius(10f);
        //设置半透明圈的颜色
        mChart.setTransparentCircleColor(getResources().getColor(R.color.colorPrimary));
        //设置半透明圈的透明度[0~255]
        mChart.setTransparentCircleAlpha(99);
        //设置饼图的最大角度：默认为360°
        mChart.setMaxAngle(120f);






