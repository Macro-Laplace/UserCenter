package com.liwei.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liwei.usercenter.model.domain.User;

/**
* @author Macro-Laplace
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-12-04 18:43:58
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 账户
     * @param userPassword 密码
     * @param checkPassword 校验码
     * @return 新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);
}
