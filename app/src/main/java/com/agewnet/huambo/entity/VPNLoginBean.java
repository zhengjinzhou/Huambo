package com.agewnet.huambo.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.agewnet.huambo.BR;
import com.agewnet.huambo.util.ToolToast;

/**
 * Created by Dumpling on 2017/10/19.
 */

public class VPNLoginBean extends BaseObservable {
    private String vpnAccount;
    private String vpnPassWord;
    private boolean isEnable;
    private boolean noEnable;
    private boolean passWordIsVisible;

    @Bindable
    public boolean isPassWordIsVisible() {
        return passWordIsVisible;
    }

    public void setPassWordIsVisible(boolean passWordIsVisible) {
        this.passWordIsVisible = passWordIsVisible;
        notifyPropertyChanged(BR.passWordIsVisible);
    }

    @Bindable
    public boolean isNoEnable() {
        return noEnable;
    }

    public void setNoEnable(boolean noEnable) {


        if (noEnable) {
            setEnable(false);
        }
        this.noEnable = noEnable;
        notifyPropertyChanged(BR.noEnable);
    }

    @Bindable
    public String getVpnAccount() {
        return vpnAccount;
    }

    public void setVpnAccount(String vpnAccount) {
        this.vpnAccount = vpnAccount;
        notifyPropertyChanged(BR.vpnAccount);
    }

    @Bindable
    public String getVpnPassWord() {
        return vpnPassWord;
    }

    public void setVpnPassWord(String vpnPassWord) {
        this.vpnPassWord = vpnPassWord;
        notifyPropertyChanged(BR.vpnPassWord);
    }

    @Bindable
    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        if (enable) {
            setNoEnable(false);
        }
        isEnable = enable;
        notifyPropertyChanged(BR.enable);

    }

    @Override
    public String toString() {
        return "VPNLoginBean{" +
                "vpnAccount='" + vpnAccount + '\'' +
                ", vpnPassWord='" + vpnPassWord + '\'' +
                ", isEnable=" + isEnable +
                ", noEnable=" + noEnable +
                ", passWordIsVisible=" + passWordIsVisible +
                '}';
    }
}
