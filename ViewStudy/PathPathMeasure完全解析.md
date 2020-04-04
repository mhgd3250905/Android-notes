## Path&PathMeasure完全解析

### 1. 谈谈Path的使用

```java
public class Path {
    /**
     * 空构造方法
     */
    public Path() {
        mNativePath = init1();
    }
 
    /**
     * 重置Path
     */
    public void reset() {
        isSimplePath = true;
        mLastDirection = null;
        if (rects != null) rects.setEmpty();
        // We promised not to change this, so preserve it around the native
        // call, which does now reset fill type.
        final FillType fillType = getFillType();
        native_reset(mNativePath);
        setFillType(fillType);
    }
 
    /**
     * 和reset一样，只不过这个会将FillType也清楚掉，但reset不会
     */
    public void rewind() {
        isSimplePath = true;
        mLastDirection = null;
        if (rects != null) rects.setEmpty();
        native_rewind(mNativePath);
    }
 
    /**
     * Path和Path之间的运算方法
     */
    public boolean op(Path path, Op op) {
        return op(this, path, op);
    }
 
    /**
     * 得到填充的类型
     */
    public FillType getFillType() {
        return sFillTypeArray[native_getFillType(mNativePath)];
    }
 
    /**
     * 设置Path的填充类型
     */
    public void setFillType(FillType ft) {
        native_setFillType(mNativePath, ft.nativeInt);
    }
 
    /**
     * 判断是否反向填充
     */
    public boolean isInverseFillType() {
        final int ft = native_getFillType(mNativePath);
        return (ft & FillType.INVERSE_WINDING.nativeInt) != 0;
    }
 
    /**
    * 计算Path所占用的空间以及位置，将信息存入bounds中，exact:是否精确测量
    */
    @SuppressWarnings({"UnusedDeclaration"})
    public void computeBounds(RectF bounds, boolean exact) {
        native_computeBounds(mNativePath, bounds);
    }
    /**
     * 自动改变，取反
     */
    public void toggleInverseFillType() {
        int ft = native_getFillType(mNativePath);
        ft ^= FillType.INVERSE_WINDING.nativeInt;
        native_setFillType(mNativePath, ft);
    }
 
    /**
     * Path是否为空
     */
    public boolean isEmpty() {
        return native_isEmpty(mNativePath);
    }
 
    /**
     * 将画笔移动的坐标位置
     */
    public void moveTo(float x, float y) {
        native_moveTo(mNativePath, x, y);
    }
 
    /**
     * 和上面一样，只不过上面是绝对位置，这个是相对位置（相对于上一个点）
     */
    public void rMoveTo(float dx, float dy) {
        native_rMoveTo(mNativePath, dx, dy);
    }
 
    /**
     * 在lineTo之前要先moveTo否则则将默认为从原点开始划线
     */
    public void lineTo(float x, float y) {
        isSimplePath = false;
        native_lineTo(mNativePath, x, y);
    }
 
    /**
     * 于lineTo相对，这个是相对位置
     */
    public void rLineTo(float dx, float dy) {
        isSimplePath = false;
        native_rLineTo(mNativePath, dx, dy);
    }
 
    /**
     * 二阶贝塞尔曲线
     */
    public void quadTo(float x1, float y1, float x2, float y2) {
        isSimplePath = false;
        native_quadTo(mNativePath, x1, y1, x2, y2);
    }
 
    /**
     * 
     */
    public void rQuadTo(float dx1, float dy1, float dx2, float dy2) {
        isSimplePath = false;
        native_rQuadTo(mNativePath, dx1, dy1, dx2, dy2);
    }
 
    /**
     * 三阶贝塞尔曲线
     */
    public void cubicTo(float x1, float y1, float x2, float y2,
                        float x3, float y3) {
        isSimplePath = false;
        native_cubicTo(mNativePath, x1, y1, x2, y2, x3, y3);
    }
 
    /**
     * 
     */
    public void rCubicTo(float x1, float y1, float x2, float y2,
                         float x3, float y3) {
        isSimplePath = false;
        native_rCubicTo(mNativePath, x1, y1, x2, y2, x3, y3);
    }
 
    /**
     * 画弧线
     */
    public void arcTo(RectF oval, float startAngle, float sweepAngle,
                      boolean forceMoveTo) {
        arcTo(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, forceMoveTo);
    }
 
    /**
     * 当调用close则将结束点和起始点连线
     */
    public void close() {
        isSimplePath = false;
        native_close(mNativePath);
    }
 
    /**
     * 绘制的方向
     */
    public enum Direction {
        CW  (0),    // 顺时针方向
        CCW (1);    //逆时针
        Direction(int ni) {
            nativeInt = ni;
        }
        final int nativeInt;
    }
 
    /**
     * 提供了大量的add图形的方法（将更多的图片添加到Path路径便于设置方向、填充方式）
     */
    public void addXXX(XXX) {
    }
 
    /**
     * 将Path进行偏移，偏移之后的结果存入dst中
     */
    public void offset(float dx, float dy, @Nullable Path dst) {
        if (dst != null) {
            dst.set(this);
        } else {
            dst = this;
        }
        dst.offset(dx, dy);
    }
 
    /**
     * 将Path进行偏移，偏移之后的结果写入path中
     */
    public void offset(float dx, float dy) {
        if (isSimplePath && rects == null) {
            // nothing to offset
            return;
        }
        if (isSimplePath && dx == Math.rint(dx) && dy == Math.rint(dy)) {
            rects.translate((int) dx, (int) dy);
        } else {
            isSimplePath = false;
        }
        native_offset(mNativePath, dx, dy);
    }
}
```

