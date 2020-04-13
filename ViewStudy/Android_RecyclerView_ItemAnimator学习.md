## RecyclerView ItemAnimator 学习

### 概述

在`RecyclerView`中，通过设置`ItemAnimator`来给条目的增删改添加动画效果。

```java
//默认的动画效果
rvCard.setItemAnimator(new DefaultItemAnimator());
```

### 1. `SimpleItemAnimator`

既然官方提供了默认的动画，所以就从默认动画入手:`DefaultItemAnimator`继承自`SimpleItemAnimator`；

这里新建一个`ItemAnimator`也继承`SimpleItemAnimator`，并实现其中的方法，加上日志打印：

```java
public class CustomItemAnimator extends SimpleItemAnimator {
    private static final String TAG = "CustomItemAnimator";

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "animateRemove() called  holder.pos= ["+holder.getBindingAdapterPosition()+"]");
        return false;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "animateAdd() called  holder.pos= ["+holder.getBindingAdapterPosition()+"]");
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.d(TAG, "animateMove() called, holder.pos= [" + holder.getBindingAdapterPosition() + "] fromX = [" + fromX + "], fromY = [" + fromY + "], toX = [" + toX + "], toY = [" + toY + "]");
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        Log.d(TAG, "animateChange() called, holder.pos= [" + newHolder.getBindingAdapterPosition() + "],  holder.pos= [" + oldHolder.getBindingAdapterPosition() + "] fromLeft = [" + fromLeft + "], fromTop = [" + fromTop + "], toLeft = [" + toLeft + "], toTop = [" + toTop + "]");
        return false;
    }

    @Override
    public void runPendingAnimations() {
        Log.d(TAG, "runPendingAnimations() called");
    }

    @Override
    public void endAnimation(@NonNull RecyclerView.ViewHolder item) {
        Log.d(TAG, "endAnimation() called with: item = [" + item + "]");
    }

    @Override
    public void endAnimations() {
        Log.d(TAG, "endAnimations() called");
    }

    @Override
    public boolean isRunning() {
//        Log.d(TAG, "isRunning() called");
        return false;
    }
}
```

设置给RecyclerView，运行，分别执行增删改动作，查看日志。

首先查看主界面

<img src=".\img\rv_itemanimator_1.png" alt="rv_itemanimator_1" style="zoom:50%;" />

- 新增条目到1位置

  ![rv_itemanimator_2](.\img\rv_itemanimator_2.png)

  可以看到，在新增条目的时候，调用了`animateAdd()`方法，同时调用了两次`animateMove()`方法。

  通过打印的坐标信息，结合界面的显示可以发现，条目2条目3分别调用了`animateMove()`方法，而且他们的动作都是向下移动788高度，这也就是条目的高度，即界面上可见的条目2条目3向下腾出一个条目的位置出来，供新增的条目1来显示。

  将`animateAdd()`返回值设置为true，查看日志

  ![rv_itemanimator_3](.\img\rv_itemanimator_3.png)

  可以发现多调用了`runPendingAnimations()`方法，通过查看`SimpleItemAnimator`中的`animateAdd()`方法描述可以知晓，这里的机制是当返回值为true的时候就会调用`runPendingAnimations()`方法，在这个方法中进行相应的动画操作。

  不仅如此，`animateAdd` `animateMove` `animateRemove` `animateChange`都遵循这个机制，当产生对应的动作的时候，可以在`runPendingAnimations()`中依次处理动画操作。
  
- 删除条目1
  
  `animateRemove`返回值为false
  
  ![rv_itemanimator_4](.\img\rv_itemanimator_4.png)
  
  `animateRemove`返回值为true
  
  ![rv_itemanimator_5](.\img\rv_itemanimator_5.png)
  
  根据方法调用打印的日志可以发现，删除的时候调用的`animateMove()`方法中的移动就是条目1删除之后，下面的两个条目向上挪动的移动。

- 更改条目

  和上面类似，不再赘述。



### 2. `DefaultItemAnimator`

现在分析一下官方提供的`DefaultItemAnimator`。

