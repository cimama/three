package com.mall.common;

import com.mall.constant.ResultEnum;
import com.mall.constant.UserAuthEnum;
import com.mall.constant.UserAuthEnum;
import lombok.Data;

@Data
public class UserAuthResult<T> {

    public int code;
    public String message;
    public T data;
    public UserAuthResult(UserAuthEnum UserAuthEnum) {
        this.code = UserAuthEnum.getCode();
        this.message = UserAuthEnum.getMessage();
    }


    public UserAuthResult(UserAuthEnum UserAuthEnum, T data) {
        this.code = UserAuthEnum.getCode();
        this.message = UserAuthEnum.getMessage();
        this.data = data;
    }

    /**
     * 默认形式，无返回数据登录成功
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> loginSuccess() {
        return new UserAuthResult<>(UserAuthEnum.LOGIN_SUCCESS);
    }
    /**
     * 默认形式，无返回数据登录成功
     * @return UserAuthEnum
     */
    public static <T> UserAuthResult<?> loginSuccess(T data) {
        return new UserAuthResult<>(UserAuthEnum.LOGIN_SUCCESS,data);
    }

    /**
     * 默认形式，无返回数据登陆失败
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> loginFailed() {
        return new UserAuthResult<>(UserAuthEnum.LOGIN_FAILED);
    }

    /**
     * 默认形式，无返回数据手机号或密码为空
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> phoneOrPasswordIsBlank() {
        return new UserAuthResult<>(UserAuthEnum.PHONE_PASSWORD_IS_BLANK);
    }

    /**
     * 默认形式，无返回数据,手机号或密码错误
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> phoneOrPasswordError() {
        return new UserAuthResult<>(UserAuthEnum.PHONE_OR_PASSWORD_ERROR);
    }

    /**
     * 默认形式，无返回数据,手机号或密码错误
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> phoneOrCodeError() {
        return new UserAuthResult<>(UserAuthEnum.PHONE_OR_CODE_ERROR);
    }

    /**
     * 默认形式，无返回数据,手机号格式错误
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> phoneError() {
        return new UserAuthResult<>(UserAuthEnum.PHONE_ERROR);
    }
    /**
     * 默认形式，无返回数据,密码不符合规则
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> passwordCheckError() {
        return new UserAuthResult<>(UserAuthEnum.PASSWORD_CHECK_ERROR);
    }
    /**
     * 默认形式，无返回数据,密码不符合规则
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> createSuccess() {
        return new UserAuthResult<>(UserAuthEnum.CREATE_SUCCESS);
    }
    /**
     * 默认形式，无返回数据,密码不符合规则
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> createFailed() {
        return new UserAuthResult<>(UserAuthEnum.CREATE_FAILED);
    }

    /**
     * 默认形式，无返回数据,修改失败
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> updateFailed() {
        return new UserAuthResult<>(UserAuthEnum.UPDATE_FAILED);
    }

    /**
     * 默认形式，无返回数据,修改成功
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> updateSuccess() {
        return new UserAuthResult<>(UserAuthEnum.UPDATE_SUCCESS);
    }

    /**
     * 默认形式，无返回数据,验证码错误
     * @return UserAuthEnum
     */
    public static UserAuthResult<?> codeError() {
        return new UserAuthResult<>(UserAuthEnum.CODE_ERROR);
    }





}
