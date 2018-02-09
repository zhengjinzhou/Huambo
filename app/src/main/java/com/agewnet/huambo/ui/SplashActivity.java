package com.agewnet.huambo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.http.RequestApi;
import com.agewnet.huambo.presenter.LoginPresenter;
import com.agewnet.huambo.receiver.MyReceiver;
import com.agewnet.huambo.util.ToastUtil;
import com.agewnet.huambo.util.UserCache;
public class SplashActivity extends AppCompatActivity{
    MyReceiver mMessageReceiver;
    LoginPresenter mLoginPresenter;
    LoginBean mLoginBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏
        super.onCreate(savedInstanceState);

        mMessageReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(CommonConstant.MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
        //获取用户数据
        LoginBean loginBean = (LoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_LOGINCACHE, LoginBean.class);
        //获取vpn登录数据
        VPNLoginBean mVPNLoginBean = (VPNLoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_VPN_LOGINCACHE, VPNLoginBean.class);
        new Handler().postDelayed(() -> {
            if (null == loginBean) {
                LoginActivity.newInstance(SplashActivity.this);
            } else {
                if (loginBean.isAutoLogin()) {
                    if (mVPNLoginBean != null && mVPNLoginBean.isEnable()){
                        ToastUtil.show(getApplicationContext(),getString(R.string.vpnLogin));
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }else {
                        MainActivity.newInstance(SplashActivity.this, String.format(RequestApi.HUAMBO_MAIN_URL, loginBean.getUserName(), loginBean.getUserPass()));
                    }
                } else {
                    LoginActivity.newInstance(SplashActivity.this);
                }
            }
            finish();
        }, 1300);
    }


    public void starMainActivity(String url) {
        //跳转
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra(CommonConstant.MAIN_HTTPURL, String.format(RequestApi.HUAMBO_MAIN_URL, mLoginBean.getUserName(), mLoginBean.getUserPass()));
        intent.putExtra(CommonConstant.MAIN_HTTPURL, url);
        startActivity(intent);
        finish();
    }
}
