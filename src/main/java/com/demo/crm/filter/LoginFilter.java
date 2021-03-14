package com.demo.crm.filter;


import com.demo.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        //将req转为HttpServletRequest类型
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //对特殊请求进行放行
        String path = request.getServletPath();
        if ("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
            chain.doFilter(req,resp);
        }else{
            //获取session，并且获取session域中的user对象
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user!=null){
                //已经登录且Session未过期
                chain.doFilter(req,resp);
            }else{
                //没有登录过，重定向到登录页，重定向的路径使用绝对路径
                //使用重定向而不使用转发，转发使用一种特殊的绝对路径，路径前不加项目名，这种路径也叫做内部路径
                //重定向使用的是传统绝对路径的写法
                System.out.println("过滤器拦截并重定向了访问jsp静态资源的非法请求");
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }
    }
}
