package com.liwei.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {



    @Test
    void contextLoads() {
        StringBuilder append = new StringBuilder().append("userAccount: ").append(12).append("\n");
        System.out.println(append.toString());
    }

}