其中Path的运算和Path的填充比较常用，下面来介绍一下：

#### Path运算

```java
    /**
     * Path和Path之间的运算
     */
    public enum Op {
        /**
         * path1中减去Path2剩下的部分
         */
        DIFFERENCE,
        /**
         * path1和path2相交的部分
         */
        INTERSECT,
        /**
         * 包含path1和path2部分
         */
        UNION,
        /**
         * 包含path和path2但不包含相交的部分
         */
        XOR,
        /**
         * Path2减去Path1剩下的部分
         */
        REVERSE_DIFFERENCE
    }
```

为了更好的理解，这里附上一个图，基本就是类似交集并集的概念：

<img src=".\img\path_1.jpg" alt="path_1" style="zoom:70%;" />

根据Path我们可以实现一个八卦图的效果：

<img src=".\img\path_2.jpg" alt="path_2" style="zoom:70%;" />

代码如下：

```java
@Override
protected void onDraw(Canvas canvas) {
    canvas.translate(getWidth() / 2, getHeight() / 2);
    canvas.save();
    path1.addCircle(0, 0, 200, Path.Direction.CW);
    path2.addRect(-200, -200, 0, 200, Path.Direction.CW);
    path1.op(path2, Path.Op.INTERSECT);//去相交的区域
    path2.reset();
    path2.addCircle(0, -100, 100, Path.Direction.CCW);
    path1.op(path2, Path.Op.UNION);//去全部的区域
    path2.reset();
    path2.addCircle(0, 100, 100, Path.Direction.CW);
    path1.op(path2, Path.Op.DIFFERENCE);//取Path1减去path2的区域
    canvas.drawPath(path1, paint);
    canvas.restore();
    paint.setColor(Color.WHITE);
    canvas.drawCircle(0, -100, 25, paint);
    paint.setColor(Color.BLACK);
    canvas.drawCircle(0, 100, 25, paint);
}
```

#### Path的填充

