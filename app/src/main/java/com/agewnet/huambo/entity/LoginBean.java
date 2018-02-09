package com.agewnet.huambo.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.agewnet.huambo.BR;
import com.agewnet.huambo.util.ToolToast;

/**
 * Created by Dumpling on 2017/10/19.
 */

public class LoginBean extends BaseObservable {
    private String userName;
    private String userPass;
    private boolean isRememberAccount;
    private boolean isAutoLogin;
    private boolean isVPNLogin;
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
    public boolean isVPNLogin() {
        return isVPNLogin;
    }

    public void setVPNLogin(boolean vPNLogin) {
        isVPNLogin = vPNLogin;
        notifyPropertyChanged(BR.vPNLogin);
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
        notifyPropertyChanged(BR.userPass);
    }

    @Bindable
    public boolean isRememberAccount() {
        return isRememberAccount;
    }

    public void setRememberAccount(boolean rememberAccount) {

        isRememberAccount = rememberAccount;
        if (!isRememberAccount) {
            setAutoLogin(false);
        }
        notifyPropertyChanged(BR.rememberAccount);


    }

    @Bindable
    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {

        isAutoLogin = autoLogin;
        if (isAutoLogin) {
            setRememberAccount(true);
        }
        notifyPropertyChanged(BR.autoLogin);
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "userName='" + userName + '\'' +
                ", userPass='" + userPass + '\'' +
                ", isRememberAccount=" + isRememberAccount +
                ", isAutoLogin=" + isAutoLogin +
                ", isVPNLogin=" + isVPNLogin +
                ", passWordIsVisible=" + passWordIsVisible +
                '}';
    }
}
