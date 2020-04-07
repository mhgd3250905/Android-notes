package com.example.recyclerviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
