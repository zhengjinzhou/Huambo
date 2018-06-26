package com.agewnet.huambo.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.agewnet.huambo.contract.LoginContract;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.ResponseLoginBean;
import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.http.RequestApi;
import com.agewnet.huambo.http.RequestListener;
import com.agewnet.huambo.http.ResponesEntity;
import com.agewnet.huambo.ui.VPNLoginActivity;
import com.agewnet.huambo.util.ToolLOG;
import com.agewnet.huambo.util.ToolToast;
import com.google.gson.reflect.TypeToken;

import net.arraynetworks.vpn.Common;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Dumpling on 2017/10/19.
 *
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void loginNameByPass(LoginBean loginBean) {
        if (TextUtils.isEmpty(loginBean.getUserName())) {
            ToolToast.success("账号有误,请你重新输入");
            return;
        }
        if (TextUtils.isEmpty(loginBean.getUserPass())) {
            ToolToast.success("密码有误,请你重新输入");
            return;
        }
        Log.d("测试", "loginNameByPass: "+loginBean.toString());
        ((LoginContract.View) getView()).clientLogin(loginBean);
    }

    public void loginClient(LoginBean loginBean) {

        getHttpClient().setRequestUrl(String.format(RequestApi.HUAMBO_LOGIN_URL, loginBean.getUserName(), loginBean.getUserPass())).setResponseConver(new TypeToken<ResponseLoginBean>() {
        }.getType()).sendRequest(new RequestListener() {
            @Override
            public void Success(ResponesEntity responesEntity) {
                ResponseLoginBean mResponseLoginBean = (ResponseLoginBean) responesEntity.getData();

                if (null != mResponseLoginBean) {
                    if (mResponseLoginBean.isResult()) {

                        ((LoginContract.View) getView()).onSuccess();
                    } else {
                        ((LoginContract.View) getView()).onFailure(mResponseLoginBean.getMessage());
                    }
                } else {
                    ((LoginContract.View) getView()).onFailure("返回异常,请重试...");
                }
            }

            @Override
            public void error(String errorMessage) {
                ((LoginContract.View) getView()).onFailure(errorMessage);
            }
        });
        /*String url = RequestApi.BASE_URL + String.format(RequestApi.HUAMBO_LOGIN_URL, loginBean.getUserName(), loginBean.getUserPass());
        Log.d("地址", "loginClient: "+url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("", "onFailure: "+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("", "onResponse: "+response.body().string());
                ((LoginContract.View) getView()).onSuccess();
            }
        });*/
    }

    @Override
    public void loginVPN(VPNLoginBean vpnLoginBean) {
        ToolToast.success(vpnLoginBean.toString());
    }

    public void starVPN(View view) {
        VPNLoginActivity.newInstance(view.getContext());
    }

    public void clearAccount(View view) {
        ((LoginContract.View) getView()).clearAccount();
    }

    public void togglePassShowType() {
        ((LoginContract.View) getView()).changePassWord();
    }
}
