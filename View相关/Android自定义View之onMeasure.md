# Android自定义View之onMeasure

### OnMeasure什么时候会调用

onMeasure方法的作用是测量空间大小，当我们创建一个View（执行构造方法）的时候不需要测量空间的大小，只有将这个View放入一个容器（父控件）中的时候才需要测量，而这个测量方法就是父控件唤起调用的。

当父控件需要放置该控件的时候，父控件会调用子控件的onMeasure方法询问子控件：”你的尺寸？我需要给你多大的地方？“，然后传入两各参数：```widthMeasureSpec```和```heightMeasureSpec```，这两个参数就是父控件高速子控件可获得的空间以及关于这个空间的约束条件，子控件拿着这些条件就能正确的测量自身的宽度了。

### onMeasure方法执行流程

上面说到onMeasure方法是父控件调用的，所有父控件都是ViewGroup的子类，ViewGroup是一个抽象类，它里面有一个抽象方法onLayout，这个方法的作用就是摆放它所有的子控件（安排位置），因为是抽象类，所以不能直接new对象，所以我们再布局文件中是可以使用View但是不能直接使用ViewGroup（代码中也一样）。

在给子控件确定位置之前，必须活的到子控件的大小，而ViewGroup并没有重写View的onMeasure方法，只提供了（```measureChildren```、```measureChild```、```measureChildWithMargins```）等测量子View的相关方法，这样做也是因为不同的容器摆放子控件的方式不同，比如RelativeLayout、LinearLayout这两个ViewGroup的子类，它们摆放子控件的方式不同，有的是线性摆放，有的是叠加摆放，这就导致测量子控件的方式会有所差别，所以ViewGroup就干脆不直接测量子控件，而是叫他的子类根据自己的布局特性重写onMeasure方法去测量。最后通过使用ViewGroup的measureChildxxx系列方法得到最终的子控件大小。

测量的时候父控件的onMeasure方法会遍历他所有的子控件，挨个调用子控件的measure方法，measure会调用onMeasure，然后会调用setMeasureDimension方法保存测量的大小，一次遍历下来，第一个子控件以及这个子控件中所有子控件都会完成测量工作，然后开始测量第二个子控件...；最后父控件所有的子控件都完成测量以后会调用setMeasureDimension方法保存自己的测量大小。值得注意的是，这个过程不止执行一次，也就是说有可能重复执行，因为有的时候一轮测量下来，父控件发现某一个子控件的尺寸不符合要求，就会重新测量一遍。

举个例子：

![自定义View_1](.\img\自定义View_1.webp)



![自定义View_2](.\img\自定义View_2.webp)

下面是测量的时序图：

![自定义View_3](.\img\自定义View_3.webp)



### MeasureSpec类

MeasureSpec约束是由父控件传递给子口控件的，我们看一看源码：

```java
public static class MeasureSpec {
    private static final int MODE_SHIFT = 30;
    private static final int MODE_MASK = 0x3 << MODE_SHIFT;
    /**
     * 父控件不强加任何约束给子控件，它可以是它想要任何大小
     */
    public static final int UNSPECIFIED = 0 << MODE_SHIFT;
    /**
     * 父控件已为子控件确定了一个确切的大小，孩子将被给予这些界限，不管子控件自己希望的是多大
     */
    public static final int EXACTLY = 1 << MODE_SHIFT;
    /**
     * 父控件会给子控件尽可能大的尺寸
     */
    public static final int AT_MOST = 2 << MODE_SHIFT;

    /**
     * 根据所提供的大小和模式创建一个测量规范
     */
    public static int makeMeasureSpec(int size, int mode) {
        if (sUseBrokenMakeMeasureSpec) {
            return size + mode;
        } else {
            return (size & ~MODE_MASK) | (mode & MODE_MASK);
        }
    }
    /**
     * 从所提供的测量规范中提取模式
     */
    public static int getMode(int measureSpec) {
        return (measureSpec & MODE_MASK);
    }
    /**
     * 从所提供的测量规范中提取尺寸
     */
    public static int getSize(int measureSpec) {
        return (measureSpec & ~MODE_MASK);
    }
    ...
}
```

