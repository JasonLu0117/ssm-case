package com.lym.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lym.vo.BaseVo;

@Controller
public class ArticleController {

    private final Logger logger = Logger.getLogger(ArticleController.class);

    private static final String FAILURE = "FAILURE";
    private static final String SUCCESS = "SUCCESS";
    
    @RequiresPermissions("article-list")
    @RequestMapping(value="/article/list", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo addUser(HttpServletRequest request) {
        System.out.println("---------article----------");
        return null;
    }
    
}