```java
    /**
     * Enum for the ways a path may be filled.
     */
    public enum FillType {
        // these must match the values in SkPath.h
        /**
         * 非零环绕数规则
         */
        WINDING(0),
        /**
         *奇偶规则
         */
        EVEN_ODD(1),
        /**
         * 反非零环绕数规则
         */
        INVERSE_WINDING(2),
        /**
         * 反奇偶规则
         */
        INVERSE_EVEN_ODD(3);
 
        FillType(int ni) {
            nativeInt = ni;
        }
 
        final int nativeInt;
    }
```


| 方法           | 判定条件                                  | **解释**                                                     |
| -------------- | ----------------------------------------- | ------------------------------------------------------------ |
| 奇偶规则       | 奇数表示在图形内，偶数表示在图形外        | 从任意位置`p`作一条射线， 若与该射线相交的图形边的数目为奇数，则`p`是图形内部点，否则是外部点。 |
| 非零环绕数规则 | 若环绕数为0表示在图形外，非零表示在图形内 | 首先使图形的边变为矢量。将环绕数初始化为零。再从任意位置`p`作一条射线。当从`p`点沿射线方向移动时，对在每个方向上穿过射线的边计数，每当图形的边从右到左穿过射线时，环绕数加1，从左到右时，环绕数减1。处理完图形的所有相关边之后，若环绕数为非零，则p为内部点，否则，`p`是外部点。 |



#### `Android`中的填充模式有四种

| 模式               | 简介             |
| ------------------ | ---------------- |
| `EVEN_ODD`         | 奇偶规则         |
| `INVERSE_EVEN_ODD` | 反奇偶规则       |
| `WINDING`          | 非零环绕数规则   |
| `INVERSE_WINDING`  | 反非零环绕数规则 |

#### 关于填充的几种方法

| 方法                    | 作用                                           |
| ----------------------- | ---------------------------------------------- |
| `setFillType`           | 设置填充规则                                   |
| `getFillType`           | 获取当前填充规则                               |
| `isInverseFillType`     | 判断是否为反向填充规则                         |
| `toggleInverseFillType` | 切换填充规则(即原有规则与反向规则之间相互切换) |




- **奇偶规则**

  这是一个比较简单，也容易理解，直接用一个简单示例来说明：

  ```java
  mDeafultPaint.setStyle(Paint.Style.FILL);                   // 设置画布模式为填充
  canvas.translate(mViewWidth / 2, mViewHeight / 2);          // 移动画布(坐标系)
  Path path = new Path();                                     // 创建Path
  path.setFillType(Path.FillType.INVERSE_EVEN_ODD);           // 反奇偶规则
  path.addRect(-200,-200,200,200, Path.Direction.CW);         // 给Path中添加一个矩形
  ```
  
  绘制结果如下：
  
  <img src=".\img\path_3.webp" alt="path_3" style="zoom:70%;" />

```java
mDeafultPaint.setStyle(Paint.Style.FILL);                   // 设置画布模式为填充
canvas.translate(mViewWidth / 2, mViewHeight / 2);          // 移动画布(坐标系)
Path path = new Path();                                     // 创建Path
path.setFillType(Path.FillType.EVEN_ODD);                   // 设置Path填充模式为 奇偶规则
path.addRect(-200,-200,200,200, Path.Direction.CW);         // 给Path中添加一个矩形
```

<img src=".\img\path_4.webp" alt="path_4" style="zoom:70%;" />

使用非零环绕原则进行填充，那么图形的经历顺序也会对填充有印象，如下：

```java
mDeafultPaint.setStyle(Paint.Style.FILL);                   // 设置画笔模式为填充
canvas.translate(mViewWidth / 2, mViewHeight / 2);          // 移动画布(坐系)
Path path = new Path();                                     // 创建Path
path.addRect(-200, -200, 200, 200, Path.Direction.CW);		//顺时针绘制
// 添加大正方形
path.addRect(-400, -400, 400, 400, Path.Direction.CCW);
path.setFillType(Path.FillType.WINDING);                    // 设置Path填充模式为非零环绕规则
canvas.drawPath(path, mDeafultPaint);                       // 绘制Path
```

