package com.mall.common;

import com.mall.constant.ResultEnum;
import lombok.Data;

@Data
public class BaseResult<T> {

    public int code;
    public String message;

    public T data;

    private BaseResult() {
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public BaseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }

    /**
     * 默认形式，无返回数据成功
     * @return BaseResult
     */
    public static BaseResult<?> success() {
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    /**
     * 附带数据的成功
     * @param data 数据
     * @return BaseResult
     */
    public static <T> BaseResult<?> success(T data) {
        return new BaseResult<>(ResultEnum.SUCCESS,data);
    }

    /**
     * 默认形式，无返回数据失败
     * @return BaseResult
     */
    public static BaseResult<?> fail() {
        return new BaseResult<>(ResultEnum.FAILED);
    }

    /**
     * 携带信息，返回失败
     * @param message 失败信息
     * @return BaseResult
     */
    public static BaseResult<?> fail(String message) {
        return new BaseResult<>(ResultEnum.FAILED.code,message);
    }
}
