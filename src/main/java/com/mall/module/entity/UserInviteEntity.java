package com.mall.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("mall_user_invite")
@Data
public class UserInviteEntity {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private Integer inviterId;
    private Integer inviteeId;
    private Double dividend;
}
