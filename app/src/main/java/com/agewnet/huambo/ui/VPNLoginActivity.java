package com.agewnet.huambo.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.contract.LoginContract;
import com.agewnet.huambo.databinding.ActivityVpnLoginBinding;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.presenter.LoginPresenter;
import com.agewnet.huambo.util.UserCache;

/**
 * VPN 登录
 */
public class VPNLoginActivity extends AppCompatActivity implements LoginContract.View {

    ActivityVpnLoginBinding mActivityVpnLoginBinding;
    LoginPresenter mLoginPresenter;
    VPNLoginBean mVPNLoginBean;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, VPNLoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityVpnLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_vpn_login);

        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attach(this);
        mVPNLoginBean = new VPNLoginBean();
        VPNLoginBean tempVPNLoginBean = (VPNLoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_VPN_LOGINCACHE, VPNLoginBean.class);
        if (null != tempVPNLoginBean) {
            mVPNLoginBean.setVpnAccount(tempVPNLoginBean.getVpnAccount());
            mVPNLoginBean.setVpnPassWord(tempVPNLoginBean.getVpnPassWord());
            mVPNLoginBean.setEnable(tempVPNLoginBean.isEnable());
            mVPNLoginBean.setNoEnable(tempVPNLoginBean.isNoEnable());
        }
        mActivityVpnLoginBinding.setPresenter(mLoginPresenter);
        mActivityVpnLoginBinding.setVpnLogin(mVPNLoginBean);

        // ======
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mVPNLoginBean) {
                    UserCache.getSingleton(getApplicationContext()).clear();
                    UserCache.getSingleton(getApplicationContext()).putObject(CommonConstant.USER_VPN_LOGINCACHE, mVPNLoginBean);
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (null != mVPNLoginBean) {
            UserCache.getSingleton(this).clear();
            UserCache.getSingleton(this).putObject(CommonConstant.USER_VPN_LOGINCACHE, mVPNLoginBean);
        }
        super.onDestroy();
    }

    /**
     * 清空用户名
     */
    @Override
    public void clearAccount() {
        mVPNLoginBean.setVpnAccount("");
    }

    /**
     * 设置当前密码状态   明文 OR  密码模式
     */
    @Override
    public void changePassWord() {
        mVPNLoginBean.setPassWordIsVisible(!mVPNLoginBean.isPassWordIsVisible());
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String errorMsg) {

    }

    @Override
    public void vpnLogin() {

    }

    @Override
    public void clientLogin(LoginBean loginBean) {

    }
}
