package com.mall.module.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.UserAuthResult;
import com.mall.module.entity.UserEntity;
import com.mall.module.entity.UserInviteEntity;
import com.mall.module.mapper.UserMapper;
import com.mall.module.service.IUserInviteService;
import com.mall.module.service.IUserService;
import com.mall.module.service.SmsSendService;
import com.mall.module.vo.UserEntityVo;
import com.mall.util.*;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    @Resource
    private IUserService userService;
    @Resource
    private SmsSendService smsSendService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IUserInviteService inviteService;

    @Override
    public UserAuthResult<?> userLogin(UserEntityVo userEntityVo) {
        String phone = userEntityVo.getPhone();
        String password = userEntityVo.getPassword();
//        验证手机号格式
        boolean mobile = ValidatePhoneUtil.isMobile(phone);
//        手机号在数据库是否存在
        boolean mobileIsExist = smsSendService.phoneExists(phone);
        System.out.println("mobileIsExist = " + mobileIsExist);
//        判断密码

//        String s = PasswordHelper.encryptPassword(password, "");
        if (!mobile) {
            return UserAuthResult.phoneError();
        } else if (!mobileIsExist) {
            return UserAuthResult.phoneOrPasswordError();
        } else if (StringUtils.isBlank(phone) || StringUtils.isBlank(password)) {
            return UserAuthResult.phoneOrPasswordIsBlank();
        }
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.select("id", "code", "password").eq("phone", phone);
        UserEntity one = userService.getOne(wrapper);
        System.out.println("JSON.toJSONString(one) = " + JSON.toJSONString(one.getId()));
        String code = one.getCode();
        String ePwd = one.getPassword();
        System.out.println("code = " + code);
        String pwd = PasswordHelper.encryptPassword(password, code);
        boolean equals = ePwd.equals(pwd);
        if (equals) {
            String token = JwtUtils.createToken(phone);
            redisTemplate.delete("TOKEN_" + phone);

            redisTemplate.opsForValue().set("TOKEN_" + phone, token, 7, TimeUnit.DAYS);
//            redisTemplate.delete(phone);

            HashMap<Object, Object> map = new HashMap<>();
            map.put("token", token);
            return UserAuthResult.loginSuccess(map);
        } else {
            return UserAuthResult.loginFailed();
        }
    }

    @Override
    public UserAuthResult<?> userBySmsLogin(UserEntityVo userEntityVo) {
        String phone = userEntityVo.getPhone();
        String code = userEntityVo.getCode();

        Boolean b = redisTemplate.hasKey(phone);
        boolean mobile = ValidatePhoneUtil.isMobile(phone);
//        判断手机号是否符合
        if (!mobile) {
            return UserAuthResult.phoneError();
        }
        boolean mobileIsExist = smsSendService.phoneExists(phone);
        System.out.println("mobileIsExist = " + mobileIsExist);
        if (!mobileIsExist) {
            return UserAuthResult.phoneOrCodeError();
        }
//        验证码是否发送成功
        if (Boolean.TRUE.equals(b)) {
            Object o = redisTemplate.opsForValue().get(phone);
//            获取验证码
            boolean equals = code.equals(o);
//            判断输入的和数据库的是否相同
            System.out.println("equals = " + equals);
            if (equals) {
                String token = JwtUtils.createToken(phone);
//                删除之前的token
                redisTemplate.delete("TOKEN_" + phone);
//                将登录成功的token存入redis
                redisTemplate.opsForValue().set("TOKEN_" + phone, token, 7, TimeUnit.DAYS);
//                将登陆成功的验证码从redis删除
                redisTemplate.delete(phone);
                HashMap<Object, Object> map = new HashMap<>();
                map.put("token", token);
                return UserAuthResult.loginSuccess(map);
            } else {
                return UserAuthResult.phoneOrCodeError();
            }
        }
        return UserAuthResult.loginFailed();
    }

    @Override
    public UserAuthResult<?> userBySmsRegisterIsUserCode(UserEntity userEntity, String userCode) {
        String phone = userEntity.getPhone();
        String password = userEntity.getPassword();
        String code = userEntity.getCode();

//        判断手机号
        boolean mobile = ValidatePhoneUtil.isMobile(phone);
        if (!mobile) {
            return UserAuthResult.phoneError();
        }
//        判断数据库是否已经存在手机号
        boolean mobileIsExist = smsSendService.phoneExists(phone);
//        System.out.println("mobileIsExist = " + mobileIsExist);
        if (mobileIsExist) {
            return UserAuthResult.phoneOrCodeError();
        }

//        密码规则大小写字母，数字，字符8-18位
        boolean pwdLength = PwdCheckUtil.checkPasswordLength(password, "8", "18");
//        检测密码中是否包含大写字母
        boolean pwdUpperCase = PwdCheckUtil.checkContainUpperCase(password);
//        检测密码中是否包含小写字母
        boolean pwdLowerCase = PwdCheckUtil.checkContainLowerCase(password);
//        检测密码中是否包含特殊符号
        boolean pwdSpecialChar = PwdCheckUtil.checkContainSpecialChar(password);
//        检测密码中是否包含数字
        boolean pwdContainDigit = PwdCheckUtil.checkContainDigit(password);

        if (!pwdContainDigit || !pwdLength || !pwdLowerCase || !pwdUpperCase || !pwdSpecialChar) {
            return UserAuthResult.passwordCheckError();
        }
//        System.out.println("formattedDate = " + formattedDate);
//        判断验证码
        Object o = redisTemplate.opsForValue().get(phone);
        if (!code.equals(o)) {
            return UserAuthResult.phoneOrCodeError();
        }
        String inviter = ValidateCodeUtils.generateValidateCode4String(6);
        LambdaQueryWrapper<UserEntity> inviterWrapper = new LambdaQueryWrapper<>();
        inviterWrapper.eq(UserEntity::getCode, inviter);
        long inviterCount = userService.count(inviterWrapper);
        //        循环判断邀请码是否相同
        while (inviterCount > 0) {
//            如果相同重新赋值
            inviter = ValidateCodeUtils.generateValidateCode4String(6);
            inviterWrapper.eq(UserEntity::getCode, inviter);
            inviterCount = userService.count(inviterWrapper);
        }
        System.out.println("inviterCode = " + inviter);
        //        生成加密密码不可逆
        String pwd = PasswordHelper.encryptPassword(password, inviter);
//        时间
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        昵称处理
        String nickname = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");


        //        存储用户
        UserEntity user = new UserEntity();
        user.setPhone(phone);
        user.setPassword(pwd);
        user.setCode(inviter);
        user.setCreateTime(time);
        user.setUpdateTime(time);
        user.setNickname(nickname);
        user.setStatus(true);
        user.setSex(true);
        user.setAuthentication(false);
        user.setAvatar("http://s4yumfkbg.bkt.clouddn.com/%E5%BE%AE%E4%BF%A1%E6%88%AA%E5%9B%BE_20231118044931.png");
//        保存用户到数据库
        boolean userSave = userService.save(user);
        if (userSave) {
            //        拿到url路径的邀请码与数据库判断查询出被邀请id
            LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserEntity::getCode, userCode);
            long count = userService.count(wrapper);
            System.out.println("count = " + count);
            //        如果邀请码不为空
            if (count > 0) {
                QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("id").eq("code", userCode).last("limit 1");
//            邀请者id
                UserEntity inviterId = userService.getOne(queryWrapper);
                System.out.println("inviterId = " + inviterId);
                QueryWrapper<UserEntity> entityQueryWrapper = new QueryWrapper<>();
                entityQueryWrapper.select("id").eq("phone", phone).last("limit 1");
//            被邀请者id
                UserEntity inviteeId = userService.getOne(entityQueryWrapper);
                System.out.println("inviteeId = " + inviteeId.getId());
                UserInviteEntity userInvite = new UserInviteEntity();
                userInvite.setInviterId(Math.toIntExact(inviterId.getId()));
                userInvite.setInviteeId(Math.toIntExact(inviteeId.getId()));
                userInvite.setDividend(0.06);
                inviteService.save(userInvite);
            }
        }
