package com.sweng.customerqueue.dao;

import com.sweng.customerqueue.model.User;


public interface UserDao {
    User findByUsername(String username);
}
