package com.liwei.usercenter.service;
import java.util.Date;

import com.liwei.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();

        user.setUsername("wei");
        user.setUserAccount("123");
        user.setAvatarUrl("http://example.com");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("13");
        user.setEmail("12343");
        user.setUserStatus(0);
        user.setIsDelete(0);
//        user.setUserRole(0);
//        user.setPlanetCode("");

        boolean save = userService.save(user);
        System.out.println("是"+user.getId());
        assertTrue(save);
    }

    @Test
    @Transactional
    void userRegister() {
        //密码长度>8
        long result = userService.userRegister("liweiwei", "", "123456");
        Assertions.assertEquals(-1, result);
        //账户名长度>4
        result=userService.userRegister("li", "123456", "123456");
        Assertions.assertEquals(-1, result);
        //密码跟验证码一样
        result=userService.userRegister("liweiliwei", "1234567", "123456");
        Assertions.assertEquals(-1, result);
        //插入已有用户名
        result=userService.userRegister("liwei", "12345678", "12345678");
        Assertions.assertEquals(-1, result);
        //注册新用户
        result=userService.userRegister("liweiliweilii", "12345678", "12345678");
        Assertions.assertNotEquals(-1, result);
    }
}