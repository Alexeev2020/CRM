package com.demo.crm.settings.service.impl;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.dao.UserDao;
import com.demo.crm.settings.domain.User;
import com.demo.crm.settings.service.UserService;
import com.demo.crm.utils.DateTimeUtil;

import com.demo.crm.utils.MD5Util;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    /**
     * 进行登录验证操作
     * @param request 请求
     * @return 返回登录成功的用户信息
     * @throws LoginException 登录验证过程中抛出自定义登录异常
     */
    @Override
    public User login(HttpServletRequest request) throws LoginException{
        //获取参数
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //对登录密码进行MD5转码
        loginPwd = MD5Util.getMD5(loginPwd);
        //获取请求ip
        String ip = request.getRemoteAddr();
        System.out.println(ip);
        User user = userDao.loginQuery(loginAct,loginPwd);
        //验证user是否为空，为空则返回 账号或密码错误，请重新输入
        if (user==null){
            throw new LoginException("账号密码错误，请重新输入");
        }
        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效，请联系管理员");
        }
        //验证锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定，请联系管理员");
        }

        //验证ip
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw new LoginException("ip未被允许，请联系管理员");
        }
        return user;
    }

    /**
     * 调用ActivityDao的selectUsers方法获得所有User信息放入List集合中
     */
    @Override
    public List<User> findUsers() {
        List<User> users = userDao.selectUsers();
        return users;
    }
}
