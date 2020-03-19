# Android Chart框架 MPAndroidChart学习笔记13_动态实时数据
---
## 1.添加数据 ##
我们有很多种方法对一个chart增加数据：

对于```DataSet```(以及它的所有子类):

- ```addEntry(Entry e)```:向DataSet中加入一个数据

对于```ChartData```（以及它的所有子类）：

- ```addEntry(Entry e,int dataSetIndex)```：给指定序列的dataSet添加数据
- ```addDataSet(DataSet d)```:向chart中添加一个dataSet

## 2.删除数据 ##
当然我们也有很多方法来删除数据：

对于```DataSet```(以及它的所有子类)：

- ```public boolean removeFirst()```:从条目数组中移除此数据集的第一个条目（索引0）。如果成功返回true，如果没有返回false
- ```public boolean removeLast()```：移除条目数组最后一个条目
- ```public boolean removeEntry(Entry e)```:移除指定的Entry
- ```public boolean removeEntry(int xIndex)```:删除指定序列的Entry

对于```ChartData```（以及它的所有子类）:

- ```public boolean removeEntry(Entry e, int dataSetIndex)```:删除指定序列的的DataSet的指定的Entry，成功返回true，失败返回false
- ```public boolean removeEntry(int xIndex, int dataSetIndex)```：删除指定序列的DataSet的指定序列的Entry，成功返回true，失败返回false
- ```public boolean removeDataSet(DataSet d)```:删除指定的DataSet，成功返回true，失败返回false
- ```public boolean removeDataSet(int index)```:删除指定序列的DataSet，成功返回true，失败返回发了

## 3.刷新数据 ##

在每次动态增加数据之后必须调用```notifyDataSetChanged()```，然后调用```invalidate()```进行刷新

		// add entries to the "data" object
		exampleData.addEntry(...);
		chart.notifyDataSetChanged(); // let the chart know it's data changed
		chart.invalidate(); // refresh
		
		// EXAMPLE 2
		// add entries to "dataSet" object
		dataSet.addEntry(...);
		exampleData.notifyDataChanged(); // let the data know a dataSet changed
		chart.notifyDataSetChanged(); // let the chart know it's data changed
		chart.invalidate(); // refresh
