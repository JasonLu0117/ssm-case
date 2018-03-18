package com.lym.intercept;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogInterceptor {
    
//    用log4j，而不是slf4j。
//    private final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);  
    private final Logger logger = Logger.getLogger(LogInterceptor.class);
    
    @Before(value = "execution(* com.lym.controller.*.*(..))")
    public void before(){  
//        logger.info("login start!");
        logger.info("logger start!");
    }  
    
    @After(value = "execution(* com.lym.controller.*.*(..))")
    public void after(){  
//        logger.info("login end!");
        logger.info("logger end!");
    }

}
