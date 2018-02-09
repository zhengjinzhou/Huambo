package com.agewnet.huambo.http;


public class ResponseCode {
    //请求  成功状态码
    public final static int RESPONSE_SUCCESS = 200;
    //4请求失败 400
    public final static int RESPONSE_ERROR = 400;

    //服务器请求成功返回码
    public final static int SERVICE_SUCCESS = 200;
    //服务器请求失败返回码
    public final static int SERVICE_ERROR = 400;

    //服务器请求异常
    public final static int SERVICE_EXCPTION = 500;


    public final static String NULL_CODE = "";
    public final static String NETWORKERROR_CODE = "";
    public final static int HTTP_CLIENT_EXCEPTION = 000;
    public final static int ERROR_CODE = 300;
    public final static int LOADMORE_CODE = 300;
    public final static int COUPON_ENABLE = 340;
    public final static int TOKENOVERDUE = 301;
    public final static int TOKENNOEXIT = 302;


}
