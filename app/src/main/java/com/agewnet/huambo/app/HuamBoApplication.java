package com.agewnet.huambo.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.util.BaseTaskSwitch;
import com.agewnet.huambo.util.ToolLOG;
import com.agewnet.huambo.util.ToolToast;
import com.agewnet.huambo.util.UserCache;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import net.arraynetworks.vpn.Common;
import net.arraynetworks.vpn.VPNManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Dumpling on 2017/10/11.
 */

public class HuamBoApplication extends Application {
    static HuamBoApplication mHuamBoApplication;
    private static VPNManager.AAAMethod[] mMethods = null;
    private Thread mThreadStartVpn = null;
    private Thread mThreadStopVpn = null;
    VPNLoginBean mVPNLoginBean;

    @Override
    public void onCreate() {
        super.onCreate();
        mHuamBoApplication = this;
        // 必须setDebugMode() 和 init()
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        mVPNLoginBean = (VPNLoginBean) UserCache.getSingleton(mHuamBoApplication).getObject(CommonConstant.USER_VPN_LOGINCACHE, VPNLoginBean.class);
        // initVpn();
        //前后台切换
        BaseTaskSwitch.init(this).setOnTaskSwitchListener(new BaseTaskSwitch.OnTaskSwitchListener() {
            @Override
            public void onTaskSwitchToForeground() {

            }
            @Override
            public void onTaskSwitchToBackground() {
                //ToolToast.success("切换到后台");
                if (null != VPNManager.getInstance()) {
                    VPNManager.getInstance().cancelLogin();
                }
            }
        });
    }

