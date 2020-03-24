# Android XML布局与View之间的转换

> 记录一下Android是如何解析XML并生成View的过程

首先创建一个项目，一个基本的Activity，xml布局也非常简单，一个RelativeLayout套了一个ImageView，如下：

```java
public class MainActivity extends Activity {  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
    }  
 }  
```

其中关键之处在于调用了父类Activity的setContentView方法：

```java
@Override  
public void setContentView(int layoutResID) {  
    if (mContentParent == null) {  
        installDecor();  
    } else {  
        mContentParent.removeAllViews();  
    }  
    mLayoutInflater.inflate(layoutResID, mContentParent);  
    final Callback cb = getCallback();  
    if (cb != null) {  
        cb.onContentChanged();  
    }  
}
```

每个Activity实际都对应一个PhoneWindow，拥有一个顶层的DecorView，DecorView集成子FrameLayout，作为根View，其中包含了一个标题区和内容区域，这里mContentParent就是其内容区域。这段代码的意思很简单，如果DecorView的内容区域为null，那么就先初始化，否则就先把内容区域的子View全部移除，然后再引入layout布局，所欲关键还是在于```mLayoutInflater.inflate(layoutResID, mContentParent);```，代码继续往下看：

```java
public View inflate(int resource, ViewGroup root) {  
    return inflate(resource, root, root != null);  
} 

public View inflate(int resource, ViewGroup root, boolean attachToRoot) {  
    if (DEBUG) System.out.println("INFLATING from resource: " + resource);  
    XmlResourceParser parser = getContext().getResources().getLayout(resource);  
    try {  
        return inflate(parser, root, attachToRoot);  
    } finally {  
        parser.close();  
    }  
} 
```

这里首先根据layout布局文件的Id生成xml资源解析器，然后调用```inflate(parser,root,attachToRoot)```生成具体的View，XmlResourceParser是集成子XmlPullParser和AttributeSet的接口，这里的parser其实是XmlBlock的内部类Parser的实例。

```java
public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {  
    synchronized (mConstructorArgs) {  
        final AttributeSet attrs = Xml.asAttributeSet(parser);  
        Context lastContext = (Context)mConstructorArgs[0];  
        mConstructorArgs[0] = mContext;  
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

            /**
             * 获取xml根节点名称
             */
            final String name = parser.getName();
              
            if (DEBUG) {  
                System.out.println("**************************");  
                System.out.println("Creating root view: "  
                        + name);  
                System.out.println("**************************");  
            }  

            if (TAG_MERGE.equals(name)) {  
                if (root == null || !attachToRoot) {  
                    throw new InflateException("<merge /> can be used only with a valid "  
                            + "ViewGroup root and attachToRoot=true");  
                }  

                rInflate(parser, root, attrs);  
                } else {  
                // Temp is the root view that was found in the xml  
                /**
                 * 根据根节点名称创建临时View，这个临时View也就是xml布局的根View
                 */
                View temp = createViewFromTag(name, attrs);  

                ViewGroup.LayoutParams params = null;  

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
                /**
                 * 在临时View的节点下创建所有的子View，显然这个方法是通过遍历xml所有的子view节点，调用createViewFromTag方法来生成子View并加载到根View中
                 */
                rInflate(parser, temp, attrs);  
                if (DEBUG) {  
                    System.out.println("-----> done inflating children");  
                }  

                // We are supposed to attach all the views we found (int temp)  
                // to root. Do that now.  
                if (root != null && attachToRoot) {  
                    root.addView(temp, params);  
                }  

                // Decide whether to return the root that was passed in or the  
                // top view found in xml.  
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

首先获取xml根节点：

```java
final String name = parser.getName(); 
```

然后根据节点创建临时View(temp)，这个临时View(temp)也是xml布局的根View:

```java
View temp = createViewFromTag(name, attrs);  
```

然后在临时View(temp)的节点下创建所有子View，显然这个方法是通过遍历xml所有的子View节点，调用createViewFromTag方法生成子view并加载到根view中：

```java
rInflate(parser, temp, attrs);  
```

紧接着判断，是否有当前是否有父view，如果有那么就把临时view(temp)加载到父view中再返回，如果没有就直接返回临时view(temp)，我们这里调用inflate方法的时候显然有父view，也就是mContentParent，也就是最顶层view：DecorView的内容区域。

```java
                // We are supposed to attach all the views we found (int temp)  
                // to root. Do that now.  
                if (root != null && attachToRoot) {  
                    root.addView(temp, params);  
                }  

                // Decide whether to return the root that was passed in or the  
                // top view found in xml.  
                if (root == null || !attachToRoot) {  
                    result = temp;  
                }  
