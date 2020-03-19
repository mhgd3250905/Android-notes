# Android Chart框架 MPAndroidChart学习笔记9_设置颜色
---
## 1.设置颜色 ##
		LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
	  	// sets colors for the dataset, resolution of the resource name to a "real" color is done internally
	  	setComp1.setColors(new int[] { R.color.red1, R.color.red2, R.color.red3, R.color.red4 }, Context);
	
	  	LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");
	  	setComp2.setColors(new int[] { R.color.green1, R.color.green2, R.color.green3, R.color.green4 }, Context);

## 2.设置颜色方法 ##

- **setColors(int[] colors,Context c)**:设置多个颜色，一旦数据集的条目多于颜色总数，颜色就会被重复使用，颜色的提供采用的是**getResources().getColor(...)**方法

- **setColors(int [] colors)**

- **setColors(ArrayList<Integer> colors)**

- **setColor(int color)**：使用单一的颜色

## 3.设置默认颜色 ##

	LineDataSet set = new LineDataSet(...);
	set.setColors(ColorTemplate.VORDIPLOM_COLORS);