<img src=".\img\path_5.webp" alt="path_5" style="zoom:70%;" />

```java
mDeafultPaint.setStyle(Paint.Style.FILL);                   // 设置画笔模式为填充
canvas.translate(mViewWidth / 2, mViewHeight / 2);          // 移动画布(坐系)
Path path = new Path();                                     // 创建Path
path.addRect(-200, -200, 200, 200, Path.Direction.CCW);		//逆时针绘制
// 添加大正方形
path.addRect(-400, -400, 400, 400, Path.Direction.CCW);
path.setFillType(Path.FillType.WINDING);                    // 设置Path填充模式为非零环绕规则
canvas.drawPath(path, mDeafultPaint);                       // 绘制Path
```

<img src=".\img\path_6.webp" alt="path_6" style="zoom:70%;" />

#### Path的Direction.CW、Direction.CCW

`Path.Direction.CW`: ClockWise，沿顺时针方向绘制；

`Path.Direction.CCW`:counter-clockwise，沿逆时针方向绘制；

举个例子：

```java
@Override
 
protected void onDraw(Canvas canvas) {
 
    super.onDraw(canvas);
 
    //设置画布背景颜色
 
    canvas.drawColor(Color.YELLOW); 
 
    //将坐标系的原点移动到控件的中心
 
    canvas.translate(getMeasuredWidth()/2,getMeasuredHeight()/2);
 
    //创建矩形
    Path path = new Path();
    RectF rectF = new RectF(-200, -200, 200, 200);
    path.addRect(rectF, Path.Direction.CW);
    //重置最后一个点的位置
    path.setLastPoint(-50,50);
    canvas.drawPath(path,paint);
}
```

运行效果图：

<img src=".\img\path_7.png" alt="path_7" style="zoom:70%;" />


之所以运行成如此，就是因为`setLastPoint`重置了最后一点的位置，即D被重置到（-50，50）。可为什么重置点D而不是其它点呢？原因在于`Path.Direction.CW`（表示按顺时针方向绘制）。绘制矩形时，首先以（left，top）为起点，以（width/2，height/2）为中心点，然后根据Direction来绘制下一点。CW表示顺时针，下一点自然就是B，再下一点就是C，最后一个点就是D，由于`setLastPoint`的原因，D点位置变成了（-50，50）。依次连接各个点，便由以上结果。

如果把`Direction.CW`修改为`Direction.CCW`，那么运行效果图为：

<img src=".\img\path_8.png" alt="path_8" style="zoom:70%;" />

### 2. PathMeasure的使用

首先分析方法：

