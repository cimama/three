package com.mall.module.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.SmsCodeResult;
import com.mall.module.entity.UserEntity;
import com.mall.module.service.IUserService;
import com.mall.module.service.SmsSendService;
import com.mall.third.SendSms;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Resource
    private SmsSendService smsSendService;

    @GetMapping("/api/auth/userCreateSms")
    public SmsCodeResult<?> userCreateSms(@Param("phone") String phone) throws Exception {

        if (smsSendService.phoneExists(phone)){
             return SmsCodeResult.phoneIsExist();
        }else {
            Boolean o = redisTemplate.hasKey(phone);
            System.out.println("o = " + o);
            if (Boolean.TRUE.equals(o)) {
                return SmsCodeResult.error();
            }else {
                SendSms.smsSend(phone,redisTemplate);
                return SmsCodeResult.success();
            }
        }
    }

    @GetMapping("/api/auth/userLoginOrForgetPasswordSms")
       public SmsCodeResult<?> userLoginSms(@Param("phone") String phone) throws Exception {
//        判断数据库是否有手机号
        if (!smsSendService.phoneExists(phone)){
            return SmsCodeResult.phoneError();
        }else {
//            查看redis数据库是否有key
            Boolean o = redisTemplate.hasKey(phone);
            System.out.println("o = " + o);
            if (Boolean.TRUE.equals(o)) {
                return SmsCodeResult.error();
            }else {
                SendSms.smsSend(phone,redisTemplate);
                return SmsCodeResult.success();
            }
        }
    }





}
