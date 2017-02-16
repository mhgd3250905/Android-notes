#Android Chart框架 MPAndroidChart学习笔记3_坐标轴设置
---
## 1.轴线的绘制 ##
设置轴线就先必须取得轴线类Axis
在一个图标中有三个轴线：

- **x轴**：调用.getXAxis()获取
- **左边y轴**：调用.getAxisLeft()获取
- **右边y轴**：调用.getAxisRight()获取

其对应轴线之设计方法如下：

	/* @描述 轴线的绘制 */
    private void drawAxis(AxisBase mAxis){
        //设置是否启用轴线：如果关闭那么就默认没有轴线/标签/网格线
        mAxis.setEnabled(true);
        //设置是否开启绘制轴的标签
        mAxis.setDrawLabels(true);
        //是否绘制轴线
        mAxis.setDrawAxisLine(true);
        //是否绘制网格线
        mAxis.setDrawGridLines(true);
    }

## 2.自定义轴的范围 ##

>对于xy轴多数方法是针对y轴

	/* @描述 自定义轴的范围 */
    private void customizeRange(YAxis mAxis){
        //设置坐标轴最大值：如果设置那么轴不会根据传入数据自动设置
        mAxis.setAxisMaximum(10f);
        //重置已经设置的最大值，自动匹配最大值
        mAxis.resetAxisMaximum();
        //设置坐标轴最小值：如果设置那么轴不会根据传入数据自动设置
        mAxis.setAxisMinimum(5f);
        //重置已经设置的最小值，自动匹配最小值
        mAxis.resetAxisMinimum();
        //将图表中最高值的顶部间距（占总轴范围的百分比）与轴上的最高值相比较。
        mAxis.setSpaceMax(10);
        //将图表中最低值的底部间距（占总轴范围的百分比）与轴上的最低值相比较。
        mAxis.setSpaceMin(10);
        //设置标签个数以及是否精确（false为模糊，true为精确）
        mAxis.setLabelCount(20,false);
        //如果设置为true，此轴将被反转，这意味着最高值将在底部，最低的顶部值。
        mAxis.setInverted(true);
        //设置轴标签应绘制的位置。无论是inside_chart或outside_chart。
        mAxis.setPosition(OUTSIDE_CHART);
        //如果设置为true那么下面方法设置最小间隔生效，默认为false
        mAxis.setGranularityEnabled(true);
        //设置Y轴的值之间的最小间隔。这可以用来避免价值复制当放大到一个地步，小数设置轴不再数允许区分两轴线之间的值。
        mAxis.setGranularity(10f);
    }

## 3.调整轴的造型 ##

	/* @描述 调整轴的造型 */
    private void modifyingAxis(AxisBase mAxis){
        //设置坐标轴标签文字颜色
        mAxis.setTextColor(Color.GREEN);
        //设置坐标轴标签文字大小
        mAxis.setTextSize(10f);
        //设置坐标轴标签文字样式
        mAxis.setTypeface(Typeface.DEFAULT_BOLD);
        //设置此轴网格线颜色
        mAxis.setGridColor(Color.RED);
        //设置此轴网格线宽度
        mAxis.setGridLineWidth(0.5f);
        //设置坐标轴的颜色
        mAxis.setAxisLineColor(Color.RED);
        //设置坐标轴的宽度
        mAxis.setAxisLineWidth(1f);
        //使用虚线组成的网格线
        //参数：linelength：虚线长度
        // spacelength:虚线间隔长度
        // phase：虚线出发点（从第一根虚线的哪里出发）
        mAxis.enableGridDashedLine(40f,2f,20f);
    }

## 4.设置一个限制的辅助线 ##

- 在坐标轴上添加一个与**指定坐标轴垂直**的辅助线
- 
	//设置一个限制的线
    private void addLimitLine(AxisBase mAxis){
        LimitLine ll = new LimitLine(10f, "Critical Blood Pressure");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(4f);
        ll.setTextColor(Color.BLACK);
        ll.setTextSize(12f);
        mAxis.addLimitLine(ll);
    }

