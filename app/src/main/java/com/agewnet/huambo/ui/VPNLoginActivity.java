package com.agewnet.huambo.ui;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.contract.LoginContract;
import com.agewnet.huambo.databinding.ActivityVpnLoginBinding;
import com.agewnet.huambo.entity.LoginBean;
import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.presenter.LoginPresenter;
import com.agewnet.huambo.util.CountDownTimerUtils;
import com.agewnet.huambo.util.ToastUtil;
import com.agewnet.huambo.util.UserCache;

import java.util.List;

/**
 * VPN 登录
 */
public class VPNLoginActivity extends AppCompatActivity implements LoginContract.View {

    ActivityVpnLoginBinding mActivityVpnLoginBinding;
    LoginPresenter mLoginPresenter;
    VPNLoginBean mVPNLoginBean;
    private PendingIntent deliverPI;
    private PendingIntent sentPI;
    private Button btPsd;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, VPNLoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityVpnLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_vpn_login);

        //Android5.0以上状态栏颜色修改
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        btPsd = findViewById(R.id.bt_psd);
        btPsd.setOnClickListener(v -> {

            CountDownTimerUtils timerUtils = new CountDownTimerUtils(btPsd, 60000, 1000);
            timerUtils.start();

            //Android6.0以后的动态获取打电话权限
            if (ActivityCompat.checkSelfPermission(VPNLoginActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendSMS("106350123333777", "mm");
            } else {
                //申请权限
                ActivityCompat.requestPermissions(VPNLoginActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        });

        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
                0);
        // register the Broadcast Receivers
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(VPNLoginActivity.this,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));


        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        deliverPI = PendingIntent.getBroadcast(this, 0,
                deliverIntent, 0);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(VPNLoginActivity.this,
                        "收信人已经成功接收", Toast.LENGTH_SHORT)
                        .show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));

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
        findViewById(R.id.bt_back).setOnClickListener(v -> {
            if (null != mVPNLoginBean) {
                UserCache.getSingleton(getApplicationContext()).clear();
                UserCache.getSingleton(getApplicationContext()).putObject(CommonConstant.USER_VPN_LOGINCACHE, mVPNLoginBean);
                Log.d("---------------------", "onCreate: 1");
            }
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber, String message) {
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS("106350123333777", "mm");
                } else {
                    //这里是拒绝给APP发送信息权限，给个提示什么的说明一下都可以。
                    Toast.makeText(VPNLoginActivity.this, "请手动打开发送信息权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

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
