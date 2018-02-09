package com.agewnet.huambo.http;

import java.lang.reflect.Type;

/**
 * Created by Dumpling on 2017/10/11.
 */

public interface NetClient {



    NetClient setRequestUrl(String url);

    NetClient setResponseConver(Type type);

    NetClient setRequestType(RequestType requestType);


    void sendRequest(RequestListener RequestListener);

    public enum RequestType {
        POST,
        PUT,
        GET;
    }

}
