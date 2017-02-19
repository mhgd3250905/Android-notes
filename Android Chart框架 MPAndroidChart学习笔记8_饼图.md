# Android Chart框架 MPAndroidChart学习笔记8_饼图
---
## 1.设置数据 ##
		List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(26.7f, "Yellow"));
        entries.add(new PieEntry(24.0f, "Red"));
        entries.add(new PieEntry(30.8f, "Blue"));
        entries.add((new PieEntry(18.5f, "Green")));

        PieDataSet set = new PieDataSet(entries, "Election Results");

## 2.设置颜色 ##

	set.setColors(new int[]{Color.YELLOW,Color.RED,Color.BLUE,Color.GREEN});

## 3.装载数据 ##

		PieData data = new PieData(set);
        mChart.setData(data);
        mChart.invalidate(); 

显示如下：

![image](https://camo.githubusercontent.com/8dac84ecf64fa0a21d938cf030f021e30e0210a5/68747470733a2f2f7261772e6769746875622e636f6d2f5068696c4a61792f4d5043686172742f6d61737465722f73637265656e73686f74732f70696563686172745f77696b692e706e67)