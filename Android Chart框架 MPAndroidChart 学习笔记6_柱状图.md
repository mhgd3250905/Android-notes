# Android Chart框架 MPAndroidChart学习笔记6_柱状图
---
## 1.xml布局 ##
	<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    tools:context="com.immoc.ww.greendaodemo.fragment.LineChartFragment">
	
	    <com.github.mikephil.charting.charts.BarChart
	        android:id="@id/chart_bar"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent" />
	</FrameLayout>

## 2.java ##
**绑定**

	@Bind(R.id.chart_bar)
	BarChart mChart;
**设置数据**
> 这里注意需要使用bar专用数据

		List<BarEntry> entries = new ArrayList<BarEntry>();
        List<BarEntry> entries2 = new ArrayList<BarEntry>();

        for (int i = 0; i < 20; i++) {
            // turn your data into Entry objects
            entries.add(new BarEntry(i, 20 * i));
            entries2.add(new BarEntry(i,10 * i));
        }
        
        //创建数据集
        BarDataSet dataSet = new BarDataSet(entries, "BarDataSet1"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarDataSet dataSet2 = new BarDataSet(entries2, "BarDataSet2"); // add entries to dataset
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //设置数据显示颜色：柱子颜色
        dataSet2.setColor(Color.RED);
        dataSet2.setBarBorderColor(Color.BLUE);
        
        List<IBarDataSet> dataSets=new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);
        //柱状图数据集
        BarData data = new BarData(dataSets);
        //设置柱子宽度
        data.setBarWidth(0.9f);
        mChart.setData(data);//装载数据
        mChart.setFitBars(true); //X轴自适应所有柱形图
        mChart.invalidate();//刷新

![image](https://camo.githubusercontent.com/eb283164a410a68bc8102a1ade623ec759f2b7f2/68747470733a2f2f7261772e6769746875622e636f6d2f5068696c4a61792f4d5043686172742f6d61737465722f73637265656e73686f74732f6e6f726d616c5f62617263686172745f77696b692e706e67)

	