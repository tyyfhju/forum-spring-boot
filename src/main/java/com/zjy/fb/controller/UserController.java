package com.zjy.fb.controller;

import com.zjy.fb.entity.User;
import com.zjy.fb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if (userService.registerUser(user)) {
            return "Registration successful";//注册成功
        } else {
            return "Username or email already exists";//用户名或邮箱已存在
        }
    }

    //邮箱验证码发送
    @PostMapping("/sendVerificationCode")
    public String sendVerificationCode(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        if (userService.sendVerificationCode(email)) {
            return "Verification code sent";//验证码已发送
        } else {
            return "Failed to send verification code";//验证码发送失败
        }
    }

    ///登录
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (userService.loginUser(username, password)) {
            // 在这里，你应该生成并返回一个令牌（token）
            return "Login successful";//登录成功
        } else {
            return "Login failed";//登录失败
        }
    }
}
