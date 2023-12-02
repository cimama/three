package com.mall.module.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.common.BaseResult;
import com.mall.common.UserAuthResult;
import com.mall.module.entity.UserEntity;
import com.mall.module.service.IUserService;
import com.mall.module.service.SmsSendService;
import com.mall.module.service.impl.UserServiceImpl;
import com.mall.module.vo.UserEntityVo;
import com.mall.util.PasswordHelper;
import com.mall.util.ValidatePhoneUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
public class UserController {
    @Resource
    private IUserService userService;



    @GetMapping("/user")
    public BaseResult<?> getUser(){
        return BaseResult.success(userService.list());
    }

    @PostMapping("/api/auth/userLogin")
    public UserAuthResult<?> userLogin(@RequestBody UserEntityVo userEntityVo){
      return userService.userLogin(userEntityVo);
    }

    @PostMapping("/api/auth/userBySmsLogin")
    public UserAuthResult<?> userBySmsLogin(@RequestBody UserEntityVo userEntityVo){
        return userService.userBySmsLogin(userEntityVo);
    }

//    @PostMapping("/api/auth/userBySmsRegister")
//    public UserAuthResult<?> userRegister(@RequestBody UserEntity userEntity) {
//        return userService.userBySmsRegister(userEntity);
//    }
    @PostMapping("/api/auth/userBySmsRegister")
    public UserAuthResult<?> userRegisterIsUserCode(@RequestBody UserEntity userEntity, @RequestParam(required = false) String userCode) {
        return userService.userBySmsRegisterIsUserCode(userEntity,userCode);
    }
    @PostMapping("/api/auth/forgetUserPassword")
    public UserAuthResult<?> forgetUserPassword(@RequestBody UserEntityVo userEntity) {
        return userService.forgetUserPassword(userEntity);
    }

}
