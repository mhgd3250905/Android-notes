package com.example.recyclerviewdemo;


import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CardItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public interface OnItemTouchCallbackListener{
        //item被滑动的回调方法
        void onSwiped(int position,int direction);
    }

    private OnItemTouchCallbackListener mListener;

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//        if (layoutManager instanceof CardLayoutManager) {
            //允许上下滑动
            swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                    | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (mListener!=null){
            mListener.onSwiped(viewHolder.getBindingAdapterPosition(),direction);
        }
    }


    //拖动itemView时对部分itemView施加动画效果
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        float trans;
        //以偏移量大的方向为标准
        if (Math.abs(dX)>Math.abs(dY)){
            trans=Math.abs(dX);
        }else {
            trans=Math.abs(dY);
        }
        //滑动比例
        float ratio=trans/getThreshold(recyclerView,viewHolder);
        if (ratio>1){
            ratio=1;
        }
        //获取itemCount总量
        int itemCount=recyclerView.getChildCount();
        
//        //移除时为底部显示的view增加动画
//        for (int i = 0; i < ; i++) {
//
//        }

        //为被拖动的View增加透明度动画
        View view = recyclerView.getChildAt(itemCount-1);

        view.setAlpha(1 - Math.abs(ratio) * 0.2f);
    }

    private float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        return recyclerView.getWidth()*getSwipeThreshold(viewHolder);
    }

    public void setmListener(OnItemTouchCallbackListener mListener) {
        this.mListener = mListener;
    }
}
