package com.liwei.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liwei.usercenter.model.domain.User;
import com.liwei.usercenter.model.request.UserLoginRequest;
import com.liwei.usercenter.model.request.UserRegisterRequest;
import com.liwei.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.liwei.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.liwei.usercenter.constant.UserConstant.USER_LOGIN_STATE;


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

    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request){
        if (!isAdmin(request))
        return new ArrayList<User>();

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isBlank(username)){
            return null;
        }
        userQueryWrapper.like("username", username);
        //脱敏
        return userService.list(userQueryWrapper).stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id,HttpServletRequest requestt){
        if (!isAdmin(requestt))
            return false;

        if(id<=0)
            return false;
       return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        //鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user =(User)userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