```

这里最关键有两个方法，一个是```createViewFromTag```，另一个是rInflate，现在来逐一分析：```createViewFromTag```实际最终调用的是```createView```方法：

```java
public final View createView(String name, String prefix, AttributeSet attrs)  
        throws ClassNotFoundException, InflateException {  
    //从map中查询构造函数是否存在，如果存在则直接根据构造函数创建实例
    //这样的好处是不用每次都通过class去获取构造函数再创建实例
    Constructor constructor = sConstructorMap.get(name);  
    Class clazz = null;  

    try {  
        //如果构造函数为null，那么久通过name获取到class
        //并且通过class来获取到构造函数，并且保存到map里
        if (constructor == null) {  
            // Class not found in the cache, see if it's real, and try to add it  
            clazz = mContext.getClassLoader().loadClass(  
                    prefix != null ? (prefix + name) : name);  
              
            if (mFilter != null && clazz != null) {  
                boolean allowed = mFilter.onLoadClass(clazz);  
                if (!allowed) {  
                    failNotAllowed(name, prefix, attrs);  
                }  
            }  
            constructor = clazz.getConstructor(mConstructorSignature);  
            sConstructorMap.put(name, constructor);  
        } else {  
            // If we have a filter, apply it to cached constructor  
            if (mFilter != null) {  
                // Have we seen this name before?  
                Boolean allowedState = mFilterMap.get(name);  
                if (allowedState == null) {  
                    // New class -- remember whether it is allowed  
                    clazz = mContext.getClassLoader().loadClass(  
                            prefix != null ? (prefix + name) : name);  
                      
                    boolean allowed = clazz != null && mFilter.onLoadClass(clazz);  
                    mFilterMap.put(name, allowed);  
                    if (!allowed) {  
                        failNotAllowed(name, prefix, attrs);  
                    }  
                } else if (allowedState.equals(Boolean.FALSE)) {  
                    failNotAllowed(name, prefix, attrs);  
                }  
            }  
        }  

        Object[] args = mConstructorArgs;  
        args[1] = attrs;  
        return (View) constructor.newInstance(args);  

    } catch (NoSuchMethodException e) {  
        InflateException ie = new InflateException(attrs.getPositionDescription()  
                + ": Error inflating class "  
                + (prefix != null ? (prefix + name) : name));  
        ie.initCause(e);  
        throw ie;  

    } catch (ClassNotFoundException e) {  
        // If loadClass fails, we should propagate the exception.  
        throw e;  
    } catch (Exception e) {  
        InflateException ie = new InflateException(attrs.getPositionDescription()  
                + ": Error inflating class "  
                + (clazz == null ? "<unknown>" : clazz.getName()));  
        ie.initCause(e);  
        throw ie;  
    }  
}  
```

这个方法很简单，就是通过xml节点名，通过反射获取view的实例再返回，其中先去map中查询构造函数是否存在，如果不存在那么通过使用```Context.getClassLoader().loadClass(ClassName)```来获取到对应的class对象，然后通过反射获取到构造方法：

```java
// Class not found in the cache, see if it's real, and try to add it  
clazz = mContext.getClassLoader().loadClass(prefix != null ? (prefix + name) : name);  

...
    
constructor = clazz.getConstructor(mConstructorSignature);  
sConstructorMap.put(name, constructor);   
```

其中```mConstructionSignature```定义如下：

```java
private static final Class[] mConstructorSignature = new Class[] {Context.class, AttributeSet.class};  
```

很显然，这里用的是带有Context和AttributeSet两个参数的构造方法，这也是为什么自动应以View一定要重载这个构造函数的原因。

最后就是rInflate方法：

```java
private void rInflate(XmlPullParser parser, View parent, final AttributeSet attrs)  
        throws XmlPullParserException, IOException {  

    final int depth = parser.getDepth();  
    int type;  

    while (((type = parser.next()) != XmlPullParser.END_TAG ||  
            parser.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {  

        if (type != XmlPullParser.START_TAG) {  
            continue;  
        }  

        final String name = parser.getName();  
          
        if (TAG_REQUEST_FOCUS.equals(name)) {  
            parseRequestFocus(parser, parent);  
        } else if (TAG_INCLUDE.equals(name)) {  
            if (parser.getDepth() == 0) {  
                throw new InflateException("<include /> cannot be the root element");  
            }  
            parseInclude(parser, parent, attrs);  
        } else if (TAG_MERGE.equals(name)) {  
            throw new InflateException("<merge /> must be the root element");  
        } else {  
            final View view = createViewFromTag(name, attrs);  
            final ViewGroup viewGroup = (ViewGroup) parent;  
            final ViewGroup.LayoutParams params = viewGroup.generateLayoutParams(attrs);  
            rInflate(parser, view, attrs);  
            viewGroup.addView(view, params);  
        }  
    }  

    parent.onFinishInflate();  
}  
```

这个方法也很简单，就是通过parser解析xml节点再生成对应View的过程。

**以上即为xml解析到View的过程**



对于Activity中的```setContentView```方法，也可以直接设置View作为参数：

```java
View content =LayoutInflater.from(this).inflate(R.layout.activity_main,rootView);  
setContentView(content); 
```

这里会有一个区别，当我们如此构造View的时候，对于xml的根节点明显是没有父View的，而根节点的```layout_width```以及```layout_height```属性设置是针对父布局而言，当父布局为null的时候，这个参数自然无效了。

所以xml如下的时候：

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:tools="http://schemas.android.com/tools"  
    android:layout_width="300dip"  
    android:layout_height="300dip"  
    android:background="#888888"  
    tools:context=".MainActivity" >  

	...

</RelativeLayout>
```

预先构造View然后作为参数调用```setContentView```的时候，RelativeLayout的宽高其实是占满DecorView的内容区域的，其宽高参数都不生效了。

而以布局资源id作为参数调用```setContentView```的时候，RelativeLayout的宽高就显示为设置值，因为这种情况xml根布局是拥有父View的。

如何解决呢？

```java
@Override  
protected void onCreate(Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
    RelativeLayout rootView = new RelativeLayout(this);  
    View content = LayoutInflater.from(this).inflate(R.layout.activity_main, rootView);  
    setContentView(content);  
}
```

