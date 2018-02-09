package com.agewnet.huambo.contract;

import com.agewnet.huambo.base.BasePresenter;
import com.agewnet.huambo.base.BaseView;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.MailBean;
import com.agewnet.huambo.entity.VPNLoginBean;

import java.util.List;

/**
 * Created by Dumpling on 2017/10/19.
 */

public interface MailContract {
    interface View extends BaseView {

        void onSuccess(List<MailBean> list);

        void onFailure(String errorMsg);


    }

    abstract class Presenter extends BasePresenter {
        public abstract void getMailList(String param);


    }
}
