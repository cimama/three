package com.mall.constant;

import lombok.Getter;

@Getter
public enum UserAuthEnum {
    LOGIN_SUCCESS(200,"登录成功"),
    CREATE_SUCCESS(200,"注册成功"),
    UPDATE_SUCCESS(200,"修改成功"),
    PHONE_ERROR(300,"请输入正确的手机号"),
    PHONE_PASSWORD_IS_BLANK(301,"手机号或密码不能为空"),
    PHONE_OR_PASSWORD_ERROR(302,"手机号或密码错误"),
    PASSWORD_CHECK_ERROR(303,"密码必须包含数字、大小写字母、特殊符号且大于8位"),
    PHONE_OR_CODE_ERROR(304,"手机号或验证码错误"),
    CODE_ERROR(305,"验证码错误"),
    CREATE_FAILED(400,"注册失败"),
    UPDATE_FAILED(400,"修改失败"),
    LOGIN_FAILED(400,"登录失败");


    public final int code;
    public final String message;
    UserAuthEnum(int code,String message){
        this.code=code;
        this.message=message;
    }
}
