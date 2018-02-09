package com.agewnet.huambo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.agewnet.huambo.app.CommonConstant;
import com.google.gson.Gson;

/**
 * Created by Dumpling on 2017/10/14.
 */

public class UserCache {

    private volatile static UserCache singleton;
    static SharedPreferences mSharedPreferences;
    /**
     * 单例
     *
     * @return
     */
    public static UserCache getSingleton(Context context) {
        //一层验证
        if (null == singleton) {
            synchronized (UserCache.class) {
                //二层验证
                if (null == singleton) {
                    singleton = new UserCache(context);
                }
            }
        }
        return singleton;
    }

    public UserCache(Context context) {
        mSharedPreferences = context.getSharedPreferences(CommonConstant.USER_SHAREPRE, context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        if (null != mSharedPreferences) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        } else {
            ToolLOG.E("UserCache putString isNull");
        }
    }

    public String getString(String key, String defaultValue) {
        if (null == mSharedPreferences) {
            ToolLOG.E("UserCache getString isNull");
            return "";
        }
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void putObject(String key, Object object) {
        if (null != mSharedPreferences) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            Gson gson = new Gson();
            editor.putString(key, gson.toJson(object));
            editor.apply();
        } else {
            ToolLOG.E("UserCache putObject isNull");
        }
    }

    public Object getObject(String key, Class c) {
        Object object = null;
        try {
            if (null != mSharedPreferences) {
                String json = mSharedPreferences.getString(key, "");
                if (TextUtils.isEmpty(json)) {
                    ToolLOG.E("UserCache getObject json isNull");
                    return null;
                } else {
                    Gson gson = new Gson();
                    object = gson.fromJson(json, c);
                }
            } else {
                ToolLOG.E("UserCache getObject isNull");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToolLOG.E("UserCache Exception isNull");
            return null;
        }
        return object;
    }


    public void clear() {
        if (null != mSharedPreferences) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.clear();
            editor.apply();
        } else {
            ToolLOG.E("UserCache clear isNull");
        }

    }

}
