package com.demo.settings.test;

import com.demo.crm.utils.DateTimeUtil;
import com.demo.crm.utils.MD5Util;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    @Test
    public void testExpiretime () {
        //失效时间
        String expireTime = "2021-10-20 00:00:00";
        //当前系统时间
        String currentTime = DateTimeUtil.getSysTime();
        //使用compareTo比较时间，返回1表示当前系统时间早于或者说小于失效时间，返回1
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);

        /*Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = sdf.format(date);
        System.out.println(d);*/
    }
    @Test
    public void testLockState(){
        String lockState = "0";
        if ("0".equals(lockState)){
            System.out.println("账号被锁定！！！");
        }
    }
    @Test
    public void testPwd(){
        String pwd = "123";
        //MD5加密
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);
    }
}