```java
public class PathMeasure {
    private Path mPath;
 
    /**
     * 用这个构造函数可创建一个空的PathMeasure，但是使用之前需要先调用setPath方法来与Path进行关联。
     * 被关联的Path必须是已经创建的好的，如果关联之后Path的内容进行了更改则需要使用setPath方法重新进行关联
     */
    public PathMeasure() {
        mPath = null;
        native_instance = native_create(0, false);
    }
    
  /**
     * 用这个构造函数是创建一个PathMeasure并关联一个Path，其实和创建一个空的PathMeasure后调用setPath进行关联效果是一样的
     * 同样被关联的Path也必须已经是创建好的，如果关联的Path内容进行了更改，则需要是用setPath方法重新关联。
     * 第二个参数是用来确保Path闭合，如果设置为true，则不论之前是否闭合，都会自动闭合该Path（如果Path可以闭合的话）
     * 这里需要注意：
     *     1、不论forceClosed设置为何种状态都不会影响原有的状态，即Path与PathMeasure关联之后，之前的Path不会有任何的改变
     *     2、forceClosed的设置状态可能会影响测量结果，如果Path未闭合但在与PathMeasure关联的时候设置了true，则测量的结果
     *     可能会比Path实际的长度稍长一点，获取到是该Path闭合的状态
    */
    public PathMeasure(Path path, boolean forceClosed) {
        // The native implementation does not copy the path, prevent it from being GC'd
        mPath = path;
        native_instance = native_create(path != null ? path.readOnlyNI() : 0,
                                        forceClosed);
    }
 
    
    public void setPath(Path path, boolean forceClosed) {
        mPath = path;
        native_setPath(native_instance,
                       path != null ? path.readOnlyNI() : 0,
                       forceClosed);
    }
 
    /**
     * 获取Path的总长度
     */
    public float getLength() {
        return native_getLength(native_instance);
    }
 
    /**
     * 用于得到路径上某一长度位置以及该位置的正切值
     * 返回值：判断是否获取成功 true表示成功，数据会存入pos和tan中
     * 参数
     *     distance : 距离Path起点的长度 取值范围0<=distance<=getLength
     *     pos      : 该点的坐标值
     *     tan      : 该点的正切值
 */
    public boolean getPosTan(float distance, float pos[], float tan[]) {
        if (pos != null && pos.length < 2 ||
            tan != null && tan.length < 2) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return native_getPosTan(native_instance, distance, pos, tan);
    }
 
    // must match flags in SkPathMeasure.h
    public static final int POSITION_MATRIX_FLAG = 0x01;  
    // must match flags in SkPathMeasure.h
    public static final int TANGENT_MATRIX_FLAG  = 0x02;    
 
    /**
     * 用于得到路径上某一长度的位置以及该位置的正切值矩阵
     * 返回值：判断获取是否成功
     * 参数
     *      1、distance ：距离起点的长度
　　 *      2、matrix : 根据flags封装好的matrix，会根据flags的位置而存入不同的内容
     *      3、flags : 规定哪些内容会存入到matrix中，可选择POSITION_MATRIX_FLAG（位置）  ANGENT_MATRIX_FLAG(正切)
    */
    public boolean getMatrix(float distance, Matrix matrix, int flags) {
        return native_getMatrix(native_instance, distance, matrix.native_instance, flags);
    }
 
    /**
     * 获取Path的一个片段
     * 返回值：判断截取是否成功，true表示截取成功，结果存入dst中，false表示截取失败，不会存在dst中
     * 参数
     *     startD:开始截取位置距离Path起点的长度，取值范围 0 <=startD<stopD<=path总长度
      stopD:结束截取位置距离Path起点的长度，取值范围  0<=startD<stopD<=path总长度
     *     dst  : 截取的Path将会添加到dst中，注意是添加不是替换
     *     startWithMove: 起始点是否使用moveTo，用于保证截取的Path第一个点位置不变
     *               true：保证截取片段不会发生变形    false ： 保证截取片段的Path连续性
     *     注意：
     *          1、如果startD、stopD的数值不在取值范围【0，getLength】内，或者startD==stopD则返回false，不会改变dst的内容
     *          2、如果在Android4.4或者之前的版本，在默认开启硬件加速的情况下，更改了dst的内容后可能会出现问题，请在关闭
     *          硬件加速或者给dst添加一个单个操作，例如dst.rLineTo(0,0)
     *          3、可以用一下的规则来判断startWithMoveTo的取值
     */
    public boolean getSegment(float startD, float stopD, Path dst, boolean startWithMoveTo) {
        // Skia used to enforce this as part of it's API, but has since relaxed that restriction
        // so to maintain consistency in our API we enforce the preconditions here.
        float length = getLength();
        if (startD < 0) {
            startD = 0;
        }
        if (stopD > length) {
            stopD = length;
        }
        if (startD >= stopD) {
            return false;
        }
 
        return native_getSegment(native_instance, startD, stopD, dst.mutateNI(), startWithMoveTo);
    }
 
    /**
     * 用来判断Path是否闭合，但是如果你在关联Path的时候设置了forceClosed在true的话，这个方法的返回值则一定为true
     */
    public boolean isClosed() {
        return native_isClosed(native_instance);
    }
 
    /**
     * Path是可以由多条曲线构成的，但不论是getLength,getSegment或者是其它的方法，都只会在其中的第一条线段上运行，
     * 而这个nextContour就是用于跳转到下一条曲线的方法，如果跳转成功则返回true，如果跳转失败则返回false
     */
    public boolean nextContour() {
        return native_nextContour(native_instance);
    }
}
```

