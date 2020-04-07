package com.example.recyclerviewdemo;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public class CardBean {
    private @DrawableRes int imgRes;
    private String title;
    private String desc;
    private @ColorRes
    int bgColor;

    public CardBean(int imgRes, String title, String desc,int bgColor) {
        this.imgRes = imgRes;
        this.title = title;
        this.desc = desc;
        this.bgColor=bgColor;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
