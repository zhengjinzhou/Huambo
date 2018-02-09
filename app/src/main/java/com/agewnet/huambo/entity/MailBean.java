package com.agewnet.huambo.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.agewnet.huambo.BR;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dumpling on 2017/10/24.
 */

public class MailBean extends BaseObservable implements Parcelable {


    /**
     * KSName : 局领导
     * UserList : [{"UserName":"江善东","MobileNumber":"13602315259","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"依林","MobileNumber":"","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"孔方彬","MobileNumber":"","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"梁富民","MobileNumber":"","MobileNumber2":"13580753393","OfficeNumber":"22023986","HomeNumber":"","ShortNumber":"667432","Email":"","Address":""},{"UserName":"王莉莉","MobileNumber":"","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"领导","MobileNumber":"","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"聂江尧","MobileNumber":"13763184969","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"钟志豪","MobileNumber":"","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"彭浩峻","MobileNumber":"","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""},{"UserName":"郭嘉欣","MobileNumber":"","MobileNumber2":"","OfficeNumber":"","HomeNumber":"","ShortNumber":"","Email":"","Address":""}]
     */

    private String KSName;
    private List<UserListBean> UserList;

    @Bindable
    public String getKSName() {
        return KSName;
    }

    public void setKSName(String KSName) {
        this.KSName = KSName;
        notifyPropertyChanged(BR.kSName);
    }

    @Bindable
    public List<UserListBean> getUserList() {
        return UserList;
    }

    public void setUserList(List<UserListBean> UserList) {
        this.UserList = UserList;
        notifyPropertyChanged(BR.userList);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.KSName);
        dest.writeList(this.UserList);
    }

    public MailBean() {
    }

    protected MailBean(Parcel in) {
        this.KSName = in.readString();
        this.UserList = new ArrayList<UserListBean>();
        in.readList(this.UserList, UserListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MailBean> CREATOR = new Parcelable.Creator<MailBean>() {
        @Override
        public MailBean createFromParcel(Parcel source) {
            return new MailBean(source);
        }

        @Override
        public MailBean[] newArray(int size) {
            return new MailBean[size];
        }
    };
}
