package com.mall.module.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/***
 * 登录实体类
 */
@Data
public class UserEntityVo {
    private String phone;
    private String password;
    private String code;
}
