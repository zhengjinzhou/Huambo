package com.agewnet.huambo.contract;

import com.agewnet.huambo.base.BasePresenter;
import com.agewnet.huambo.base.BaseView;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.ResponseLoginBean;
import com.agewnet.huambo.entity.VPNLoginBean;

/**
 * Created by Dumpling on 2017/10/19.
 */

public interface LoginContract {
    interface View extends BaseView {
        void clearAccount();

        void changePassWord();

        void onSuccess();

        void onFailure(String errorMsg);

        void vpnLogin();

        void clientLogin(LoginBean loginBean);


    }

    abstract class Presenter extends BasePresenter {
        public abstract void loginNameByPass(LoginBean loginBean);

        public abstract void loginVPN(VPNLoginBean vpnLoginBean);

    }
}
