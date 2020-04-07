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

    List<View> mAnimatorViewList = new ArrayList<>();
    List<MoveInfo> mMoveInfoAnimatorViewList = new ArrayList<>();
    List<RecyclerView.ViewHolder> mRemoveAnimatorViewList = new ArrayList<>();
    List<RecyclerView.ViewHolder> mChangeAnimatorViewList = new ArrayList<>();


    private static class MoveInfo {
        RecyclerView.ViewHolder holder;
        int fromX;
        int fromY;
        int toX;
        int toY;

        public MoveInfo(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "animateRemove()");
        mRemoveAnimatorViewList.add(holder);
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        Log.d(TAG, "animateAdd() ");
        Log.d("tag", "this action is animateAdd");
        holder.itemView.setAlpha(0);
        mAnimatorViewList.add(holder.itemView);
        return true;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        Log.d(TAG, "animateMove()");
        final View view = holder.itemView;
        fromX += view.getTranslationX();
        fromY -= view.getTranslationY();

        endAnimation(holder);
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
        mMoveInfoAnimatorViewList.add(new MoveInfo(holder, fromX, fromY, toX, toY));
        return true;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        Log.d(TAG, "animateChange() called");
        ViewPropertyAnimator oldAnimator = oldHolder.itemView.animate();
        oldAnimator.alpha(0).setDuration(getChangeDuration()).start();
        newHolder.itemView.setAlpha(0);
        mChangeAnimatorViewList.add(newHolder);
        return true;
    }

    @Override
    public void runPendingAnimations() {
        Log.d(TAG, "runPendingAnimations() called");
        for (final View view : mAnimatorViewList) {
            ViewPropertyAnimator animate = view.animate();
            animate.alpha(1f);
            animate.setDuration(1000).start();
        }

        mAnimatorViewList.clear();

        boolean needRemove = mRemoveAnimatorViewList.size() > 0;
        for (final RecyclerView.ViewHolder holder : mRemoveAnimatorViewList) {
            final View view = holder.itemView;

            ViewPropertyAnimator animate = view.animate();
            animate.translationX(-1000);
            animate.setDuration(getRemoveDuration()).start();
        }
        mRemoveAnimatorViewList.clear();

        Runnable moveRunnable = new Runnable() {
            @Override
            public void run() {
                for (MoveInfo moveInfo : mMoveInfoAnimatorViewList) {
                    final RecyclerView.ViewHolder holder = moveInfo.holder;
                    final View view = holder.itemView;
                    final int deltaX = moveInfo.toX - moveInfo.fromX;
                    final int deltaY = moveInfo.toY - moveInfo.fromY;
                    final ViewPropertyAnimator animate = view.animate();
                    if (deltaX != 0) {
                        animate.translationX(0);
                    }
                    if (deltaY != 0) {
                        animate.translationY(0);
                    }
                    animate.setDuration(getMoveDuration())
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    dispatchMoveStarting(holder);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    if (deltaX != 0) {
                                        view.setTranslationX(0);
                                    }
                                    if (deltaY != 0) {
                                        view.setTranslationY(0);
                                    }
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).start();
                }
                mMoveInfoAnimatorViewList.clear();
            }
        };

        if (needRemove && mMoveInfoAnimatorViewList.size() > 0) {
            mMoveInfoAnimatorViewList.get(0).holder.itemView.postDelayed(moveRunnable, getRemoveDuration());
        }

        for (RecyclerView.ViewHolder holder : mChangeAnimatorViewList) {
            ViewPropertyAnimator animate = holder.itemView.animate();
            animate.alpha(1).setDuration(getChangeDuration()).start();
        }

        mChangeAnimatorViewList.clear();
    }

    @Override
    public void endAnimation(@NonNull RecyclerView.ViewHolder item) {
        Log.d(TAG, "endAnimation()");
    }

    @Override
    public void endAnimations() {
        Log.d(TAG, "endAnimations() called");
    }

    @Override
    public boolean isRunning() {
        Log.d(TAG, "isRunning() called");
        return false;
    }
}
