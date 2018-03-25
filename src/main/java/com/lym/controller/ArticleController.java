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
public class ArticleController extends BaseController {

    private final Logger logger = Logger.getLogger(ArticleController.class);

    private static final String FAILURE = "FAILURE";
    private static final String SUCCESS = "SUCCESS";
    
    @RequiresPermissions("article-list")
    @RequestMapping(value="/article/list", method=RequestMethod.GET)
    @ResponseBody
    public BaseVo addUser(HttpServletRequest request) {
        try {
            System.out.println("---------article----------");
        } catch (Exception e) {
            // 用BaseController捕获登录和授权异常后，这里不会再捕获。
            // 这里只捕获登录/授权以外的异常。
            e.printStackTrace();
        }
        return null;
    }
    
}
