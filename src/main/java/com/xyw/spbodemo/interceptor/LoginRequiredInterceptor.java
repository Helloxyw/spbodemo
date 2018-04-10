package com.xyw.spbodemo.interceptor;

import com.xyw.spbodemo.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    //这个方法在业务处理器处理请求之前被调用，在该方法中对用户请求 request 进行处理。
    //如果程序员决定该拦截器对 请求进行拦截处理后还要调用其他的拦截器，或者是业务处理器去进行处理，则返回true；
    // 如果程序员决定不需要再调用其他的组件去处理请求，则返回false。
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(hostHolder.getUser()==null){
            httpServletResponse.sendRedirect("/?pop=1");
            return false;
        }
        return true;

    }

    //方法在业务处理器处理完请求后，但是DispatcherServlet 向客户端返回响应前被调用
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //这个方法在 DispatcherServlet 完全处理完请求后被调用，可以在该方法中进行一些资源清理的操作。
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
