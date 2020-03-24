# 解析Android的LayoutInflate布局

### 1. LayoutInflate的三个参数

**```inflate(int resource, ViewGroup root, boolean attachToRoot);```**

- ```inflate(layoutId, null);```
- ```inflate(layoutId, root, false);```
- ```inflate(layoutId, root, true);```

### 2. 源码解析

```java
public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) { 
    synchronized (mConstructorArgs) { 
      final AttributeSet attrs = Xml.asAttributeSet(parser); 
      Context lastContext = (Context)mConstructorArgs[0]; 
      mConstructorArgs[0] = mContext; 
      //这个函数的返回值就是result
      //如果设置root，那么最终返回的也就是root了
      //如果root不为空，而xml解析出来的View作为子控件在root中
      View result = root; 
 
      try { 
        // Look for the root node. 
        int type; 
        while ((type = parser.next()) != XmlPullParser.START_TAG && 
            type != XmlPullParser.END_DOCUMENT) { 
          // Empty 
        } 
 
        if (type != XmlPullParser.START_TAG) { 
          throw new InflateException(parser.getPositionDescription() 
              + ": No start tag found!"); 
        } 
 
        final String name = parser.getName(); 
         
        if (DEBUG) { 
          System.out.println("**************************"); 
          System.out.println("Creating root view: " 
              + name); 
          System.out.println("**************************"); 
        } 
 
        if (TAG_MERGE.equals(name)) { 
          //这里可以看到
          //当这个布局的根节点为<merge />的时候
          //如果root为空，或者attachToRoot为false的时候，抛出异常
          //很明显，以merge为根节点的时候，必须是作为子View存在的
          if (root == null || !attachToRoot) { 
            throw new InflateException("<merge /> can be used only with a valid " 
                + "ViewGroup root and attachToRoot=true"); 
          } 
 
          rInflate(parser, root, attrs, false); 
        } else { 
          // Temp is the root view that was found in the xml 
          View temp; 
          if (TAG_1995.equals(name)) { 
            temp = new BlinkLayout(mContext, attrs); 
          } else { 
            //通过createViewFromTag来创建View
            temp = createViewFromTag(root, name, attrs); 
          } 
 		
          //跟踪这个params，可以看到：
          //如果root为null的时候，那么这个params就会为null
          //而root不为null的时候，会从父View root获取到LayoutParams
          ViewGroup.LayoutParams params = null; 
 
          //这个方法可以看到：
          //如果root不为null，而且attachToRoot为false的时候
          //为temp设置LayoutParams
          if (root != null) { 
            if (DEBUG) { 
              System.out.println("Creating params from root: " + 
                  root); 
            } 
            // Create layout params that match root, if supplied 
            params = root.generateLayoutParams(attrs); 
            if (!attachToRoot) { 
              // Set the layout params for temp if we are not 
              // attaching. (If we are, we use addView, below) 
              temp.setLayoutParams(params); 
            } 
          } 
 
          if (DEBUG) { 
            System.out.println("-----> start inflating children"); 
          } 
          // Inflate all children under temp 
          rInflate(parser, temp, attrs, true); 
          if (DEBUG) { 
            System.out.println("-----> done inflating children"); 
          } 
 
          // We are supposed to attach all the views we found (int temp) 
          // to root. Do that now. 
          //而这里，如果root不为null，但是attachToRoot为true的时候，将view按照params添加到root中
          if (root != null && attachToRoot) { 
            root.addView(temp, params); 
          } 
 
          // Decide whether to return the root that was passed in or the 
          // top view found in xml. 
          //这里，如果root为null，或者attachToRoot为false的时候
           //那么将temp赋值给result
          if (root == null || !attachToRoot) { 
            result = temp; 
          } 
        } 
 
      } catch (XmlPullParserException e) { 
        InflateException ex = new InflateException(e.getMessage()); 
        ex.initCause(e); 
        throw ex; 
      } catch (IOException e) { 
        InflateException ex = new InflateException( 
            parser.getPositionDescription() 
            + ": " + e.getMessage()); 
        ex.initCause(e); 
        throw ex; 
      } finally { 
        // Don't retain static reference on context. 
        mConstructorArgs[0] = lastContext; 
        mConstructorArgs[1] = null; 
      } 
 
      return result; 
    } 
  } 


```

