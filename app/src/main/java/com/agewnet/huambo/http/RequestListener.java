package com.agewnet.huambo.http;

/**
 * Created by Dumpling on 2017/10/12.
 */

public interface RequestListener {

    void Success(ResponesEntity responesEntity);

    void error(String errorMessage);

}
