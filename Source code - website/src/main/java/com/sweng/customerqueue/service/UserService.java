package com.sweng.customerqueue.service;

import com.sweng.customerqueue.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
}
