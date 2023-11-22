package com.zjy.fb.service;

import com.zjy.fb.entity.User;


public interface UserService {
    //用户注册
    boolean registerUser(User user);
    //生成一个随机邮箱验证码
    boolean sendVerificationCode(String email);
    //用户登录
    boolean loginUser(String username, String password);
    //检查邮箱是否已被注册
    boolean isEmailRegistered(String email);
}