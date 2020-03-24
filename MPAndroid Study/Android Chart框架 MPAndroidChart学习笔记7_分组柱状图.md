# Android Chart框架 MPAndroidChart学习笔记7_分组柱状图
---
## 1.设置数据 ##

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

## 2.设置组 ##

**通过调用groupBars(fromX,groupSpace,barSpace)方法来设置分组柱状图**

- fromX表示从X轴开始的坐标
- groupSpace表示组之间的距离
- barSpace表示组内柱子之间的距离

		float groupSpace=1f;
        float barSpace=0.2f;
        float barWidth=0.45f;


        //柱状图数据集
        BarData data = new BarData(dataSet,dataSet2);
        //设置柱子宽度
        data.setBarWidth(barWidth);
        mChart.setData(data);//装载数据
        mChart.groupBars(0f,groupSpace,barSpace);
        mChart.invalidate();//刷新
	
![image](https://camo.githubusercontent.com/8b2f6996b19725410e02c85f16b93ed94478bdd9/68747470733a2f2f7261772e6769746875622e636f6d2f5068696c4a61792f4d5043686172742f6d61737465722f73637265656e73686f74732f67726f757065645f62617263686172745f77696b692e706e67)

## 3.保证标签在小组中间 ##
	XAxis xAxis = chart.getXAxis();
	xAxis.setCenterAxisLabels(true);
