package com.mall.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.UserAuthResult;
import com.mall.module.entity.UserEntity;
import com.mall.module.vo.UserEntityVo;

public interface IUserService extends IService<UserEntity> {
    UserAuthResult<?> userLogin(UserEntityVo userEntityVo);

    UserAuthResult<?> userBySmsLogin(UserEntityVo userEntityVo);

//    UserAuthResult<?> userBySmsRegister(UserEntity userEntity);

    UserAuthResult<?> userBySmsRegisterIsUserCode(UserEntity userEntity, String userCode);

    UserAuthResult<?> forgetUserPassword(UserEntityVo userEntity);
}
