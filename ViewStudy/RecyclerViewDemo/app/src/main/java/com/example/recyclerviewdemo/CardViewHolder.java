package com.example.recyclerviewdemo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTitle;
    public TextView tvDesc;
    public ImageView ivContent;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        tvTitle=itemView.findViewById(R.id.tv_title);
        ivContent=itemView.findViewById(R.id.iv_content);
        tvDesc=itemView.findViewById(R.id.tv_desc);
    }
}