- 添加条目

  **`animateAdd()`方法**

  ```java
  @Override
  public boolean animateAdd(final RecyclerView.ViewHolder holder) {
      Log.d(TAG, "animateAdd() called");
      //清除重置所有的动画动作
      resetAnimation(holder);
      //将当前这个条目透明度设置为0
      holder.itemView.setAlpha(0);
      /**
       *  mPendingAdditions: 这个集合用来保存在这个方法中设置为透明度0的条目
       *  在runPendingAnimations方法中,会遍历这些条目，并执行对应的动画
       */
      mPendingAdditions.add(holder);
      return true;
  }
  ```

  可以看到这里显示清除有关这个条目的所有动画，然后将其透明度设置为0，然后将这个holder加入到了`mPendingAdditions`列表中，并且返回值为true，也就是说后续会调用方法`runPendingAnimations()`;

  

  **首先看一下`resetAnimation`方法**

  ```java
  private void resetAnimation(RecyclerView.ViewHolder holder) {
      if (sDefaultInterpolator == null) {
          sDefaultInterpolator = new ValueAnimator().getInterpolator();
      }
      holder.itemView.animate().setInterpolator(sDefaultInterpolator);
      endAnimation(holder);
  }
  ```

  可以看到，在这里给所有条目都添加了一个相同的插值器，然后调用`endAnimation`方法，顾名思义，就是结束条目的动画；

  

  **继续看一下`endAnimation`方法**

  ```java
/**
   * 判断当前关闭item先关的change动画是否是必要的
   * @param changeInfo change的待执行动画信息
   * @param item 当前的条目
   * @return 
   */
  private boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, RecyclerView.ViewHolder item) {
      boolean oldItem = false;
      /**
       * 如果这个条目是change中的新条目
       * 那么就直接把这个条目设置为最终形态
       * 并且作为最终的显示
       * 
       * 如果这个条目是change中的旧条目
       * 那么也要将这个条目是指为最终形态
       * 因为后续的复用中需要使用
       */
      if (changeInfo.newHolder == item) {
          changeInfo.newHolder = null;
      } else if (changeInfo.oldHolder == item) {
          changeInfo.oldHolder = null;
          oldItem = true;
      } else {
          return false;
      }
      item.itemView.setAlpha(1);
      item.itemView.setTranslationX(0);
      item.itemView.setTranslationY(0);
      //结束change中旧条目的调用
      dispatchChangeFinished(item, oldItem);
      return true;
  }
  @Override
  public void endAnimation(RecyclerView.ViewHolder item) {
      final View view = item.itemView;
      /**
       * 调用cancel的时候，会触发对动画设置的监听
       * 从这里可以推测，对于view的动画，都使用ViewPropertyAnimator补间动画实现，
       * 否则调用其cancel无法触发回调;
       *
       * 而在view的cancel回调中，均设置其状态为最终完成状态
       */
      view.animate().cancel();
      /**
       * mPendingMoves 中保存的是需要移动的条目动画
       * 譬如remove动画时本已经上滑但是被强制下滑到原始位置的条目动画
       * 通常在{@link StudyDefaultItemAnimator#runPendingAnimations()} 中执行动画后就会清除
       * 但是如果动画还没有执行，mPendingMoves 还没有清空，那么就直接将这些需要复原的条目进行复原
       */
      for (int i = mPendingMoves.size() - 1; i >= 0; i--) {
          MoveInfo moveInfo = mPendingMoves.get(i);
          if (moveInfo.holder == item) {
              view.setTranslationY(0);
              view.setTranslationX(0);
              dispatchMoveFinished(item);
              mPendingMoves.remove(i);
          }
      }
      /**
       * 清除change方法中产生的需要执行的动画集合
       */
      endChangeAnimation(mPendingChanges, item);
      /**
       *  mPendingRemovals这个集合是待执行的删除的条目的集合
       *  删除之后还需要将其状态设置为初始状态
       *  而初始状态和设置的删除动画有关
       *  譬如删除动画是将alpha设置为0，那么这里就要还原为1
       */
      if (mPendingRemovals.remove(item)) {
          view.setAlpha(1);
          dispatchRemoveFinished(item);
      }
      /**
       * 清除待执行的新增动画
       */
      if (mPendingAdditions.remove(item)) {
          view.setAlpha(1);
          dispatchAddFinished(item);
      }
     	/**
     	 * 清除待执行的change动画
     	 */
      for (int i = mChangesList.size() - 1; i >= 0; i--) {
          ArrayList<ChangeInfo> changes = mChangesList.get(i);
          endChangeAnimation(changes, item);
          if (changes.isEmpty()) {
              mChangesList.remove(i);
          }
      }
      /**
       * 清除待执行的移动动画
       */
      for (int i = mMovesList.size() - 1; i >= 0; i--) {
          ArrayList<MoveInfo> moves = mMovesList.get(i);
          for (int j = moves.size() - 1; j >= 0; j--) {
              MoveInfo moveInfo = moves.get(j);
              if (moveInfo.holder == item) {
                  view.setTranslationY(0);
                  view.setTranslationX(0);
                  dispatchMoveFinished(item);
                  moves.remove(j);
                  if (moves.isEmpty()) {
                      mMovesList.remove(i);
                  }
                  break;
              }
          }
      }
      /**
       * 清除待执行的新增动画
       */
      for (int i = mAdditionsList.size() - 1; i >= 0; i--) {
          ArrayList<RecyclerView.ViewHolder> additions = mAdditionsList.get(i);
          if (additions.remove(item)) {
              view.setAlpha(1);
              dispatchAddFinished(item);
              if (additions.isEmpty()) {
                  mAdditionsList.remove(i);
              }
          }
      }
      
      ...
          
      dispatchFinishedWhenDone();
  }
  ```
  
  其实内容很简单就是清除那些待执行的动画。
  
  **接下来看一下`animateMove`方法**
  
  这个方法表示的是当每一个动画执行的时候，相关的条目发生移动的时候（譬如add时条目下移腾出新条目的控件，remove时删除条目下方的条目上移补足空缺）：
  
  ```java
  @Override
  public boolean animateMove(final RecyclerView.ViewHolder holder, int fromX, int fromY,
                             int toX, int toY) {
      Log.d(TAG, "animateMove() called holder pos： " + holder.getBindingAdapterPosition());
      final View view = holder.itemView;
      fromX += (int) holder.itemView.getTranslationX();
      fromY += (int) holder.itemView.getTranslationY();
      resetAnimation(holder);
      int deltaX = toX - fromX;
      int deltaY = toY - fromY;
      if (deltaX == 0 && deltaY == 0) {
          dispatchMoveFinished(holder);
          return false;
      }
      if (deltaX != 0) {
          view.setTranslationX(-deltaX);
      }
      if (deltaY != 0) {
          view.setTranslationY(-deltaY);
      }
      mPendingMoves.add(new MoveInfo(holder, fromX, fromY, toX, toY));
      return true;
  }
  ```
  
  在这个方法中，通过`view.setTranslationX(-deltaX)`和`view.setTranslationY(-deltaY)`将这些本该要移动的条目强制回退到原位，然后把这些移动的信息保存在mPendingMoves列表中，留待`runPendingAnimations`方法中执行；
  
  
  
  **接下来看一下`runPendingAnimations`方法**
  
  ```java
  /**
   * 具体执行动画的方法
   */
  @Override
  public void runPendingAnimations() {
      Log.d(TAG, "runPendingAnimations() called");
      /**
       * 判断当前是否有延迟执行的动画
       */
      boolean removalsPending = !mPendingRemovals.isEmpty();
      /**
       * 当前是否有待执行的条目移动动画
       */
      boolean movesPending = !mPendingMoves.isEmpty();
      boolean changesPending = !mPendingChanges.isEmpty();
      //通过待执行动画是否为空来判断当前是否需要执行add相关的动画
      boolean additionsPending = !mPendingAdditions.isEmpty();
      if (!removalsPending && !movesPending && !additionsPending && !changesPending) {
          // nothing to animate
          return;
      }
      
      ...
      
      //执行条目移动动画
      if (movesPending) {
          final ArrayList<MoveInfo> moves = new ArrayList<>();
          moves.addAll(mPendingMoves);
          mMovesList.add(moves);
          mPendingMoves.clear();
          Runnable mover = new Runnable() {
              @Override
              public void run() {
                  for (MoveInfo moveInfo : moves) {
                      animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY,
                              moveInfo.toX, moveInfo.toY);
                  }
                  moves.clear();
                  mMovesList.remove(moves);
              }
          };
          //如果这个移动是删除相关的，那么就先等删除动画结束后再执行（这里的动画是alpha由1到0）
          if (removalsPending) {
              View view = moves.get(0).holder.itemView;
              ViewCompat.postOnAnimationDelayed(view, mover, getRemoveDuration());
          } else {
              mover.run();
          }
      }
      
  	...
          
      // 处理新增动画
      if (additionsPending) {
          final ArrayList<RecyclerView.ViewHolder> additions = new ArrayList<>();
          additions.addAll(mPendingAdditions);
          mAdditionsList.add(additions);
          mPendingAdditions.clear();
          Runnable adder = new Runnable() {
              @Override
              public void run() {
                  for (RecyclerView.ViewHolder holder : additions) {
                      animateAddImpl(holder);
                  }
                  additions.clear();
                  mAdditionsList.remove(additions);
              }
          };
          //如果当前还有诸如删除更新相关的动画，那么就等待其执行完了再执行新增动画
          if (removalsPending || movesPending || changesPending) {
              long removeDuration = removalsPending ? getRemoveDuration() : 0;
              long moveDuration = movesPending ? getMoveDuration() : 0;
              long changeDuration = changesPending ? getChangeDuration() : 0;
              long totalDelay = removeDuration + Math.max(moveDuration, changeDuration);
              View view = additions.get(0).itemView;
              ViewCompat.postOnAnimationDelayed(view, adder, totalDelay);
          } else {
              adder.run();
          }
      }
  }
  ```
  
  这里仅仅保留了和add相关的部分，首先判断当前是否有待执行的动画，这里首先处理的move动画，也就是add过程中，那些需要向下移动的条目动画，然后执行add动画，也就是透明度由0到1。可以发现，在所有的的动画中add动画的优先级是最低的，如果有删除移动更新动画，就先delay，然后在执行；
  
