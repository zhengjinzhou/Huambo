package com.agewnet.huambo.base;


import com.agewnet.huambo.http.HttpClient;

import java.lang.ref.WeakReference;

/**
 * Created by Dumpling on 2017/10/11.
 */

public abstract class BasePresenter {


    WeakReference<BaseView> weak = null;


    public void attach(BaseView baseView) {
        weak = new WeakReference<BaseView>(baseView);

    }


    public void dettach() {
        if (null != weak) {
            weak.clear();
            weak = null;
        }
    }

    public BaseView getView() {
        if (null != weak) {
            return weak.get();
        } else {
            return null;
        }
    }

    public HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        return httpClient;
    }

}
