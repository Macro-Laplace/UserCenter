package com.liwei.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Macro-Laplace
 * @version 1.0
 * @date 2022/12/5 21:26
 * @description: 用户注册请求体。 一般vo是返回给页面的，所以这里不适合起名vo
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -1376444615544489299L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
