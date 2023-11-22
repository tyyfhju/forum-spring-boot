package com.zjy.fb.mapper;

import com.zjy.fb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//注册登录的mybatis
public interface UserMapper extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
}