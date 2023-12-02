package com.mall.constant;

import lombok.Getter;

@Getter
public enum SmsCodeEnum {
    SUCCESS(200,"验证码发送成功"),
    ERROR(400,"请勿频繁发送验证码，请稍后重试~"),
    PHONE_ERROR(300,"请注册账号后再试~"),
    PHONE_IS_EXIST(301,"账号已存在请直接登录！");
    public final int code;
    public final String message;
    SmsCodeEnum(int code, String message){
        this.code=code;
        this.message=message;
    }
}