首先声明了：

```java
View result=root;//最终返回值为result
```

然后执行：

```java
temp = createViewFromTag(root, name, attrs);
```

然后进行判断：

```java
if(root!=null) { 
	params = root.generateLayoutParams(attrs); 
	if (!attachToRoot) {
    	temp.setLayoutParams(params); 
 	} 
} 
```

可以看到，如果root不为null，而且attachToRoot为false的时候，为temp设置了LayoutParams；

接下来判断：

```java
if (root != null && attachToRoot) { 
	root.addView(temp, params); 
} 
```

如果root不为空，而且attachToRoot为true的时候，将temp按照LayoutParams添加到root中；

而且：

```Java
if (root == null || !attachToRoot) {  
	result = temp;  
} 
```

如果root为null，而attachToRoot为false的时候，那么将temp赋值给result；

从上面的分析已经可以看出：

1. Inflate(resId, null)只创建temp，并且返回temp；
2. Inflate(resId, parent, false)创建temp，然后执行```temp.setLayoutParams(params);```，返回temp；
3. Inflate(resIdent, parent, true)创建temp，然后执行```root.addView(temp,params);```最后返回root；

由上面就能解释：

1. Inflate(resId, null)不能正确处理宽高是因为：```layout_width```和```layout_height```是相对父级设置的，必须与父级的LayoutParams一致。而这个方法中，temp是没有父View的，所以获取到的params为null，也就无法正确处理宽高了；
2. Inflate(resId, parent, true)不仅能够正确处理，而且已经把resId对应的View加入到了parent中，并且返回的是parent，和以上两者返回值有绝对区别；
3. 而在```ListView```中，如果对于Item绑定布局的时候使用```Inflate(resId, parent, true)```的时候会出现报错，为什么？是因为这个方法会返回root，而且会调用```root.addView(temp, params);```来讲temp添加到root中，但是ListView集成AdapterView，而在AdapterView中对于```addView```方法有如下代码：

```java
@Override 
public void addView(View child) { 
	throw new UnsupportedOperationException("addView(View) is not supported in AdapterView"); 
} 
```

以上就是这个错误产生的原因了；

### 3. Activity的```setContentView```方法

看下代码：

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    LayoutInflater mInflater = LayoutInflater.from(this);
    View view1 = mInflater.inflate(R.layout.activity_main, null);
    View view2 = mInflater.inflate(R.layout.activity_main, (ViewGroup) findViewById(android.R.id.content), false);
    View view3 = mInflater.inflate(R.layout.activity_main, (ViewGroup) findViewById(android.R.id.content), true);
    iv = findViewById(R.id.iv);
 
}
```

首先这里传入的parent参数使用的Activity的内容区域，也就是DecorView中的内容区域：即android.R.id.content，是一个FrameLayout，我们在setContentView的时候，其实系统会自动报上一层FrameLayout，按照上面的说法有如下推断：

1. view1的layoutParams为null；
2. view2的layoutParams不为null，且为FrameLayout.LayoutParams;
3. view3的为FrameLayout，而且将xml对应的布局添加到Activity的内容区域了；

### 4. 从ViewGroup和View的角度来解析

对于自定义ViewGroup和自定义View，一定会用到onMeasure方法：

ViewGroup的onMeasure方法所做的是，为childView设置测量模式和测试出来的值。

如何设置呢？根据LayoutParams。

1. 如果childView的宽为：LayoutParams.MATCH_PARENT，则设置模式为MeasureSpec.EXACTLY，且为childView的计算宽度；
2. 如果childView的宽为：固定值（即大于0），则设置模式为MeasureSpec.EXACTLY，且将lp.width直接作为childView的宽度；
3. 如果childView的宽为：LayoutParams.WRAP_CONTENT，则设置模式为MeasureSpec.AT_MOST；

高度与宽度类似。

View和onMeasure方法：

主要做的就是根据ViewGroup传入的测量模式和测量值，计算自己应该的高和宽：

一般是这样的流程：

1. 如果宽的模式为AT_MOST：则自己计算宽的值。
2. 如果宽的模式为EXACTLY：则直接使用MeasureSpec.getSize(widthMeasureSpec);

如果View的宽和高设置为准确值，则一定依赖于LayoutParams，所以我们的Inflate(resId, null)才能正确处理宽和高；

