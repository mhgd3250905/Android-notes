#Android Chart框架 MPAndroidChart学习笔记4_XY轴基本设置
---
## 1.X轴的基本设置 ##

	    /* @描述 X轴基本设置 */
    private void CustomizingXAxis(XAxis mAxis){
        //X轴标签的倾斜角度
        mAxis.setLabelRotationAngle(0f);
        //设置X轴标签出现位置
        //TOP、BOTTOM、BOTH_SIDED、TOP_INSIDE、BOTTOM_INSIDE
        mAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置标签文本格式
        mAxis.setTextSize(10f);
        //设置标签文本颜色
        mAxis.setTextColor(Color.RED);
        //是否绘制轴线
        mAxis.setDrawAxisLine(true);
        //是否绘制网格线
        mAxis.setDrawGridLines(false);
        //自定义数值格式
        //mAxis.setValueFormatter(new MyCustomFormatter());
    }

## 2.Y轴基本设置 ##

**Y轴的获取方式**：

	YAxis leftAxis = chart.getAxisLeft();
	YAxis rightAxis = chart.getAxisRight();
	YAxis leftAxis = chart.getAxis(AxisDependency.LEFT);
	//获取两个Y轴
	YAxis yAxis = radarChart.getYAxis();

>默认没有特别指定情况下，右Y轴会与左Y轴描述一样的内容比例
>如果需要达到特定的描述，如下操作：

	//定义第一个dataSet
	LineDataSet dataSet = new LineDataSet(entries, "Test");
	//将第一个dataSet的数据用左Y轴来描述
	dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
	//定义第二个dataSet
	LineDataSet dataSet2 = new LineDataSet(entries2, "Test2"); 
	//将第二个dataSet的数据用左Y轴来描述
	dataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);
	//一系列设置之后
	...
	//加入到chart中
	LineData lineData = new LineData(dataSet,dataSet2);
    mChart.setData(lineData);

**补充一些Y轴的绘制方法：**

	/* @描述 Y轴基本设置 */
    private void CustomizingYAxis(YAxis mAxis){
        //是否启用绘制零线:设置为true后才有后面的操作
        mAxis.setDrawZeroLine(true);
        //设置绘制零线宽度
        mAxis.setZeroLineWidth(5f);
        //绘制零线颜色
        mAxis.setZeroLineColor(Color.BLUE);
    }

**附送几个官方文档example：**
	
	Example1：	
	// data has AxisDependency.LEFT
	YAxis left = mChart.getAxisLeft();
	left.setDrawLabels(false); // no axis labels
	left.setDrawAxisLine(false); // no axis line
	left.setDrawGridLines(false); // no grid lines
	left.setDrawZeroLine(true); // draw a zero line
	mChart.getAxisRight().setEnabled(false); // no right axis
	
	Example2：
	YAxis yAxis = mChart.getAxisLeft();
	yAxis.setTypeface(...); // set a different font
	yAxis.setTextSize(12f); // set the text size
	yAxis.setAxisMinimum(0f); // start at zero
	yAxis.setAxisMaximum(100f); // the axis maximum is 100
	yAxis.setTextColor(Color.BLACK);
	yAxis.setValueFormatter(new MyValueFormatter());
	yAxis.setGranularity(1f); // interval 1
	yAxis.setLabelCount(6, true); // force 6 labels
	//... and more




