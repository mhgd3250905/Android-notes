## RecyclerView ItemTouchHelper 学习

今天我们来学习一下`RecyclerView`另一个鲜为人知的辅助类--`ItemTouchHelper`。我们在做列表视图，就比如说，`ListView`或者`RecyclerView`，通常会有两种需求：1. 侧滑删除；2. 拖动交换位置。对于第一种需求使用传统的版本实现还比较简单，我们可以自定义`ItemView`来实现；而第二种的话，可能就稍微有一点复杂，可能需要重写`LayoutManager`。
  这些办法也不否认是有效的解决方案，但是是否是简单和低耦合性的办法呢？当然不是，踩过坑的同学应该都知道，不管是自定义`View`还是自定义`LayoutManager`都不是一件简单的事情，其次，自定义`ItemView`导致`Adapter`的通用性降低。这些实现方式都是比较麻烦的。
  而谷歌爸爸真是贴心，知道我们都有这种需求，就小手一抖，随便帮我们实现了一个Helper类，来减轻我们的工作量。这就是`ItemTouchHelper`的作用。

### 1.概述

​		在正式介绍`ItemTouchHelper`之前，我么你先了解一下这究竟是什么东西？

​		从`ItemTouchHelper`源码中可以看出来，`ItemTouchHelper`继承了`ItemDecoration`，根本上就是一个`ItemDecoration`。