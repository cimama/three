package com.mall.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.module.entity.UserEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SmsSendService {
    @Resource
    private IUserService userService;

    public  boolean phoneExists(String phone){
        LambdaQueryWrapper<UserEntity> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getPhone,phone);
        wrapper.last("limit 1");
        long count = userService.count(wrapper);
        return count > 0;
    }


}
