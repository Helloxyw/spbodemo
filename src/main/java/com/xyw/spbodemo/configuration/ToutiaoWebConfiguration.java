package com.xyw.spbodemo.configuration;

import com.xyw.spbodemo.interceptor.LoginRequiredInterceptor;
import com.xyw.spbodemo.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);  //quan ju  
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
