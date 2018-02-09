package com.agewnet.huambo.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.agewnet.huambo.app.HuamBoApplication;


/**
 * Created by Dumpling on 2017/10/11.
 */

public class ToolToast {
    public static void success(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(HuamBoApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void error(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(HuamBoApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
