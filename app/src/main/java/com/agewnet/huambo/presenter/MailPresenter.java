package com.agewnet.huambo.presenter;

import android.text.TextUtils;
import android.view.View;

import com.agewnet.huambo.contract.LoginContract;
import com.agewnet.huambo.contract.MailContract;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.MailBean;
import com.agewnet.huambo.entity.ResponseLoginBean;
import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.http.RequestApi;
import com.agewnet.huambo.http.RequestListener;
import com.agewnet.huambo.http.ResponesEntity;
import com.agewnet.huambo.ui.VPNLoginActivity;
import com.agewnet.huambo.util.ToolLOG;
import com.agewnet.huambo.util.ToolToast;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Dumpling on 2017/10/19.
 */

public class MailPresenter extends MailContract.Presenter {

    /**
     * 获取联系人列表
     *
     * @param param
     */
    @Override
    public void getMailList(String param) {
        getHttpClient().setRequestUrl(String.format(RequestApi.HUAMBO_MAILLIST, param)).setResponseConver(new TypeToken<List<MailBean>>() {
        }.getType()).sendRequest(new RequestListener() {
            @Override
            public void Success(ResponesEntity responesEntity) {
                List<MailBean> mailBeanList = (List<MailBean>) responesEntity.getData();
                if (null != mailBeanList) {
                    ((MailContract.View) getView()).onSuccess(mailBeanList);

                } else {
                    ((MailContract.View) getView()).onFailure(responesEntity.getMessage());
                }


            }

            @Override
            public void error(String errorMessage) {
                ((MailContract.View) getView()).onFailure(errorMessage);
            }
        });
    }
}
