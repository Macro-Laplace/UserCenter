package com.liwei.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liwei.usercenter.model.domain.User;
import com.liwei.usercenter.service.UserService;
import com.liwei.usercenter.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Macro-Laplace
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-12-04 18:43:58
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword))
            return -1;

        if(userAccount.length()<4)
            return -1;

        if(userPassword.length()<8)
            return -1;
        //userAccount不能包含特殊字符
        String validPattern ="[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find())
            return -1;
        //账户不可重复,需要查数据库的放到后面做
        QueryWrapper<User> userQW = new QueryWrapper<>();
        userQW.eq("userAccount",userAccount);
        long count = userMapper.selectCount(userQW);
        if(count>0){
            return -1;
        }
        //对密码加密
        final String SALT="liwei";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);

        //User那边id是Long包装类，这边返回long,所以判断一下防止拆箱失败
        if(!saveResult)
            return -1;
        return user.getId();

    }
}




