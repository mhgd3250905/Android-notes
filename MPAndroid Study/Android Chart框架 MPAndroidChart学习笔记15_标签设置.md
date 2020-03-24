# Android Chart框架 MPAndroidChart学习笔记15_标签设置
---
[点击这里查看项目源码](https://github.com/mhgd3250905/Android-notes)

## 1.自定义标记
在MPAndroid中提供了两种方法：   

- 实现```IMaker```接口类
- 继承```MarkerView```实现MakerView类

这里我们选择第二种方式，代码如下：

### 1.1 继承```MarkerView```实现MakerView类


	public class MyMarkView extends MarkerView{
	    private TextView tvContent;
	    /**
	     * Constructor. Sets up the MarkerView with a custom layout resource.
	     *
	     * @param context
	     * @param layoutResource the layout resource to use for the MarkerView
	     */
	    public MyMarkView(Context context, int layoutResource) {
	        super(context, layoutResource);
	        tvContent= (TextView) findViewById(R.id.tv_content);
	
	    }
	
	    @Override
	    public void refreshContent(Entry e, Highlight highlight) {
	        tvContent.setTextColor(getResources().getColor(R.color.colorAccent));
	        tvContent.setText("您选择的是"+e.getY());
	
	        super.refreshContent(e, highlight);
	    }
	
	    private MPPointF mOffset;
	
	    @Override
	    public MPPointF getOffset() {
		        if(mOffset==null){
		            mOffset=new MPPointF(-(getWidth()/2),-getHeight());
	        }
	        return mOffset;
    	}
	}

### 1.2 实例化并引用

	IMarker marker= new MyMarkView(getContext(),R.layout.makerview);
    mChart.setMarker(marker);

**当然我们需要一个layout**

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent">
	    <TextView
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:id="@id/tv_content"/>
	</LinearLayout>


### 1.3分析方法 ###

在1.1的继承类方法：

构造函数：在这里实例化对象
  
	public MyMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent= (TextView) findViewById(R.id.tv_content);
    }

刷新内容：这里设置标记显示的内容

	@Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setTextColor(getResources().getColor(R.color.colorAccent));
        tvContent.setText("您选择的是"+e.getY());

        super.refreshContent(e, highlight);
    }

设置标签出现位置
> getWidth()、getHeight()表示标签的宽度、高度

	private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {
        if(mOffset==null){
            mOffset=new MPPointF(-(getWidth()/2),-getHeight());
        }
        return mOffset;
    }
所以这里的显示效果如下：

![new MPPointF(-(getWidth()/2),-getHeight())](http://ww1.sinaimg.cn/large/006aPzcjgy1fd7qyi6w7wj30ai0ccaaf)

如果我们尝试设置为   

	mOffset=new MPPointF(0,0);

那么便会出现下面的样式
	
![new MPPointF(0,0)](http://ww1.sinaimg.cn/large/006aPzcjgy1fd7qyozqzfj30ad094jrn)

怎么样，察觉到不一样了吗？
	








