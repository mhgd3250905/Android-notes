package com.example.recyclerviewdemo;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class CardLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "CardLayoutManager";


    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        //如果RecyclerView中只有一个Item或者没有，什么都不做
        if (itemCount < 1) {
            return;
        }
        //最底部Item的角标
        int bottomPosition = 0;
        //从最底层的item开始摆放
        for (int i = bottomPosition; i < itemCount; i++) {
            //从缓存池中取出itemView
            View view = recycler.getViewForPosition(i);
            //将itemView添加到RecyclerView
            addView(view);
            //测量itemView
            measureChildWithMargins(view, 0, 0);
            //recyclerView宽度-itemView宽度
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
            //将itemView水平居中
            layoutDecoratedWithMargins(view,
                    widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));
            float scale = 1 - 0.1f * 2;
            float transY = (float) (view.getHeight() * 0.1 * 2);
            //改变view的大小和位置
            if (i >= itemCount - 3) {
                scale = 1 - 0.1f * (itemCount - i - 1);
                transY = (float) (view.getHeight() * 0.1 * (itemCount - i - 1));
            }
            Log.d(TAG, "itemCount: " + itemCount + "\ti: " + i + "\tscale: " + scale);
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setTranslationY(transY);
        }
    }
}
