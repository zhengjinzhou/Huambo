package com.agewnet.huambo.entity;

/**
 * Created by Dumpling on 2017/10/20.
 */

public class ResponseLoginBean {

    /**
     * Result : true
     * Message : 登录成功
     */

    private boolean Result;
    private String Message;

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }


}
