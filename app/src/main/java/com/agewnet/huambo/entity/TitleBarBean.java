package com.agewnet.huambo.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.agewnet.huambo.BR;


/**
 * Created by Dumpling on 2017/10/17.
 */

public class TitleBarBean extends BaseObservable {

    private String centerText;
    private String leftText;
    private String rightText;
    private boolean leftIsVisible;
    private boolean RightIsVisible;
    private int rightImageResoure;


    @Bindable
    public int getRightImageResoure() {
        return rightImageResoure;
    }

    public void setRightImageResoure(int rightImageResoure) {
        this.rightImageResoure = rightImageResoure;
        notifyPropertyChanged(BR.rightImageResoure);
    }

    @Bindable
    public boolean isLeftIsVisible() {
        return leftIsVisible;
    }

    public void setLeftIsVisible(boolean leftIsVisible) {
        this.leftIsVisible = leftIsVisible;
        notifyPropertyChanged(BR.leftIsVisible);
    }

    @Bindable
    public boolean isRightIsVisible() {
        return RightIsVisible;
    }

    public void setRightIsVisible(boolean rightIsVisible) {
        RightIsVisible = rightIsVisible;
        notifyPropertyChanged(BR.rightIsVisible);
    }

    @Bindable
    public String getCenterText() {
        return centerText;
    }

    public void setCenterText(String centerText) {
        this.centerText = centerText;
        notifyPropertyChanged(BR.centerText);
    }

    @Bindable
    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        notifyPropertyChanged(BR.leftText);
    }

    @Bindable
    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        notifyPropertyChanged(BR.rightText);
    }


}