从源码中我们知道，MeasureSpec其实就是尺寸和模式通过各种运算计算出的一个整型值，它提供了三种模式（UNSPECIFIED、EXACTLY、AT_MOST）

> **约束**：UNSPECIFIED（未指定）
>
> **布局参数**：无
>
> **值**：0(00000000000000000000000000000000)
>
> **说明**：父控件没有对子控件施加任何约束，子控件可以得到任意想要的大小（使用较小）

****

> **约束**：EXACTLY（完全）
>
> **布局参数**：match_parent/具体宽高值
>
> **值**：1073741824(01000000000000000000000000000000)
>
> **说明**：父控件给子控件决定了确切大小。子控件将被限定在给定的边界里而忽略它本身大小。怎样理解忽略它本身大小呢？假设给定自定义控件设置的大小为1dp，那可想而知，控件就看不到了，相比而言match_parent也是如此，假设父控件只有1dp，那子控件也就看不到了，所以这个模式子控件将被限定在给定的边界里而忽略它本身大小。

- 如果是match_parent，说明父控件已经明确知道子控件想要多大的尺寸了（就是剩余的空间都要了）
- 如果是设置的具体值，那更应该说明父控件已经知道子控件的大小了（具体的值）

****

> **约束**：AT_MOST(至多)
>
> **布局参数**：warp_centent
>
> **值**：-2147483648(10000000000000000000000000000000)
>
> **说明**：子控件至多达到指定大小的值。这种模式下父控件无法确定子控件的尺寸，这种情况下子控件需要根据需求去设置大小。



针对这几种模式，在自定义控件中也要处理相应的尺寸：

```java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	final int width = getMySize(widthMeasureSpec);
	final int height = getMySize(heightMeasureSpec);
	final int min = Math.min(width, height);//保证控件为方形
	setMeasuredDimension(min, min);
}
/**
 * 获取测量大小
 */
private int getMySize(int measureSpec) {
	int result;
	int specMode = MeasureSpec.getMode(measureSpec);
	int specSize = MeasureSpec.getSize(measureSpec);
	if (specMode == MeasureSpec.EXACTLY) {
		result = specSize;//确切大小,所以将得到的尺寸给view
	} else if (specMode == MeasureSpec.AT_MOST) {
		//默认值为450px,此处要结合父控件给子控件的最多大小(要不然会填充父控件),所以采用最小值
		result = Math.min(DEFAULT_SIZE, specSize);
	} else {
		result = DEFAULT_SIZE;
	}
	return result;
}
```

### 从ViewGroup的onMeasure到View的onMeasure

- ViewGroup中三个测量子控件的方法：

  通过上面的介绍，如果要自定义ViewGroup就必须重写onMeasure方法，在这里测量子控件的尺寸。子控件的尺寸怎么测量呢？ViewGroup中提供了三个关于测量子控件的方法：

```java
/**
 * 遍历ViewGroup中所有的子控件，调用measuireChild测量宽高
 */
protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
	final int size = mChildrenCount;
	final View[] children = mChildren;
	for (int i = 0; i < size; ++i) {
		final View child = children[i];
		if ((child.mViewFlags & VISIBILITY_MASK) != GONE) {
			//测量某一个子控件宽高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
		}
	}
}

/**
 * 测量某一个child的宽高
 */
protected void measureChild(View child, int parentWidthMeasureSpec,int parentHeightMeasureSpec) {
	final LayoutParams lp = child.getLayoutParams();
	//获取子控件的宽高约束规则
	final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
	mPaddingLeft + mPaddingRight, lp.width);
	final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, mPaddingTop + mPaddingBottom, lp.height);

	child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}

/**
 * 测量某一个child的宽高，考虑margin值
 */
protected void measureChildWithMargins(View child,
int parentWidthMeasureSpec, int widthUsed,int parentHeightMeasureSpec, int heightUsed) {
	final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
	//获取子控件的宽高约束规则
	final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
	final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin + heightUsed, lp.height);
	//测量子控件 
    child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
}
```