- 删除条目和新增条目差不多；
  
- 修改条目
  
  **`animateChange()`方法**
  
  ```java
  @Override
  public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
                               int fromX, int fromY, int toX, int toY) {
      if (oldHolder == newHolder) {
          //当条目复用的时候
          //当条目执行位置变化动画的时候
          return animateMove(oldHolder, fromX, fromY, toX, toY);
      }
      //记录旧条目的位置，透明度
      final float prevTranslationX = oldHolder.itemView.getTranslationX();
      final float prevTranslationY = oldHolder.itemView.getTranslationY();
      final float prevAlpha = oldHolder.itemView.getAlpha();
      resetAnimation(oldHolder);
      int deltaX = (int) (toX - fromX - prevTranslationX);
      int deltaY = (int) (toY - fromY - prevTranslationY);
      // 恢复旧条目的位置透明度（这也是动画结束之后应该恢复的状态）
      oldHolder.itemView.setTranslationX(prevTranslationX);
      oldHolder.itemView.setTranslationY(prevTranslationY);
      oldHolder.itemView.setAlpha(prevAlpha);
      if (newHolder != null) {
          // 设置新条目的初始位置
          resetAnimation(newHolder);
          newHolder.itemView.setTranslationX(-deltaX);
          newHolder.itemView.setTranslationY(-deltaY);
          newHolder.itemView.setAlpha(1f);
      }
      mPendingChanges.add(new ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY));
      return true;
  }
  ```
  
  这里其实就是记录旧条目的状态以及设置新条目的初始状态
  
  **继续看change在`runPendingAnimations`的具体执行方法**
  
  ```java
  void animateChangeImpl(final ChangeInfo changeInfo) {
      final RecyclerView.ViewHolder holder = changeInfo.oldHolder;
      final View view = holder == null ? null : holder.itemView;
      final RecyclerView.ViewHolder newHolder = changeInfo.newHolder;
      final View newView = newHolder != null ? newHolder.itemView : null;
      if (view != null) {
          final ViewPropertyAnimator oldViewAnim = view.animate().setDuration(
                  getChangeDuration());
          mChangeAnimations.add(changeInfo.oldHolder);
          oldViewAnim.translationX(changeInfo.toX - changeInfo.fromX);
          oldViewAnim.translationY(changeInfo.toY - changeInfo.fromY);
          oldViewAnim.alpha(1f).setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animator) {
                  dispatchChangeStarting(changeInfo.oldHolder, true);
              }
              @Override
              public void onAnimationEnd(Animator animator) {
                  oldViewAnim.setListener(null);
                  view.setAlpha(1);
                  view.setTranslationX(0);
                  view.setTranslationY(0);
                  dispatchChangeFinished(changeInfo.oldHolder, true);
                  mChangeAnimations.remove(changeInfo.oldHolder);
                  dispatchFinishedWhenDone();
              }
          }).start();
      }
      if (newView != null) {
          final ViewPropertyAnimator newViewAnimation = newView.animate();
          mChangeAnimations.add(changeInfo.newHolder);
          newViewAnimation.translationX(0).translationY(0).setDuration(getChangeDuration())
                  .alpha(1).setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animator) {
                  dispatchChangeStarting(changeInfo.newHolder, false);
              }
              @Override
              public void onAnimationEnd(Animator animator) {
                  newViewAnimation.setListener(null);
                  newView.setAlpha(1);
                  newView.setTranslationX(0);
                  newView.setTranslationY(0);
                  dispatchChangeFinished(changeInfo.newHolder, false);
                  mChangeAnimations.remove(changeInfo.newHolder);
                  dispatchFinishedWhenDone();
              }
          }).start();
      }
  }
  ```
  
  机构分明，就是分别执行新旧条目的动画。
  
  
  
  **代码就看到这里，后面就可以仿照`DefaultItemAnimator`进行自定义了**
  
  
  
