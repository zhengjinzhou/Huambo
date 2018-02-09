package com.agewnet.huambo.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.app.HuamBoApplication;
import com.agewnet.huambo.contract.LoginContract;
import com.agewnet.huambo.databinding.ActivityLoginBinding;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.http.RequestApi;
import com.agewnet.huambo.presenter.LoginPresenter;
import com.agewnet.huambo.util.ToastUtil;
import com.agewnet.huambo.util.ToolToast;
import com.agewnet.huambo.util.UserCache;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, ActivityCompat.OnRequestPermissionsResultCallback{

    public final static int REQUEST_READ_PHONE_STATE = 1;

    ActivityLoginBinding mActivityLoginBinding;
    LoginPresenter mLoginPresenter;
    LoginBean mLoginBean;
    ProgressDialog mProgressDialog;
    public static void newInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        //权限申请
        initPermission();
        initView();
    }

    /**
     * 动态权限申请
     */
    private void initPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.CALL_PHONE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }

    private void initView() {
        //初始化
        mLoginPresenter = new LoginPresenter();
        mLoginBean = new LoginBean();
        mProgressDialog = new ProgressDialog(this);
        LoginBean tempLogin = (LoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_LOGINCACHE, LoginBean.class);
        Log.d("", "initView: "+tempLogin.toString());

        if (null != tempLogin) {
            if (tempLogin.isRememberAccount()) {
                mLoginBean.setUserName(tempLogin.getUserName());
                //mLoginBean.setUserPass(tempLogin.getUserPass());
                mLoginBean.setRememberAccount(tempLogin.isRememberAccount());
            }
        } else {
            //默认密码状态显示
            mLoginBean.setPassWordIsVisible(false);
        }
        //是否为VPN登录
        mLoginBean.setVPNLogin(false);
        mLoginPresenter.attach(this);
        mActivityLoginBinding.setPresenter(mLoginPresenter);
        //绑定LoginBean
        mActivityLoginBinding.setLoginBean(mLoginBean);
    }

    /**
     * 清空用户名
     */
    @Override
    public void clearAccount() {
        mLoginBean.setUserName("");
    }

    /**
     * 设置当前密码状态   明文 OR  密码模式
     */
    @Override
    public void changePassWord() {
        mLoginBean.setPassWordIsVisible(!mLoginBean.isPassWordIsVisible());
    }
    public void starMainActivity(String url) {
        //跳转
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(CommonConstant.MAIN_HTTPURL, url);
        startActivity(intent);
        finish();
    }

    /**
     * 登录成功
     */
    @Override
    public void onSuccess() {
        Log.d("----------", "onSuccess: ------------111------");
        //记录密码       添加到本地缓存
        UserCache.getSingleton(this).putObject(CommonConstant.USER_LOGINCACHE, mLoginBean);
        VPNLoginBean mVPNLoginBean = (VPNLoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_VPN_LOGINCACHE, VPNLoginBean.class);
        //Dialog
        onDialogDismiss();
        if (null != mVPNLoginBean) {
            if (mVPNLoginBean.isEnable()) {
                //starMainActivity(RequestApi.HUAMBO_VPN_MAIN);
                starMainActivity(String.format(RequestApi.HUAMBO_MAIN_URL, mLoginBean.getUserName(), mLoginBean.getUserPass()));
            } else {
                starMainActivity(String.format(RequestApi.HUAMBO_MAIN_URL, mLoginBean.getUserName(), mLoginBean.getUserPass()));
            }
        } else {
            starMainActivity(String.format(RequestApi.HUAMBO_MAIN_URL, mLoginBean.getUserName(), mLoginBean.getUserPass()));
        }
    }

    /**
     * 登录失败
     *
     * @param errorMsg
     */
    @Override
    public void onFailure(String errorMsg) {
        onDialogDismiss();
        ToolToast.error(errorMsg);
    }
    private void onShowDialog() {
        if (null != mProgressDialog) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.setMessage(getString(R.string.logining));
                if (!isFinishing()) {
                    mProgressDialog.show();
                }
            }
        }
    }

    private void onDialogDismiss() {
        if (null != mProgressDialog) {
            if (mProgressDialog.isShowing()) {
                if (!isFinishing()) {
                    mProgressDialog.dismiss();
                }
            }
        }
    }

    @Override
    public void vpnLogin() {
        onShowDialog();
        HuamBoApplication.getContext().initVpn().Login(new HuamBoApplication.VpnCallBack() {
            @Override
            public void onSuccess() {
                mLoginPresenter.loginClient(mLoginBean);
            }
            @Override
            public void onError(String message) {
                ToolToast.error(message);
                onDialogDismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void clientLogin(LoginBean loginBean) {
        onShowDialog();
        VPNLoginBean mVPNLoginBean = (VPNLoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_VPN_LOGINCACHE, VPNLoginBean.class);
        if (null != mLoginPresenter) {
            if (null != mVPNLoginBean) {
                if (mVPNLoginBean.isEnable()) {
                    HuamBoApplication.getContext().initVpn().Login(new HuamBoApplication.VpnCallBack() {
                        @Override
                        public void onSuccess() {
                           // mLoginPresenter.loginClient(mLoginBean);
                            Log.d("-----------------", "onSuccess: vpn登录？");
                            starMainActivity(String.format(RequestApi.HUAMBO_MAIN_URL, mLoginBean.getUserName(), mLoginBean.getUserPass()));
                        }
                        @Override
                        public void onError(String message) {
                            ToolToast.error(message);
                            onDialogDismiss();
                        }
                    });
                } else {
                    mLoginPresenter.loginClient(loginBean);
                }
            } else {
                mLoginPresenter.loginClient(loginBean);
            }
        }
    }
}
