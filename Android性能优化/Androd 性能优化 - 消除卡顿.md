# 性能优化 - 消除卡顿

#### 1.获取屏幕刷新帧率
```java
Display display = getWindowManager().getDefaultDisplay();
float refreshRate = display.getRefreshRate();
```

**Android系统没隔16ms发出VSYNC信号，出发对UI进行渲染，每次渲染都成功，就能都达到流畅的画面所需要的60fps**

#### 2.通用优化流程

##### 2.1 UI层优化

参见[Android 过度绘制](./Android 过度绘制.md)

##### 2.2 代码问题查找

工具：Lint

参见问题：我们重点关注Performance和Xml中的一些建议

- 在绘制时实例化对象（onDraw）
- 手机不能进入休眠状态（Wake lock）
- 资源忘记回收
- Handler使用不当导致内存泄漏
- 没有使用SparseArray代替HashMap
- 未使用的资源
- 布局中无用的参数
- 可优化的布局（如Image与TextView的组合是否可以使用TextView独立完成）
- 效率低下的无用命名空间等

