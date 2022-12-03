package com.liwei.usercenter;

import com.liwei.usercenter.mapper.UserMapper;
import com.liwei.usercenter.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
//@RunWith(SpringRunner.class)
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

}