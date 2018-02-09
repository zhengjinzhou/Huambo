package com.agewnet.huambo.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.agewnet.huambo.BR;

/**
 * Created by Dumpling on 2017/10/26.
 */

public class UserListBean extends BaseObservable implements Parcelable {



    /**
     * UserName : 江善东
     * MobileNumber : 13602315259
     * MobileNumber2 :
     * OfficeNumber :
     * HomeNumber :
     * ShortNumber :
     * Email :
     * Address :
     */

    private String UserName;
    private String MobileNumber;
    private String MobileNumber2;
    private String OfficeNumber;
    private String HomeNumber;
    private String ShortNumber;
    private String Email;
    private String Address;

    @Bindable
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
        notifyPropertyChanged(BR.userName);
    }

    @Bindable
    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
        notifyPropertyChanged(BR.mobileNumber);
    }

    @Bindable
    public String getMobileNumber2() {
        return MobileNumber2;
    }

    public void setMobileNumber2(String MobileNumber2) {
        this.MobileNumber2 = MobileNumber2;
        notifyPropertyChanged(BR.mobileNumber2);
    }

    @Bindable
    public String getOfficeNumber() {
        return OfficeNumber;
    }

    public void setOfficeNumber(String OfficeNumber) {
        this.OfficeNumber = OfficeNumber;
        notifyPropertyChanged(BR.officeNumber);
    }

    @Bindable
    public String getHomeNumber() {
        return HomeNumber;
    }

    public void setHomeNumber(String HomeNumber) {
        this.HomeNumber = HomeNumber;
        notifyPropertyChanged(BR.homeNumber);
    }

    @Bindable
    public String getShortNumber() {
        return ShortNumber;
    }

    public void setShortNumber(String ShortNumber) {
        this.ShortNumber = ShortNumber;
        notifyPropertyChanged(BR.shortNumber);
    }

    @Bindable
    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
        notifyPropertyChanged(BR.address);

    }

    @Override
    public String toString() {
        return "UserListBean{" +
                "UserName='" + UserName + '\'' +
                ", MobileNumber='" + MobileNumber + '\'' +
                ", MobileNumber2='" + MobileNumber2 + '\'' +
                ", OfficeNumber='" + OfficeNumber + '\'' +
                ", HomeNumber='" + HomeNumber + '\'' +
                ", ShortNumber='" + ShortNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserName);
        dest.writeString(this.MobileNumber);
        dest.writeString(this.MobileNumber2);
        dest.writeString(this.OfficeNumber);
        dest.writeString(this.HomeNumber);
        dest.writeString(this.ShortNumber);
        dest.writeString(this.Email);
        dest.writeString(this.Address);
    }

    public UserListBean() {
    }

    protected UserListBean(Parcel in) {
        this.UserName = in.readString();
        this.MobileNumber = in.readString();
        this.MobileNumber2 = in.readString();
        this.OfficeNumber = in.readString();
        this.HomeNumber = in.readString();
        this.ShortNumber = in.readString();
        this.Email = in.readString();
        this.Address = in.readString();
    }

    public static final Parcelable.Creator<UserListBean> CREATOR = new Parcelable.Creator<UserListBean>() {
        @Override
        public UserListBean createFromParcel(Parcel source) {
            return new UserListBean(source);
        }

        @Override
        public UserListBean[] newArray(int size) {
            return new UserListBean[size];
        }
    };
}
