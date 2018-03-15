package com.xyw.spbodemo.service;

import com.xyw.spbodemo.dao.LoginTicketDao;
import com.xyw.spbodemo.dao.UserDao;
import com.xyw.spbodemo.model.LoginTicket;
import com.xyw.spbodemo.model.User;
import com.xyw.spbodemo.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.*;


@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;



    public String addUser(User user){
        userDao.addUser(user);
        return user.getName();
    }

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isEmpty(username)) {
            map.put("msgname", "the username can not be empty");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msgpwd", "the password can not be empty");
            return map;
        }

        User user = userDao.selectByName(username);
        if (user != null) {
            map.put("msgname", "the username has been registern");
            return map;
        }

        //password strength
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));

        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));

        userDao.addUser(user);

        //login
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);


        return map;
    }


    //login
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isEmpty(username)) {
            map.put("msgname", "the username can not be empty");
            return map;
        }

        if (StringUtils.isEmpty(password)) {
            map.put("msgpwd", "the password can not be empty");
            return map;
        }

        User user = userDao.selectByName(username);

        if (user == null) {
            map.put("msgname", "the username is not exist");
            return map;
        }

        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(password)) {
            map.put("msgpwd", "the password is incorrect");
            return map;
        }
        //ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }


    public String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketDao.addTicket(ticket);
        return ticket.getTicket();
    }


    //logout
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }


    public User getUser(int id) {
        return userDao.selectById(id);
    }

}
