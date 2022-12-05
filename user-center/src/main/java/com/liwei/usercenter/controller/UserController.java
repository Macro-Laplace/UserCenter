package com.liwei.usercenter.controller;

import com.liwei.usercenter.model.domain.User;
import com.liwei.usercenter.model.request.UserLoginRequest;
import com.liwei.usercenter.model.request.UserRegisterRequest;
import com.liwei.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Macro-Laplace
 * @version 1.0
 * @date 2022/12/5 21:19
 * @description: TODO
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) return null;
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userPassword, userAccount, checkPassword)) return null;
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;

    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) return null;
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userPassword, userAccount)) return null;
        User user = userService.userLogin(userAccount, userPassword, request);
        return user;

    }
}
