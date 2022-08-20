package com.runde.commonlibrary.bean;

import java.io.Serializable;

/** 微信授权实体类
 * Created by hua on 2019/3/21 0021.
 */

public class WechatLoginBean implements Serializable {

    private String openid;
    private String token;
    private String phoneNum;
    private String sms_token;
    private String verifyCode;
    private String funcType;
    private String nickName;
    private String password;
    private int parentId;
    private int respondCode; //获取验证码接口的返回码，-44不需要跳注册页面，-22就要

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSms_token() {
        return sms_token;
    }

    public void setSms_token(String sms_token) {
        this.sms_token = sms_token;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getRespondCode() {
        return respondCode;
    }

    public void setRespondCode(int respondCode) {
        this.respondCode = respondCode;
    }
}
