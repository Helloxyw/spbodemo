package com.xyw.spbodemo;


import com.xyw.spbodemo.model.User;
import com.xyw.spbodemo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpbodemoApplication.class)
public class MysqlTest {

    @Autowired
    private UserService userService;


    @Test
    public void test(){
        User user = new User();
        user.setName("大锤");
        user.setSalt("");
        user.setHeadUrl("lalalallla");
    //  user.setName("nike");
        user.setPassword("123456");

        System.out.println(userService.addUser(user));
    }

}
