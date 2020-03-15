# 过度绘制

### 1.如何避免过度绘制

#### 1.1 过度绘制颜色定义

1. 原色 -> 像素点在屏幕上绘制了一次
2. 蓝色 -> 像素点在屏幕上绘制了两次
3. 绿色 -> 像素点在屏幕上绘制了三次
4. 粉色 -> 像素点在屏幕上绘制了四次
5. 红色 -> 像素点在屏幕上绘制了五次

#### 1.2 过度绘制的定义

不出现粉色和红色，也就是说最多可以接受的过度绘制是2次（绿色）

#### 1.3 解决办法

##### 1.3.1 合理选择控件容器

在目前Android提供的布局控件中如LinearLayout、TableLayout、FrameLayout、RelativeLayout、ConstrainLayout等等，对于LinearLayout来说使用方便，但是只能描述一个方向上的连续排列的空间，容易导致布局文件嵌套太深，不符合布局扁平化的设计原理，但是RelativeLayout和ConstrainLayout布局几乎可以描述任意复杂度的界面，但是表达能力越强的容器空间，性能往往略低，因为他们在onMeasure和onLayout阶段会耗费更多时间。

综上所述：**LinearLayout易用，效率高，表达能力有限，RelativeLayout和ConstrainLayout复杂，表达能力强，但是效率稍逊。**

所以当某一个界面，使用LinearLayout并不会比RelativeLayout和ConstrainLayout带来更多的控件数和空间层级时，我们要有限考虑使用效率更高的Linearlayout。**根据实际情况做一个取舍，在保证性能的同时，尽量避免OverDraw**

##### 1.3.2 去掉window默认的背景
但我们使用了Android自带的一些主题额时候，window会被默认添加一个纯色的背景，这个背景是被DecorView持有的，如果我们自定义布局的时候又添加了一张背景图或者设置背景色，那么DecorView的background就是无用的，但是它仍然会调用onDraw造成一次OverDraw，所以可以做如下操作取消这个背景绘制：
```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_main);
    }
```

##### 1.3.3 去掉其他不必要的背景
对于ViewGroup来说，只要设置了background属性，那么就会执行其onDraw方法，所以在那些会被子ViewGroup或子View完全遮盖的ViewGroup来说，没有设置backgroudn属性的必要，同时在对控件设置background时，如设置为selector背景，而他normal状态color和背景颜色一致，那么可以设置为```@android:color/transparent```来解决问题。

**所以在开发过程中，我们为某个View或者某个ViewGroup设置背景的时候，先考虑一下是否真的有必要，或者思考一下这个背景能不能分段设置在子View上，而不是图方便直接设置在根View上**

##### 1.3.4 ClipRect和QuickReject 

为了解决OverDraw的问题，Android系统柜通过避免绘制那些完全不可见的组件来尽量减小消耗，对于自定义View来说，重写了onDraw方法后，Android系统无法检测在onDraw里面具体执行了什么操作，系统无法监控并自动优化，也就无法避免OverDraw了。

我们可以在onDraw方法重写中通过```canvas.clipRect()```来帮助系统识别那些可见的区域。这个方法可以指定一块区域，只有在这个区域内才会被绘制，其他区域都会被忽略，这个API恶意很好的帮助那些有多组重叠组件的自定义View来控制显示的区域。同时clipRect方法还可以帮助节约CPU与GPU资源，在clipRect区域之外的绘制指令都不会被执行，那些部分内容在矩形区域内的组件，仍然会得到绘制。除了clipRect方法之外，我们还可以使用```canvas.quickreject()```来判断是否没和某个矩形相交，从而跳过那些非矩形区域内的绘制操作。

##### 1.3.5 使用ViewStub占位

ViewStub -> **高效占位符**

通常在运行时动态根据条件来决定显示哪个View或布局的时候，都是将全部都写好，然后将其可见性都设置为```View.GONE```，然后再代码中动态更改其可见性。

这样做的缺点就是，耗费资源。虽然把view的初始可见```View.GONE```但是在Inflate布局的时候View仍然会Inflate，也就是说仍然会创建对象，会被实例化，会被设置属性，耗费内存等资源。

推荐的做法就是使用```ViewStub```：这是一个轻量级的View，它是一个看不见的不占布局位置，占用资源非常小的空间。可以为```ViewStub```指定一个布局，在Inflate布局的时候，只有```ViewStub```会被初始化，然后将其设置为可见的时候，或者调用了```ViewStu.inflate()```的时候，其中的布局才会被Inflate和实例化。
```xml
<ViewStub 
    android:id="@+id/view_stub"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout="@layout/tet" /> 
```
加载布局的时候，可以使用以下方法：
```java
//第一种方法
((ViewStub)findViewById(R.id.view_stub)).setVisibility(View.VISIBLE);
//第二种方法
View view=((ViewStub)findViewById(R.id.view_stub)).inflate();
```
**注意```ViewStub```的inflate方法只能调用一次，第二次就会报错**

##### 1.3.6 用Merge减少布局深度

Merge标签的作用就是**干掉一个view层级**。

使用Merge的条件限制：
1. 子视图不需要指定任何针对父视图的布局属性，也就是说父容器仅仅是个容器，子视图只需要直接添加到父视图上用于显示就行。
2. 比如如需要在LinearLayout里面嵌入一个布局（或者视图），而恰好这个布局（或者视图）的根节点也是LinearLayout，这样就多了一层没用的嵌套，这个时候就可以使用merge根标签来避免拖慢程序速度。
3. Merge只能作为XML布局的根节点使用
4. 在inflate以```<merge/>```开头的不文件时，必须指定一个父布ViewGroup，并且必须设定```attachToRoot```为true;

[Android布局优化Merge的使用](https://www.jianshu.com/p/69e1a3743960)

##### 1.3.7 善用draw9Patch
Android 2D渲染器对.9图有优化，那么可以将vie（类似聊天框样式的view）的背景图设置为.9图，同时将图中和前景色重叠的部分设置为透明。

##### 1.3.8 慎用Alpha
加入需要对一个View做Alpha转化，需要先将View绘制出来，然后做Alpha转化，最后将转化后的效果绘制在界面上，通俗点说Alpha转化就要对当前的View绘制两边，绘制效率大打折扣，耗时翻倍，所以Alpha还是要慎用。

如果一定要做Aplha转化的话，可以采用缓存的方式：
```java
view.setLayerType(LAYER_TYPE_HARDWARE);
...
doSomeThing();
...
view.setLayerType(LAYER_TYPE_NONE);
```
通过setLayerType方式可以将当前界面缓存在GPU中，这样不需要每次绘制原始界面，但是GPU内存是相当宝贵的，所以用完要马上释放掉。

##### 1.3.9 避免过度设计
过度设计会给App带来不好的体验，产生复杂的布局层级、重叠的View、重叠的背景，这样就会引起以上的各种问题，所以产品设计也要有一个权衡，在复杂的业务逻辑与简单易用的界面展现做一个平衡，而不是一味的过度设计。