    public HuamBoApplication initVpn() {
        //初始化 VPN　Handler　
        VPNManager.initialize(this).setHandler(mHandler);
        //初始化 VPN　
        VPNManager.getInstance().setLogLevel(Common.LogLevel.LOG_DEBUG, 0);
        return mHuamBoApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static HuamBoApplication getContext() {
        return mHuamBoApplication;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Common.VpnMsg.MSG_VPN_CONNECTING:
                    ToolLOG.D("vpn connecting---------------------");
                    break;
                case Common.VpnMsg.MSG_VPN_CONNECTED:
                    ToolLOG.D("vpn connected ---------------------");
                    if (null != mVpnCallBack) {
                        mVpnCallBack.onSuccess();
                    }
                    break;
                case Common.VpnMsg.MSG_VPN_DISCONNECTING:
                    ToolLOG.D("vpn disconnecting ---------------------");
                    //showError(msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE));
                    break;
                case Common.VpnMsg.MSG_VPN_DISCONNECTED:
                    ToolLOG.D("vpn disconnected---------------------");
                    showError(msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE));
                    break;
                case Common.VpnMsg.MSG_VPN_CONNECT_FAILED:
                    ToolLOG.D("vpn connect failed---------------------");
                    showError(msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE));
                    break;
                case Common.VpnMsg.MSG_VPN_RECONNECTING:
                    ToolLOG.D("vpn reconnecting---------------------");
                    showError(msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE));
                    break;
                case Common.VpnMsg.MSG_VPN_LOGIN:
                    int error = msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE);
                    mMethods = (VPNManager.AAAMethod[]) (msg.obj);
                    if (0 == mMethods.length) {
                        VPNManager.getInstance().cancelLogin();
                    }
                    showError(msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE));
                    // MainActivity.newInstance(LoginActivity.this);
                    break;
                case Common.VpnMsg.MSG_VPN_DEVREG:
                    mMethods = (VPNManager.AAAMethod[]) (msg.obj);
                    //      ToolToast.success("devreg");
                    showError(msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE));
                    //MainActivity.newInstance(LoginActivity.this);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    public void vpnLogin() {
        /**
         * 请求权限
         */
        if (null != mVPNLoginBean) {
            String server = "mobile.dg.cn";
            mVPNLoginBean = (VPNLoginBean) UserCache.getSingleton(mHuamBoApplication).getObject(CommonConstant.USER_VPN_LOGINCACHE, VPNLoginBean.class);
            String username = mVPNLoginBean.getVpnAccount();
            String userPwd = mVPNLoginBean.getVpnPassWord();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(username)) {
                mVpnCallBack.onError("vpn账号或密码为空");
                return;
            }
            start(server, 443, username, userPwd, "", "",
                    VPNManager.VpnFlag.VPN_FLAG_HTTP_PROXY
                            | VPNManager.VpnFlag.VPN_FLAG_SOCK_PROXY);
        }
    }

    /**
     * Show the error message when login failed or device registration failed.
     *
     * @param msgcode the error code.
     */
    private void showError(int msgcode) {
        stop();
        if (null == mVpnCallBack) {
            ToolToast.error("请重试");
            return;
        }
        if (Common.VpnError.ERR_WRONG_USER_PASS == msgcode) {
            mVpnCallBack.onError("VPN账号或密码错误");
        } else if (Common.VpnError.ERR_INTERRUPTED == msgcode) {
            // Show certificate errors
            String[] msg = VPNManager.getInstance().getErrorInfo(msgcode);
        } else if ((Common.VpnError.ERR_DEVID_APPROVE_PENDING == msgcode)
                || (Common.VpnError.ERR_DEVID_APPROVE_DENY == msgcode)
                || (Common.VpnError.ERR_DEVID_USER_LIMIT == msgcode)
                || (Common.VpnError.ERR_DEVID_DEV_LIMIT == msgcode)
                || (Common.VpnError.ERR_DEVID_UNREG == msgcode)) {
            String[] msg = VPNManager.getInstance().getErrorInfo(msgcode);
        } else if (Common.VpnError.ERR_CALLBACK_FAILED == msgcode) {
            String[] msg = VPNManager.getInstance().getErrorInfo(msgcode);
        } else if ((msgcode >= Common.VpnError.ERR_CERT_NO)
                && (msgcode <= Common.VpnError.ERR_CERT_SERVER_REVOKED)) {
            String[] msg = VPNManager.getInstance().getErrorInfo(msgcode);
        } else {
            mVpnCallBack.onError("Connection to server failed.");
        }
    }

    private class StartVpnThread extends Thread {
        String mHost;
        int mPort;
        String mUserName;
        String mPassword;
        String mCertPath;
        String mCertPass;
        int mFlag;
        public StartVpnThread(String host, int port, String username,
                              String password, String certpath, String certpass, int flag) {
            mHost = host;
            mPort = port;
            mUserName = username;
            mPassword = password;
            mCertPath = certpath;
            mCertPass = certpass;
            mFlag = flag;
        }

        public void run() {
            VPNManager.getInstance().startVPN(mHost, mPort, mUserName,
                    mPassword);
        }
    }

    public void Login(VpnCallBack vpnCallBack) {
        mVpnCallBack = vpnCallBack;
        if (!isNetworkAvailable(this)) {
            mVpnCallBack.onError("请检查网络连接");
            return;
        }
        vpnLogin();
    }

    /**
     * start the VPN tunnel.
     */
    public void start(String host, int port, String username, String password,
                      String certpath, String certpass, int flag) {
        if (mThreadStartVpn != null) {
            ToolLOG.D("ThreadStartVpn is not null, will kill it!");
            mThreadStartVpn.interrupt();
            try {
                mThreadStartVpn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThreadStartVpn = null;
        }
        if (null != VPNManager.getInstance()) {
            VPNManager.getInstance().cancelLogin();
        }
        mThreadStartVpn = new StartVpnThread(host, port, username, password,
                certpath, certpass, flag);
        mThreadStartVpn.start();
    }

    /**
     * Stop the VPN tunnel.
     */
    public void stop() {
        if (Common.VpnStatus.IDLE == VPNManager.getInstance().getStatus()) {
            return;
        }
        if (mThreadStopVpn != null) {
            ToolLOG.D("ThreadStopVpn is not null, will kill it.");
            mThreadStopVpn.interrupt();
            try {
                mThreadStopVpn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThreadStopVpn = null;
        }

        if (mThreadStartVpn != null) {
            ToolLOG.D("ThreadStartVpn is not null, will kill it!");
            mThreadStartVpn.interrupt();
            try {
                mThreadStartVpn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mThreadStartVpn = null;
        }

        mThreadStopVpn = new Thread(() -> {
            VPNManager.getInstance().stopVPN();
            mThreadStopVpn = null;
        });
        mThreadStopVpn.start();
        VPNManager.getInstance().cancelLogin();
    }

    VpnCallBack mVpnCallBack;

    public interface VpnCallBack {
        void onSuccess();
        void onError(String message);
    }

    // check all network connect, WIFI or mobile
    public static boolean isNetworkAvailable(final Context context) {
        boolean hasWifoCon = false;
        boolean hasMobileCon = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfos = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfos) {
            String type = net.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                //  LevelLogUtils.getInstance().i(tag, "get Wifi connection");
                if (net.isConnected()) {
                    hasWifoCon = true;
                }
            }
            if (type.equalsIgnoreCase("MOBILE")) {
                //  LevelLogUtils.getInstance().i(tag, "get Mobile connection");
                if (net.isConnected()) {
                    hasMobileCon = true;
                }
            }
        }
        return hasWifoCon || hasMobileCon;
    }
}