### 3. 自定义条目动画

   - 自定义Add动画

     将Add动画自定义为由右向左滑动加入，那么修改`animateAdd`方法：

     ```java
     @Override
     public boolean animateAdd(final RecyclerView.ViewHolder holder) {
         Log.d(TAG, "animateAdd() called");
         //清除重置所有的动画动作
         resetAnimation(holder);
         /**
          * 新条目右右向左进入,那么先把item放在右侧
          */
         holder.itemView.setTranslationX(holder.itemView.getMeasuredWidth());
         /**
          *  mPendingAdditions: 这个集合用来保存在这个方法中设置为透明度0的条目
          *  在runPendingAnimations方法中,会遍历这些条目，并执行对应的动画
          */
         mPendingAdditions.add(holder);
         return true;
     }
     ```

     先把新条目放置在右侧，同时也修改后续的动画执行部分`animateAddImpl`：

     ```java
     /**
      * 执行新增动画
      *
      * @param holder 执行动画的条目
      */
     void animateAddImpl(final RecyclerView.ViewHolder holder) {
         final View view = holder.itemView;
         final ViewPropertyAnimator animation = view.animate();
         mAddAnimations.add(holder);
         /**
          * 新条目由右向左移动
          */
         animation.translationX(0).setDuration(getAddDuration())
                 .setListener(new AnimatorListenerAdapter() {
                     @Override
                     public void onAnimationStart(Animator animator) {
                         dispatchAddStarting(holder);
                     }
                     @Override
                     public void onAnimationCancel(Animator animator) {
                         view.setTranslationX(0);
                     }
                     @Override
                     public void onAnimationEnd(Animator animator) {
                         animation.setListener(null);
                         dispatchAddFinished(holder);
                         mAddAnimations.remove(holder);
                         dispatchFinishedWhenDone();
                     }
                 }).start();
     }
     ```

     执行左移动画即可，效果如下：

     <img src=".\img\add.gif" alt="add" style="zoom:67%;" />

