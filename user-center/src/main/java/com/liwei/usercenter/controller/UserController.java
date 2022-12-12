package com.liwei.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liwei.usercenter.common.BaseResponse;
import com.liwei.usercenter.common.ErrorCode;
import com.liwei.usercenter.common.ResultUtil;
import com.liwei.usercenter.exception.BusinessException;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userPassword, userAccount, checkPassword,planetCode))
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        long id = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
        return  ResultUtil.success(id);

    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) return ResultUtil.error(ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userPassword, userAccount)) return null;
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtil.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) return null;
        int result = userService.userLogout(request);
        return ResultUtil.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!isAdmin(request))
            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//        if (StringUtils.isBlank(username)) {
//            return null;
//        }
//        userQueryWrapper.like("username", username);
        if (StringUtils.isNotBlank(username)) {
            userQueryWrapper.like("username", username);
        }

        //脱敏
        List<User> collect = userService.list(userQueryWrapper).stream()
                .map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtil.success(collect);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        //从session中取出来可能是已经过期的数据，所以再查一次数据库
        Long id = currentUser.getId();
        User user = userService.getById(id);
        User safetyUser = userService.getSafetyUser(user);
        return  ResultUtil.success(safetyUser);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);

        if (id <= 0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"id小于0");
        boolean result = userService.removeById(id);
        return ResultUtil.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        //鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
