package com.example.recyclerviewdemo;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private Context context;
    private List<CardBean> dataList;

    public CardAdapter(Context context, List<CardBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_card_item,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardBean bean = dataList.get(position);
        holder.itemView.setBackgroundResource(bean.getBgColor());
        holder.tvTitle.setText(bean.getTitle());
        Glide.with(context).load(bean.getImgRes()).into(holder.ivContent);
        holder.tvDesc.setText(bean.getDesc());
    }

    private ScaleInAnimation mSelectAnimation = new ScaleInAnimation();

//    @Override
//    public void onViewAttachedToWindow(CardViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
//        addAnimation(holder);
//    }
//
//    private void addAnimation(CardViewHolder holder) {
//        for (Animator anim : mSelectAnimation.getAnimators(holder.itemView)) {
//            anim.setDuration(300).start();
//            anim.setInterpolator(new LinearInterpolator());
//        }
//    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
