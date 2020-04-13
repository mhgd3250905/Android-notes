package com.example.recyclerviewdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

public class CustomItemAnimator extends SimpleItemAnimator {
    private static final String TAG = "CustomItemAnimator";

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "animateRemove() called  holder.pos= ["+holder.getBindingAdapterPosition()+"]");
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "animateAdd() called  holder.pos= ["+holder.getBindingAdapterPosition()+"]");
        return true;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.d(TAG, "animateMove() called, holder.pos= [" + holder.getBindingAdapterPosition() + "] fromX = [" + fromX + "], fromY = [" + fromY + "], toX = [" + toX + "], toY = [" + toY + "]");
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        Log.d(TAG, "animateChange() called, holder.pos= [" + newHolder.getBindingAdapterPosition() + "],  holder.pos= [" + oldHolder.getBindingAdapterPosition() + "] fromLeft = [" + fromLeft + "], fromTop = [" + fromTop + "], toLeft = [" + toLeft + "], toTop = [" + toTop + "]");
        return true;
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