其中```measureChildren```就是遍历所有子控件挨个测量，最终测量子控件的方法就是```measureChild```和```measureChildWithMergins```了，```measureChildWithMargins```跟```measureChild```的区别就是父控件支持```margin```属性对子控件的测量有影响，比如我们的屏幕是1080x1920的，子控件的宽度为填充父窗体，如果使用了```marginLeft```并设置为100；在测量子控件的时候，如果用```measureChild```，计算的宽度是1080，而如果是使用```measureChildWithMargins```，计算的宽度是1080-100=980。

### ViewGroup支持margin属性

ViewGroup中有两个内部类，ViewGroup.LayoutParams和ViewGroup.MarginLayoutParams，MarginLayoutParams继承自LayoutParams，这两个内部类就是ViewGroup的布局参数类，比如我们再LinearLa等不居中使用的layout_width和layout_height等以layout_开头的属性都是布局属性，最后都要通过LayoutParams获取相应的属性。在View中有一个mLayoutParams的变量用来保存这个View的所有布局属性。

LayoutParams和MarginLayoutParams的关系：

LayoutParams中定义了两个属性（也就是layout_width和layout_height的来头）

```java
<declare-styleable name= "ViewGroup_Layout">
    <attr name ="layout_width" format="dimension">
        <enum name ="fill_parent" value="-1" />
        <enum name ="match_parent" value="-1" />
        <enum name ="wrap_content" value="-2" />
    </attr >
    <attr name ="layout_height" format="dimension">
        <enum name ="fill_parent" value="-1" />
        <enum name ="match_parent" value="-1" />
        <enum name ="wrap_content" value="-2" />
    </attr >
</declare-styleable >
```

MarginLayoutParams是layoutParams的子类，它当然也延续了layout_width和layout_height属性，但是它扩充了其他属性：

```java
< declare-styleable name ="ViewGroup_MarginLayout">
    <attr name ="layout_width" />   <!--使用已经定义过的属性-->
    <attr name ="layout_height" />
    <attr name ="layout_margin" format="dimension"  />
    <attr name ="layout_marginLeft" format= "dimension"  />
    <attr name ="layout_marginTop" format= "dimension" />
    <attr name ="layout_marginRight" format= "dimension"  />
    <attr name ="layout_marginBottom" format= "dimension"  />
    <attr name ="layout_marginStart" format= "dimension"  />
    <attr name ="layout_marginEnd" format= "dimension"  />
</declare-styleable >
```

这就是margin属性的由来。

- 为什么LayoutParams类要定义在ViewGroup中？

  众所周知ViewGroup是所有容器的基类，一个控件需要被包裹在一个容器中，这个容器必须提供一种规则控制子控件的摆放，所以ViewGroup提供一个布局属性类，用于控制子控件的布局（layout_）属性。

- 为什么view中会有一个mLayoutParams变量？

  我们再构造方法中初始化布局文件中的属性值，我们属性分为两种。一种是本View的绘制属性，比如TextView的文本、文字颜色、背景灯，这些属性是跟View的绘制相关的。另一种就是以layout_开头的叫做布局属性，这些属性是父控件对子控件的大小以及位置的一些描述属性，这些属性都是用ViewGroup.LauoutParams定义的，所以用一个变量（mLayoutParams）保存着。

### getChildMeasureSpec方法

measureChildWithMargins跟measureChild都调用了这个方法，其作用就是通过**父控件的宽高约束规则**和**父控件加在子控件上的宽高布局参数**生成一个子控件的约束。我们知道View的onMeasure方法需要两个参数（父控件对View的宽高约束），这个宽高约束就是通过这个方法生成的。为什么通过父类布局的约束生成子View的约束呢？

