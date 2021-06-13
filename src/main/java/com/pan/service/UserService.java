package com.pan.service;

import com.pan.pojo.User;

public interface UserService {

    User checkUser(String username, String password);
}
