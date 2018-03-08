package com.xyw.spbodemo;

import com.xyw.spbodemo.dao.LoginTicketDao;
import com.xyw.spbodemo.dao.NewsDao;
import com.xyw.spbodemo.dao.UserDao;
import com.xyw.spbodemo.model.LoginTicket;
import com.xyw.spbodemo.model.News;
import com.xyw.spbodemo.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpbodemoApplication.class)
@Sql("/init-schema.sql")  // every time test will run this sql script
public class InitDatabaseTest {

    @Autowired
    UserDao userDao;

    @Autowired
    NewsDao newsDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    @Test
    public void initData() {
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                    random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);


            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime()+1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://image.nowcoder.com/head/%dm.png",i));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}",i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html",i));

            newsDao.addNews(news);

            user.setPassword("new Password");
            userDao.updatePassword(user);

            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setTicket(String.format("TICKET%d",i));
            loginTicketDao.addTicket(ticket);

            loginTicketDao.updateStatus(ticket.getTicket(),2);
        }

        Assert.assertEquals("new Password", userDao.selectById(1)
                .getPassword());
        Assert.assertNull(userDao.selectById(1));

        Assert.assertEquals(2,loginTicketDao.selectByTicket("TICKET2").getStatus());
    }
}