理论介绍完毕，来实现一个如下效果：

<img src=".\img\path_9.png" alt="path_9" style="zoom:70%;" />

```java
private void init() {
    paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setDither(true);
    paint.setStrokeWidth(30.0f);
    path = new Path();
    path.moveTo(100, 100);
    path.lineTo(600, 100);
    path.lineTo(600, 600);
    path.lineTo(100, 600);
    path.lineTo(100,100);
    pathMeasure = new PathMeasure(path, true);
}
@Override
protected void onDraw(Canvas canvas) {
    Path dst1 = new Path();
    pathMeasure.getSegment(0, start, dst1, true);
    paint.setColor(Color.YELLOW);
    canvas.drawPath(dst1, paint);
    paint.setColor(Color.BLACK);
    Path dst2 = new Path();
    if (start > pathMeasure.getLength() - 100) {
        pathMeasure.getSegment(start, pathMeasure.getLength(), dst2, true);
        pathMeasure.getSegment(0, 100 - pathMeasure.getLength() + start, dst2, true);
    } else {
        pathMeasure.getSegment(start, start + 100, dst2, true);
        paint.setColor(Color.BLACK);
    }
    canvas.drawPath(dst2, paint);
    Path dst3 = new Path();
    pathMeasure.getSegment(start + 100, pathMeasure.getLength(), dst3, true);
    paint.setColor(Color.YELLOW);
    canvas.drawPath(dst3, paint);
    getHandler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: start = " + start);
            start += 10;
            if (start >= pathMeasure.getLength()) {
                start = start - pathMeasure.getLength();
            }
            invalidate();
        }
    }, 10);
}
```

<img src=".\img\path_10.png" alt="path_10" style="zoom:70%;" />

几行代码就搞定了一个动画效果，是不是很简单，为了更好的蛆了解`getPostTan`和`getMatrix`方法，给出如下效果：

```java
private void init() {
    paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setDither(true);
    //缩小图片
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 2;
    bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher,options);
}
@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.translate(getWidth()/2,getHeight()/2);
    path.reset();
    path.addCircle(0,0,200,Path.Direction.CW);
    canvas.drawPath(path,paint);
    mFloat+=0.005;
    if (mFloat>=1){
        mFloat=0;
    }
    PathMeasure pathMeasure=new PathMeasure(path,false);
    pathMeasure.getPosTan(mFloat*pathMeasure.getLength(), pos,tan);
    //计算出当前的切线与x轴的夹角的度数
    double degress=Math.atan2(tan[1],tan[0])*180/Math.PI;
    mMatrix.reset();
    //进行角度旋转
    mMatrix.postRotate((float) degress,bitmap.getWidth()/2,bitmap.getHeight()/2);
    //将图片的绘制点中心与当前点重合
    mMatrix.postTranslate(pos[0]-bitmap.getWidth()/2,pos[1]-bitmap.getHeight()/2);
    canvas.drawBitmap(bitmap,mMatrix,paint);
    invalidate();
}
```

**tips:**

1. `Math.atan2()`：预支比较的是`Math.atan()`，`Math.atan`的范围是-pi/2~pi/2之间，`Math.atan2()`是-pi~pi之间，的到的是弧度，需要进一步转化为角度：

   <img src=".\img\path_11.png" alt="path_11" style="zoom:100%;" />

