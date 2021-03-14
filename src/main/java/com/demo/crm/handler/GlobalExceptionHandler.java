package com.demo.crm.handler;

import com.demo.crm.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器增强，声明本类为全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    //定义方法，处理发生的异常
    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public Map doLoginException(HttpServletResponse response, Exception e){
        Map<String,String> map = new HashMap<>();
        String msg = e.getMessage();
        System.out.println(msg);
        map.put("msg",msg);
        e.printStackTrace();
        return map;
    }
    //定义方法，处理发生的异常
    @ExceptionHandler
    @ResponseBody
    public Map doException(HttpServletResponse response, Exception e){
        Map<String,String> map = new HashMap<>();
        String msg = e.getMessage();
        System.out.println(msg);
        map.put("msg","系统故障，请稍后登录");
        e.printStackTrace();
        return map;
    }

}
