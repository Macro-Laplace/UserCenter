package com.liwei.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liwei.usercenter.model.domain.User;

import javax.servlet.http.HttpServletRequest;

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
     * @param planetCode 星球编号
     * @return 新用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);

    /**
     * 用户登陆
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);
}
