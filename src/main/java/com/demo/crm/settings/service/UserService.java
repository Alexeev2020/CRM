package com.demo.crm.settings.service;

import com.demo.crm.exception.LoginException;
import com.demo.crm.settings.domain.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface UserService {
    User login(HttpServletRequest request) throws LoginException;
    List<User> findUsers();
}
