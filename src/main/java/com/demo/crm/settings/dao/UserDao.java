package com.demo.crm.settings.dao;

import com.demo.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
   User loginQuery (@Param("loginAct") String loginAct, @Param("loginPwd") String loginPwd);
   List<User> selectUsers();

}