打个比方，父控件的宽高约束为wrap_content，而子控件为match_parent，这意思就是，父控件说我的宽高就是包裹我的子控件，而子控件说我的宽高就是填充满父窗体。最后该如何确定大小呢？所以我们通过这个方法为子控件生成一个新的约束规则。只要记住，子控件的宽高约束规则是父控件调用getChildMeasureSpec方法生成。

measureChild源码，measureChildWithMargins类似：

```java
protected void measureChild(View child, int parentWidthMeasureSpec,
	int parentHeightMeasureSpec) {
	//获取了子控件的layout_布局属性
	final LayoutParams lp = child.getLayoutParams();
	final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, mPaddingLeft + mPaddingRight, lp.width);
	final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, mPaddingTop + mPaddingBottom, lp.height);
	child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}
```

![自定义View_4](.\img\自定义View_4.webp)

getChildMeasureSpec方法代码不多，也比较简单，就是几个switch将各种情况考虑之后生成一个字控件的新的宽高约束，这个方法的结果也能够用如下一个表来概括：

![自定义View_5](.\img\自定义View_5.webp)

进行上面的步骤，接下来就是在measureChildWithMargins或者measureChild中调用子控件的measure方法测量子控件的尺寸了。

### View的onMeasure

View的onMeasure方法已经默认为我们的空间测量了宽高，我们看看它做了什么工作：

```java
protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension( getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
            getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
}

/**
 * 为宽度获取一个建议最小值
 */
protected int getSuggestedMinimumWidth () {
    return (mBackground == null) ? mMinWidth : max(mMinWidth , mBackground.getMinimumWidth());
}
/**
 * 获取默认的宽高值
 */
public static int getDefaultSize (int size, int measureSpec) {
    int result = size;
    int specMode = MeasureSpec. getMode(measureSpec);
    int specSize = MeasureSpec. getSize(measureSpec);
    switch (specMode) {
    case MeasureSpec. UNSPECIFIED:
        result = size;
        break;
    case MeasureSpec. AT_MOST:
    case MeasureSpec. EXACTLY:
        result = specSize;
        break;
    }
    return result;
}
```

从源码可以了解到：

- 如果View的宽高模式为未指定，他的宽高将设置为```android:minWidth```和```android:minHeight```的值与背景宽高中较大的一个；
- 如果View的宽高模式为EXACTLY（具体的size），最终宽高就是这个size的值；
- 如果View的宽高模式EXACTLY（填充父控件），最终宽高降为填充父控件；
- 如果View的宽高模式为AT_MOST（包裹内容），最终宽高也是填充父控件。

**也就是说如果我们的自定义控件在布局文件中，只要设置指定的具体宽高，或者MATCH_PARENT的情况，我们可以不用重写onMeasure方法。**



但是如果自定义控件需要设置包裹内容WRAP_CONTENT,我们就需要重写onMeasure，为控件设置需要的尺寸；默认情况下WRAP_CONTENT的处理也将填充整个父控件。



### setMEAsureDimension

onMeasure方法最后需要调用setMeasureDimension方法来保存测量的宽高值，如果不调用这个方法，可能会产生不可以测的问题。



### 总结

测量控件大小是父控件发起的；

父控件要测量控件大小，需要重写onMeasure方法，然后调用measureChildren或者measureChildWithMargins方法；

View的onMeasure方法的参数是通过getChildMeasureSpec生成的；

如果我们自定义控件需要使用wrap_content属性，我们需要重写onMeasure方法；

测量控件的步骤：

```父控件onMeasure``` -> ```measureChildren```或```measureChildWithMargin``` -> ```getChildMeasureSpec``` -> ```子控件的measure``` -> ```onMeasure``` -> ```setMeasureDeiension``` -> ```父控件onMeasure结束调用setMeasureDimension```

最后保存自己的大小。



