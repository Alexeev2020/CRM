package com.demo.crm.settings.web.controller;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.domain.User;
import com.demo.crm.settings.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/settings/user")
public class UserController{
    @Resource
    private UserService userService;
    /*登陆验证方法*/
    @RequestMapping("/login.do")
    @ResponseBody
    private Map login(HttpServletRequest request) throws LoginException {

        Map<String,String> map = new HashMap<>();
        System.out.println("进入登录验证阶段");

        //核心查询与处理方法
        User user = userService.login(request);

        request.getSession().setAttribute("user",user);
        map.put("success","1");
        return map;
    }
}
