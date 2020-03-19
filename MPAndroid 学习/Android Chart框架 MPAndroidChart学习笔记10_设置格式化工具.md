# Android Chart框架 MPAndroidChart学习笔记10_设置格式化工具
---
## 1.创建一个格式化工具 ##
> 使用IValueFormatter接口并实现```getFormattedValue()```来获取我们想要的标签显示

	public class MyValueFormatter implements IValueFormatter {
	
	    private DecimalFormat mFormat;
	
	    public MyValueFormatter() {
	        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
	    }
	
	    @Override
	    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
	        // write your logic here
	        return mFormat.format(value) + " $"; // e.g. append a dollar-sign
	    }
	}

## 2.设置格式工具 ##
	// usage on whole data object
	lineData.setValueFormatter(new MyValueFormatter());

	// usage on individual dataset object
	lineDataSet.setValueFormatter(new MyValueFormatter());

## 3.预定格式化程序 ##
- **LargeValueFormatter**：将比较大的数值进行转化，比如1000转化为1k，1000000转化为1m（million），1000000000转化为1b（billion），以及1兆转化为1t
- **PercentFormatter**：将数值转化为百分比，比如50→50%
- **StackedValueFormatter**：专门设计用来与堆积条形图。它允许指定是否应绘制所有堆栈值或仅为顶层值。

	
	