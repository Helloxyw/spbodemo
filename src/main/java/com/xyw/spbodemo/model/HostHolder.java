package com.xyw.spbodemo.model;

import org.springframework.stereotype.Component;

@Component   //@component （把普通pojo实例化到spring容器中)，
             // 相当于配置文件中的<bean id="" class=""/>
public class HostHolder {      //save users

    //线程本地
    private static ThreadLocal<User> users = new ThreadLocal<User>();


    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }

}
