package com.agewnet.huambo.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;

import com.agewnet.huambo.R;
import com.agewnet.huambo.app.CommonConstant;
import com.agewnet.huambo.databinding.ActivityMainBinding;
import com.agewnet.huambo.databinding.HomePopupwindowBinding;
import com.agewnet.huambo.entity.TitleBarBean;
import com.agewnet.huambo.entity.VPNLoginBean;
import com.agewnet.huambo.http.RequestApi;
import com.agewnet.huambo.util.JsonConvert;
import com.agewnet.huambo.util.PopupUtil;
import com.agewnet.huambo.util.ToolLOG;
import com.agewnet.huambo.util.ToolToast;
import com.agewnet.huambo.util.UserCache;
import com.google.gson.reflect.TypeToken;

import net.arraynetworks.vpn.ProxySettings;
import net.arraynetworks.vpn.VPNManager;

public class MainActivity extends AppCompatActivity {

    /**
     * 控件
     */
    WebView mWebView;
    /**
     * BinDing View
     */
    ActivityMainBinding mActivityMainBinding;
    /**
     * 菜单弹出popup
     */
    static PopupWindow mPopupWindow;

    /**
     * 访问路径
     */
    private String mHttpUrl = RequestApi.HUAMBO_MAIN_URL;
    /**
     * Title Bean
     */
    TitleBarBean mTitleBarBean;
    public static void newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void newInstance(Context context, String httpUrl) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(CommonConstant.MAIN_HTTPURL, httpUrl);
        context.startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //获取控件
        mWebView = mActivityMainBinding.webMain;
        VPNLoginBean mVPNLoginBean = (VPNLoginBean) UserCache.getSingleton(this).getObject(CommonConstant.USER_VPN_LOGINCACHE, VPNLoginBean.class);
        if (null != mVPNLoginBean) {
            if (mVPNLoginBean.isEnable()) {
                ProxySettings.setProxy(this, ProxySettings.DefaultHost, VPNManager.getInstance().getHttpProxyPort(), getApplication().getClass().getName());
            }
        }
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {

        mTitleBarBean = new TitleBarBean();
        mTitleBarBean.setLeftIsVisible(true);
        //绑定  TitleBarBean
        mActivityMainBinding.titleBar.setTitle(mTitleBarBean);
        //添加点击事件
        mActivityMainBinding.titleBar.ivTitleBarLeftImg.setOnClickListener(view -> {
            if (null != mWebView) {
                mWebView.goBack();
            }
        });
        //设置菜单按钮
        mActivityMainBinding.titleBar.ivTitleBarRightImg.setImageResource(R.drawable.main_title_popup);
        //弹出菜单点击事件
        mActivityMainBinding.titleBar.ivTitleBarRightImg.setOnClickListener(view -> {

            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                mPopupWindow.showAsDropDown(mActivityMainBinding.titleBar.ivTitleBarRightImg);
            }
        });
        //获取路径
        if (!TextUtils.isEmpty(getIntent().getStringExtra(CommonConstant.MAIN_HTTPURL))) {
            mHttpUrl = getIntent().getStringExtra(CommonConstant.MAIN_HTTPURL);
        }

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);//加载JavaScript

        //设置可以支持缩放
        //设置支持缩放
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.addJavascriptInterface(new HuamboJsInterface(), "contact");

        mWebView.setWebViewClient(mWebViewClient);//这个一定要设置，要不然不会再本应用中加载
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.getSettings().setSupportZoom(true);
        //清除缓存
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.deleteDatabase("WebView.db");
        this.deleteDatabase("WebViewCache.db");
        mWebView.clearCache(true);
        mWebView.clearFormData();
        getCacheDir().delete();
        mWebView.loadUrl(mHttpUrl);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());

        //初始化Popup
        mPopupWindow = PopupUtil.showTipPopupWindow(this, R.layout.home_popupwindow, contentView -> {
            HomePopupwindowBinding mHomePopupwindowBinding = DataBindingUtil.bind(contentView);
            mHomePopupwindowBinding.setPopupControl(new PopupControl());
        });
    }

    /**
     * 如果要实现文件下载的功能，需要设置WebView的DownloadListener，通过实现自己的DownloadListener来实现文件的下载
     */
    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.i("tag", "url="+url);
            Log.i("tag", "userAgent="+userAgent);
            Log.i("tag", "contentDisposition="+contentDisposition);
            Log.i("tag", "mimetype="+mimetype);
            Log.i("tag", "contentLength="+contentLength);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    //ChromeClient
    WebChromeClient mWebChromeClient = new WebChromeClient() {
        //监听网页加载
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

            result.confirm();
            ToolToast.error(message);
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTitleBarBean.setCenterText(title);
        }
    };
    //WebViewClient
    WebViewClient mWebViewClient = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("http:") || url.startsWith("https:") ) {
                view.loadUrl(url);
                return false;
            }else{
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

        }
    };
    /**
     * 弹出菜单点击事件
     */
    public class PopupControl {
        /**
         * 通讯录
         */
        public void MailList() {
            mPopupWindow.dismiss();
            MailActivity.newInstance(MainActivity.this);
        }

        /**
         * 注销
         */
        public void Cancellation() {
            mPopupWindow.dismiss();
            // ToolToast.success("Cancellation");
            UserCache.getSingleton(MainActivity.this).clear();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MainActivity.this.startActivity(intent);
            finish();
        }

        /**
         * 退出登录
         */
        public void Signout() {
            mPopupWindow.dismiss();
            finish();
        }
    }

    /**
     * JS 交互
     */
    public final class HuamboJsInterface {
        @JavascriptInterface
        public void clickAndroid(String json) {
            ToolLOG.E(json);
            String mType = JsonConvert.analysisJson(json, new TypeToken<String>() {
            }.getType(), "type");
            if (!TextUtils.isEmpty(mType)) {
                switch (mType) {
                    case "phonebook":
                        MailActivity.newInstance(MainActivity.this);
                        mPopupWindow.dismiss();
                        break;
                    case "logout":
                        UserCache.getSingleton(MainActivity.this).clear();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        MainActivity.this.startActivity(intent);
                        finish();
                        break;
                    case "quit":
                        finish();
                        break;
                    default:
                        showPopupWindow();
                        break;
                }
            }
        }
    }

    /**
     * 显示popup
     */
    private void showPopupWindow() {
        new Handler().postDelayed(() -> mPopupWindow.showAsDropDown(mActivityMainBinding.vTop), 200L);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mWebView) {
            mWebView.destroy();
        }
    }
}
