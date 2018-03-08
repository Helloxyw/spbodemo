package com.xyw.spbodemo.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Before("execution(* com.xyw.spbodemo.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        logger.info("before method");
    }

    @Before("execution(* com.xyw.spbodemo.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        logger.info("after method");
    }

}
