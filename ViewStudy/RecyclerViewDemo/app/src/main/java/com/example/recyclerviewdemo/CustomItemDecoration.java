package com.example.recyclerviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDividerDrawable;
    private final int mDivider;
    private final Drawable mShadowDrawable;

    public CustomItemDecoration(Context context) {
        mDividerDrawable = ContextCompat.getDrawable(context, R.drawable.divider);
        mShadowDrawable = ContextCompat.getDrawable(context, R.drawable.shadow);
        mDivider = mDividerDrawable.getIntrinsicHeight();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //绘制分割线
        final int left = parent.getPaddingLeft();
        final int righr = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDivider;
            mDividerDrawable.setBounds(left, top, righr, bottom);
            mDividerDrawable.draw(c);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //绘制阴影
//        final int left = parent.getPaddingLeft();
//        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
//        final int count = parent.getChildCount();
//        for (int i = 0; i < count; i++) {
//            View child = parent.getChildAt(i);
//            final int top = child.getTop();
//            final int bottom = child.getBottom();
//            mShadowDrawable.setBounds(left, top, right, bottom);
//            mShadowDrawable.draw(c);
//        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        //设置间距
        int layoutPosition = parent.getChildViewHolder(view).getBindingAdapterPosition();
        if (layoutPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, mDivider);
        }
    }
}
