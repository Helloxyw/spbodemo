package com.xyw.spbodemo.controller;

import com.xyw.spbodemo.model.User;
import com.xyw.spbodemo.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Controller
public class IndexController {

    //IOC 控制反转是指 我把一个类的实现写在一个单独的地方,而在用到的地方
    // 用最简单的方式将其注入进来.利用注解或者配置文件在容器中寻找该对象
    // Spring 最核心的概念 可以注入进来
    @Autowired
    private ToutiaoService toutiaoService;

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = {"/", "/index"}) // the path means access entrance
    @ResponseBody
    public String index(HttpSession session) {

        logger.info("visit index");
        return "hello world" + session.getAttribute("msg") +"<br/> say :" +
                toutiaoService.say();
    }


    @RequestMapping(value = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          //can shu request
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "nowCoder") String key) {
        return String.format("{%s},{%d},{%d},{%s}", groupId, userId, type, key);
    }


    @RequestMapping(value = {"/th"})
    public String news(Model model) { //Model 是后端和渲染之间交流数据的一个模型
        model.addAttribute("value1", "vv1");
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }

        model.addAttribute("colors", colors);
        model.addAttribute("map", map);

        model.addAttribute("user", new User("mike"));
        return "news";  //spring boot使用thymeleaf等于将视图地址默认在src/main/resources/templates下了
    }

    /*
    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        return "";
    }
    */


    @RequestMapping(value = {"/redirect/{code}"})
    public RedirectView redirect(@PathVariable("code") int code, HttpSession session) {
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        session.setAttribute("msg", "I jump from a redirect");
        return red;

    }


    @RequestMapping(value = {"/admin"})
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key) {
        if ("admin".equals(key)) {
            return "hello admin!";
        } else {
            throw new IllegalArgumentException("key wrong");
        }

    }

    @ExceptionHandler()
    @ResponseBody    // ERROR :springMVC 外的Exception或Spring mvc 没有处理的Exception
    public String error(Exception e) {
        return "ERROR" + e.getMessage();
    }


}
