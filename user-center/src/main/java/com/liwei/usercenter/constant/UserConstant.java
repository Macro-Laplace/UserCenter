package com.liwei.usercenter.constant;

/**
 * 接口中属性都是public static final.所以用来定义常量
 */
public interface UserConstant {
    /**
     * 用户登陆态在session中对应的键
     */
    String USER_LOGIN_STATE = "userLoginState";

    // ---------------------权限--------------------------------
    /**
     * 默认权限 普通用户
     */
    int DEFAULT_ROLE = 0;
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;
}
