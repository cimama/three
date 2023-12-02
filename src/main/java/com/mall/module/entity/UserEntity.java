package com.mall.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("mall_user")
public class UserEntity {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String nickname;
    private String phone;
    private String avatar;
    private String password;
    private String email;
    private String address;
    private String code;
    private String createTime;
    private String updateTime;
    private boolean authentication;
    private boolean sex;
    private boolean status;


}
