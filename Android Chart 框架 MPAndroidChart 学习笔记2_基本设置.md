#MPAndroidChart 学习笔记
---
![](http://i.imgur.com/4R9QlWc.png)
## 1.启用禁用交互 ##

	/* @描述 启用、禁用交互 */
    private void interactionWithChart(LineChart chart) {
        chart.setTouchEnabled(true);//是否开启触摸相关的交互方式
        chart.setDragEnabled(true);//是否开启拖拽相关的交互方式
        chart.setScaleEnabled(true);//是否开启xy轴的缩放
        chart.setScaleXEnabled(true);//是否开启x轴的缩放
        chart.setScaleYEnabled(true);//是否开启y轴的缩放
        //是否开启双指捏合缩放:如果关闭了，仍然可以完成x或y一个轴的缩放
        chart.setPinchZoom(true);
    }

## 2.图表的图幅以及摩擦系数 ##
	/* @描述 图表的图幅以及摩擦系数 */
    private void flingAndSeceleration(LineChart chart) {
        //如果设置为true，图表继续滚动后润色，达到一种滚动平滑的效果
        //默认值：true
        chart.setDragDecelerationEnabled(true);
        //设置摩擦系数[0:1]:float
        // 0表示摩擦最大，基本上一滑就停
        // 1表示没有摩擦，会自动转化为0.9999,及其顺滑
        chart.setDragDecelerationFrictionCoef(0.5f);
    }

## 3.手势回调 ##
**调用接口**：

	public interface OnChartGestureListener {
		/* @描述 触摸开始（ACTION_DOWN） */
	    void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture);
	
	    /* @描述 触摸结束 */
	    void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture);
	
	    /* @描述 长按 */
	    public void onChartLongPressed(MotionEvent me);
	
	    /* @描述 双击 */
	    public void onChartDoubleTapped(MotionEvent me);
	
	    /* @描述 单击 */
	    public void onChartSingleTapped(MotionEvent me);
	
	    /* @描述 图幅描述 */
	    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY);
	
		/* @描述 缩放 */
	    public void onChartScale(MotionEvent me, float scaleX, float scaleY);
	
		/* @描述 手势移动 */
	    public void onChartTranslate(MotionEvent me, float dX, float dY);
	}

**调用接口实现抽象方法，然后记得在java中进行绑定**：

	mChart.setOnChartGestureListener(this);

## 4.突出高亮 ##

	/* @描述 突出高亮 */
    private void highlighting(LineChart chart){
        //默认为true:保证在拖动是图像被充分放大了
        chart.setHighlightPerDragEnabled(true);
        //默认为true
        // 设置为true之后可以通过点击的方式高亮选择数据点
        //设置为false之后无法通过点击方式选择，但仍然可以通过拖拽实现
        chart.setHighlightPerTapEnabled(true);
        //高亮选择只可以在距离高亮点指定范围内
        //当选择点与高亮点距离超过设置值时高亮消失
		//设置之后高亮拖拽相对不好用
        chart.setMaxHighlightDistance(10f);
    }

**针对数据集**：

		//数据内容是否可以高亮选择
        dataSet.setHighlightEnabled(false);
        //是否显示高亮提示线
        dataSet.setDrawHighlightIndicators(true);
        //设置高亮提示先颜色
        dataSet.setHighLightColor(Color.RED);


