package com.kswook.bean;

import java.io.Serializable;

public class ReturnData<T> implements Serializable {
    private static final long serialVersionUID = 71846877475846866L;
    private String respCode;
    private String respMessage;
    private String message;
    private T data;
//    private PageInfo pageInfo;

    public ReturnData() {

    }

    public static ReturnData success() {
        return new ReturnData("200", "SUCCESS");
    }

    public static ReturnData fail() {
        return new ReturnData("400", "failed");
    }

    public ReturnData data(T data) {
        this.data = data;
        return this;
    }
    public ReturnData message(String msg) {
        this.message = msg;
        return this;
    }

    public ReturnData(String respCode, String respMessage) {
        this.respCode = respCode;
        this.respMessage = respMessage;
    }

    public ReturnData(String respCode, String respMessage, T data) {
        this.respCode = respCode;
        this.respMessage = respMessage;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

}
