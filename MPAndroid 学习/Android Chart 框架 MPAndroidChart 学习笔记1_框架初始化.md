### Android Chart 框架 MPAndroidChart 学习笔记1_框架初始化 ###
## 1.安装 ##
### 1.1 添加依赖 ###
>工程级别的build.gradle中添加:

	allprojects {
	    repositories {
	        maven { url "https://jitpack.io" }
	    }
	}
>app下的build.gradle中添加:

	dependencies {
    	compile 'com.github.PhilJay:MPAndroidChart:v3.0.1'
	}

## 2.使用 ##

### 2.1 在XML中加入chart ###

	   <com.github.mikephil.charting.charts.LineChart
	        android:id="@+id/chart"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent" />
### 2.2 在java中初始化 ###

	LineChart chart = (LineChart) findViewById(R.id.chart);
	或者:
	// programmatically create a LineChart
    LineChart chart = new LineChart(Context);

    // get a layout defined in xml
    RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout);
    rl.add(chart); // add the programmatically created chart

### 2.3 添加数据 ###

>//初始化Entry列表

	List<Entry> entries = new ArrayList<Entry>();
	
>new Entry(x,y) 这里xy就对应xy轴的数值

	//向Entry中加入数据
    for (int i = 0; i < 20; i++) {
            // turn your data into Entry objects
            entries.add(new Entry(i, 2*i));
    }

>绑定dataSet
  
	LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

>设置表格UI
>
    dataSet.setColor(R.color.colorAccent);
    dataSet.setValueTextColor(R.color.colorPrimary); // styling, ...

>设置表格形式（lineData line表格）

    LineData lineData = new LineData(dataSet);
    mChart.setData(lineData);

>刷新表格
>
    mChart.invalidate();






	