//        返回成功信息
        return UserAuthResult.createSuccess();
    }

    /***
     * 忘记密码
     * @param userEntity
     * @return
     */
    @Override
    public UserAuthResult<?> forgetUserPassword(UserEntityVo userEntity) {
//        获取输入的手机号
        String phone = userEntity.getPhone();
//        密码
        String password = userEntity.getPassword();
//        验证码
        String code = userEntity.getCode();
//        判断手机号是否在数据库存在
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getPhone, phone).last("limit 1");
        long count = userService.count(wrapper);
        if (count > 0) {
//            判断验证码是否存在
            Boolean b = redisTemplate.hasKey(phone);
            if (Boolean.TRUE.equals(b)) {
//                取验证码
                Object o = redisTemplate.opsForValue().get(phone);
//                和输入的验证码比对
                if (code.equals(o)) {
                    //        密码规则大小写字母，数字，字符8-18位
                    boolean pwdLength = PwdCheckUtil.checkPasswordLength(password, "8", "18");
                    //        检测密码中是否包含大写字母
                    boolean pwdUpperCase = PwdCheckUtil.checkContainUpperCase(password);
                    //        检测密码中是否包含小写字母
                    boolean pwdLowerCase = PwdCheckUtil.checkContainLowerCase(password);
                    //        检测密码中是否包含特殊符号
                    boolean pwdSpecialChar = PwdCheckUtil.checkContainSpecialChar(password);
                    //        检测密码中是否包含数字
                    boolean pwdContainDigit = PwdCheckUtil.checkContainDigit(password);
//                    符合规范
                    if (pwdLength && pwdContainDigit && pwdLowerCase && pwdUpperCase && pwdSpecialChar){
//                        取出邀请码作为加密盐值
                        QueryWrapper<UserEntity> queryWrapper=new QueryWrapper<>();
                        queryWrapper.select("code").eq("phone",phone).last("limit 1");
                        UserEntity one = userService.getOne(queryWrapper);
                        String pwd = PasswordHelper.encryptPassword(password, one.getCode());
//                        给密码赋值
                        UserEntity user=new UserEntity();
                        user.setPassword(pwd);
                        boolean userPassword = userService.update(user, wrapper);
//                        如果修改成功
                        if (userPassword){
//                            成功
                            return UserAuthResult.updateSuccess();
                        }else {
//                            失败
                            return UserAuthResult.updateFailed();
                        }
                    }else {
//                        密码不符合
                        return UserAuthResult.passwordCheckError();
                    }
                }else {
//                    验证码错误
                    return UserAuthResult.codeError();
                }
            }
        }
//        手机号错误
        return UserAuthResult.phoneOrCodeError();
    }
}
