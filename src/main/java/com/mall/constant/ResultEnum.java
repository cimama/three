package com.mall.constant;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(200,"请求成功"),
    FAILED(400,"请求失败");
    public final int code;
    public final String message;
    ResultEnum(int code,String message){
        this.code=code;
        this.message=message;
    }
}
