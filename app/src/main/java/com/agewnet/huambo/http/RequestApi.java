package com.agewnet.huambo.http;

/**
 * Created by Dumpling on 2017/10/12.
 */

public interface RequestApi {
    /**
     * 服务器域名 http://19.108.192.125
     * http://121.15.203.82:9210
     *
     * http://120.86.117.106
     *
     http://121.15.203.82:9210/DMS_Phone/index.aspx

     http://sljoa.dg //水务局
     http://www.dgluqiao.com

     http://19.108.192.125//塘厦
     kjjoa.dg//科技局
     nyj.dg//农业局
     http://19.104.9.233
     http://19.111.48.16  东莞市滨海湾新区管委会智慧办公系统  dgsbhwxqgwh

     */
    String BASE_URL = "http://121.15.203.82:9210";

    /**
     * 登录路径
     */
    String HUAMBO_LOGIN_URL = "/DMS_Phone/Login/LoginHandler.ashx?Action=Login&cmd={UserID:'%1$s',UserPsw:'%2$s'}";
    //   String HUAMBO_MAIN_URL = BASE_URL + "/DMS_Phone/Login/LoginIndex.aspx";
    /**
     * 主页面地址
     */
    String HUAMBO_MAIN_URL = BASE_URL + "/DMS_Phone/Login/QuickLogin.aspx?cmd={UserID:'%1$s',UserPsw:'%2$s'}&From=APP";
    /**
     * 通讯录获取列表
     */
    String HUAMBO_MAILLIST = "/DMS_Phone/Contact/ContactHandler.ashx?Action=GetContactByUserID&para={UserID:'%1$s'}";

    String mobile = "";
}