- 自定义remove动画

  将删除动画自定义为由左向右离开

  那么修改`animateRemove`方法：

  ```java
  @Override
  public boolean animateRemove(final RecyclerView.ViewHolder holder) {
      Log.d(TAG, "animateRemove() called");
      resetAnimation(holder);
      /**
       * 删除动画设置为由左向右离开
       */
      mPendingRemovals.add(holder);
      return true;
  }
  ```

  对的，不用修改，保持原状，继续修改后续的动画执行部分`animateRemoveImpl`：

  ```java
  private void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
      Log.d(TAG, "animateRemoveImpl() called");
      final View view = holder.itemView;
      final ViewPropertyAnimator animation = view.animate();
      mRemoveAnimations.add(holder);
      animation.setDuration(getRemoveDuration()).translationX(view.getMeasuredWidth()).setListener(
              new AnimatorListenerAdapter() {
                  @Override
                  public void onAnimationStart(Animator animator) {
                      dispatchRemoveStarting(holder);
                  }
                  @Override
                  public void onAnimationEnd(Animator animator) {
                      animation.setListener(null);
                      view.setTranslationX(0);
                      dispatchRemoveFinished(holder);
                      mRemoveAnimations.remove(holder);
                      dispatchFinishedWhenDone();
                  }
              }).start();
  }
  ```

  实现效果如下：

  <img src=".\img\remove.gif" alt="remove" style="zoom:67%;" />
  
