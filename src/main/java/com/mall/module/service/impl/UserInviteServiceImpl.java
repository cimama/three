package com.mall.module.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.module.entity.UserInviteEntity;
import com.mall.module.mapper.UserInviteMapper;
import com.mall.module.service.IUserInviteService;
import org.springframework.stereotype.Service;

@Service
public class UserInviteServiceImpl extends ServiceImpl<UserInviteMapper, UserInviteEntity> implements IUserInviteService {
}
