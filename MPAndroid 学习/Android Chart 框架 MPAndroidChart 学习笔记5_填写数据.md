#Android Chart框架 MPAndroidChart学习笔记5_填写数据
---
## 1.基本数据的设置 ##
### 1.1 Entry类 ###
	public Entry(float x, float y) { ... }
**可以理解为一个Enery就是坐标轴上的一个数据点**
### 1.2 LineDataSet类 ###
	public LineDataSet(List<Entry> entries, String label) { ... }
**既然是Entry数据点组成的集合，可以理解为表示数据的线**
### 1.3 LineData ###
	/** List constructor */
    public LineData(List<ILineDataSet> sets) { ... }
**可以理解为描述chart数据的类**
### 1.3 装载数据 ###
	public void setData(ChartData data) { ... }
**调用chart的setData()方法来装载数据**

## 2.装载数据示例 ##
### 2.1 使用Entry构成一条数据线 ###
	List<Entry> valsComp1 = new ArrayList<Entry>();
    List<Entry> valsComp2 = new ArrayList<Entry>();

	Entry c1e1 = new Entry(0f, 100000f);
    valsComp1.add(c1e1);
    Entry c1e2 = new Entry(1f, 140000f);
    valsComp1.add(c1e2);
    ...

    Entry c2e1 = new Entry(0f, 130000f);
    valsComp2.add(c2e1);
    Entry c2e2 = new Entry(1f, 115000f); 
    valsComp2.add(c2e2);
    ...
### 2.2 设置DataSet ###
	LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
    setComp1.setAxisDependency(AxisDependency.LEFT);
    LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
    setComp2.setAxisDependency(AxisDependency.LEFT);
>通过setAxisDependency()来设置描述数据的坐标轴是使用左边Y轴还是右边Y轴

### 2.3 将数据装载到chart ###
	// use the interface ILineDataSet
    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    dataSets.add(setComp1);
    dataSets.add(setComp2);

    LineData data = new LineData(dataSets);
    mLineChart.setData(data);
    mLineChart.invalidate(); // refresh

## 3 在X轴添加坐标描述 ##
> 正常情况下X轴的标签描述就是数字，但是很多情况我们都需要去自定义修改，我们可以使用IAxisValueFormatter接口来自定义X轴，下面是一个例子：

	final String[] quarters = new String[] { "Q1", "Q2", "Q3", "Q4" };
	
	IAxisValueFormatter formatter = new IAxisValueFormatter() {

    	@Override
    	public String getFormattedValue(float value, AxisBase axis) {
        	return quarters[(int) value];
    }

    	// we don't draw numbers, so no decimal digits needed
    	@Override
    	public int getDecimalDigits() {  return 0; }
	};

	XAxis xAxis = mLineChart.getXAxis();
	xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
	xAxis.setValueFormatter(formatter);

## 4 对数据点Entries进行排序 ##

	List<Entry> entries = ...;
	Collections.sort(entries, new EntryXComparator());