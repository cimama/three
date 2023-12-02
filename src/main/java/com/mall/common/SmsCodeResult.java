package com.mall.common;

import com.mall.constant.SmsCodeEnum;
import lombok.Data;

@Data
public class SmsCodeResult<T> {

    public int code;
    public String message;

    public SmsCodeResult(SmsCodeEnum SmsCodeEnum) {
        this.code = SmsCodeEnum.getCode();
        this.message = SmsCodeEnum.getMessage();
    }

    /**
     * 默认形式，无返回数据成功
     * @return BaseResult
     */
    public static SmsCodeResult<?> success() {
        return new SmsCodeResult<>(SmsCodeEnum.SUCCESS);
    }



    /**
     * 默认形式，无返回数据异常
     * @return BaseResult
     */
    public static SmsCodeResult<?> error() {
        return new SmsCodeResult<>(SmsCodeEnum.ERROR);
    }

    public static SmsCodeResult<?> phoneError() {
        return new SmsCodeResult<>(SmsCodeEnum.PHONE_ERROR);
    }
    public static SmsCodeResult<?> phoneIsExist() {
        return new SmsCodeResult<>(SmsCodeEnum.PHONE_IS_EXIST);
    }



}