- 自定义change动画

  将删除动画自定义为旧条目由右向左离开，新条目由右向左加入

  那么修改`animateChange`方法：

  ```java
  @Override
  public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
                               int fromX, int fromY, int toX, int toY) {
      if (oldHolder == newHolder) {
          //当条目复用的时候
          //当条目执行位置变化动画的时候
          return animateMove(oldHolder, fromX, fromY, toX, toY);
      }
      /**
       * 更新条目动画为旧条目由右向左离开 新条目由右向左进入
       * 所以初始状态下旧条目是在原位，而新条目应该在旧条目右边
     */
      //记录旧条目的位置，透明度
    final float prevTranslationX = oldHolder.itemView.getTranslationX();
      final float prevTranslationY = oldHolder.itemView.getTranslationY();
      final float prevAlpha = oldHolder.itemView.getAlpha();
      resetAnimation(oldHolder);
      int deltaX = (int) (toX - fromX - prevTranslationX);
      int deltaY = (int) (toY - fromY - prevTranslationY);
      // 恢复旧条目的位置透明度（这也是动画结束之后应该恢复的状态）
      oldHolder.itemView.setTranslationX(prevTranslationX);
      oldHolder.itemView.setTranslationY(prevTranslationY);
      oldHolder.itemView.setAlpha(prevAlpha);
      if (newHolder != null) {
          // 设置新条目的初始位置
          resetAnimation(newHolder);
          newHolder.itemView.setTranslationX(-deltaX + oldHolder.itemView.getMeasuredWidth());
          newHolder.itemView.setTranslationY(-deltaY);
          newHolder.itemView.setAlpha(1f);
      }
      mPendingChanges.add(new ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY));
      return true;
  }
  ```
  
  对的，不用修改，保持原状，继续修改后续的动画执行部分`animateChangeImpl`：
  
```java
  void animateChangeImpl(final ChangeInfo changeInfo) {
    final RecyclerView.ViewHolder holder = changeInfo.oldHolder;
      final View view = holder == null ? null : holder.itemView;
      final RecyclerView.ViewHolder newHolder = changeInfo.newHolder;
      final View newView = newHolder != null ? newHolder.itemView : null;
      if (view != null) {
          final ViewPropertyAnimator oldViewAnim = view.animate().setDuration(getChangeDuration());
          mChangeAnimations.add(changeInfo.oldHolder);
          //旧条目向左移动
          oldViewAnim.translationX(changeInfo.toX - changeInfo.fromX - view.getMeasuredWidth());
          oldViewAnim.translationY(changeInfo.toY - changeInfo.fromY);
          oldViewAnim.setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animator) {
                  dispatchChangeStarting(changeInfo.oldHolder, true);
              }
              @Override
              public void onAnimationEnd(Animator animator) {
                  oldViewAnim.setListener(null);
                  view.setAlpha(1);
                  view.setTranslationX(0);
                  view.setTranslationY(0);
                  dispatchChangeFinished(changeInfo.oldHolder, true);
                  mChangeAnimations.remove(changeInfo.oldHolder);
                  dispatchFinishedWhenDone();
              }
          }).start();
      }
      if (newView != null) {
          final ViewPropertyAnimator newViewAnimation = newView.animate();
          mChangeAnimations.add(changeInfo.newHolder);
          newViewAnimation.translationX(0).translationY(0).setDuration(getChangeDuration())
                  .alpha(1).setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationStart(Animator animator) {
                  dispatchChangeStarting(changeInfo.newHolder, false);
              }
              @Override
              public void onAnimationEnd(Animator animator) {
                  newViewAnimation.setListener(null);
                  newView.setAlpha(1);
                  newView.setTranslationX(0);
                  newView.setTranslationY(0);
                  dispatchChangeFinished(changeInfo.newHolder, false);
                  mChangeAnimations.remove(changeInfo.newHolder);
                  dispatchFinishedWhenDone();
              }
          }).start();
      }
  }
```

  实现效果如下：

<img src=".\img\change.gif" alt="change" style="zoom:67%;" />

  



### 4. [轻松愉快，源码在这里！](https://github.com/mhgd3250905/Android-notes/tree/master/ViewStudy/RecyclerViewDemo)