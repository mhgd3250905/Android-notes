## Android 简单自定义TextView

> 这里主要是了解关于`onMeasure`方法的测量。

### `onMeasure`测量宽高

通过`onMeasure`方法来测量宽高，然后通过`setMeasureDimension(width, height)`方法来申请需要的空间。

```java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    final int width = getMySize(widthMeasureSpec, getTextWidth(text));
    final int height = getMySize(heightMeasureSpec, getTextHeight(text));
    setMeasuredDimension(width, height);
}
```

下面是`getMySize(measureSec, defaultWidth)`方法：

```java
/**
 * 获取测量大小
 *
 * @param measureSpec
 * @return
 */
private int getMySize(int measureSpec, int widthDefault) {
    int result;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
    if (specMode == MeasureSpec.EXACTLY) {
        result = specSize;
    } else if (specMode == MeasureSpec.AT_MOST) {
        result = Math.min(widthDefault, specSize);
    } else {
        result = widthDefault;
    }
    return result;
}
```

总结起来和你简单

- 如果是`EXACTLY`那么就是直接使用父布局给的值；
- 如果是`AT_MOST`那么就使用最小宽度和给定值的最小值；
- 如果是`UNSPECIFIED`那么就使用最小宽度；

这里的重点是最小宽度的获取，也是就是指定文字的宽高获取以及文字的摆放。

### 文字尺寸的测量

```java
//获取文字宽度
private int getTextWidth(String text) {
    Rect rect = new Rect();
    paint.getTextBounds(text, 0, text.length(), rect);
    return rect.width() + getPaddingLeft() + getPaddingRight();
}
//获取文字高度
private int getTextHeight(String text) {
    Rect rect = new Rect();
    paint.getTextBounds(text, 0, text.length(), rect);
    return rect.height() + getPaddingTop() + getPaddingBottom();
}
```

### 文字的摆放

`Canvas.draw(text, x, y, paint)`中的参数y，指的是文字的基线（baseLine）。x的值并不是最左边的字符的起点，绝大多数的字符，他们的宽度都是要略大于实际显示宽度，字符的左右会留出一部分空闲，用于文字之间的间隔，以及文字与边框之间的间隔。

`FontMetircs.getFontMetrics()`获取Paint的`FontMetrics`。

`FontMetrics`是个相对专业的工具类，它提供了几个文字排印方面的数值：ascent， descent，top，bottom，leading。

![自定义TextView_1](.\img\自定义TextView_1.png)

- **baseLine**： 基线

- **ascent/descent**：上图中的绿色和橙色的线，他们的作用是限制普通字符的顶部和底部范围。

  普通字符，上不会高过ascent，下不会低过descent。因此绝大部分字符都会被限定在ascent到descent之间的范围内。在Android中。ascent的值时图中绿线和baseLine的相对位移，值为负，descent的值时图中橙线基于baseLine的相对位移，值为正。

- **top/bottom**：上图中的蓝色线和红色线，他的作用是限制所有字形的顶部和底部范围。除了普通字符，有些字形的显示范围是会超过ascent和descent的，而top和bottom所限制的是所有字形的显示范围，包括特殊字形。

- **leading**：这个词的本意并不是行的额外间距，而是行距，即两个相邻的baseLine之间的距离。不过对于很多非专业领域，leading的意思被改变了，被大家当做行的额外间距来用；而Android里的leading，同样也是行的额外间距的意思。

`FontMetrics`提供的就是Paint根据当前的字体和字号，得出这些值的推荐值。它把这些值以变量的形式存储，供开发者需要时使用。

- `FontMetrics.ascent`：float类型；
- `FontMetrics.descent`：float类型；
- `FontMetrics.top`：float类型；
- `FontMetrics.bottom`：float类型；
- `FontMetrics.leading`：float类型；

另外，ascent和descent这两个值还可以通过`Paint.ascent()`和`Paint.descent()`来快捷获取。

```java
//计算基线
Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
int dy=(fontMetricsInt.descent-fontMetricsInt.ascent)/2-fontMetricsInt.descent;
int baseLine=getHeight()/2+dy;
```

![自定义TextView_2](.\img\自定义TextView_2.png)

下面为完整的onDraw方法：

```java
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //计算基线
    Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
    int dy=(fontMetricsInt.descent-fontMetricsInt.ascent)/2-fontMetricsInt.descent;
    int baseLine=getHeight()/2+dy;
    int x=getPaddingLeft();
    //x: 开始的位置 y: 基线
    canvas.drawText(text, x, baseLine, paint);
}
```

