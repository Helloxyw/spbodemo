package com.xyw.spbodemo;

import com.xyw.spbodemo.model.EntityType;
import com.xyw.spbodemo.service.LikeService;
import com.xyw.spbodemo.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpbodemoApplication.class)
public class JedisTest {

    @Autowired
    LikeService likeService;

    @Test
    public void test(){
        likeService.like(2,EntityType.ENTITY_NEWS,100);
    }
}
