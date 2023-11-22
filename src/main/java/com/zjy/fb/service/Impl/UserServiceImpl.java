package com.zjy.fb.service.Impl;

import com.zjy.fb.entity.User;
import com.zjy.fb.mapper.UserMapper;
import com.zjy.fb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JavaMailSender javaMailSender;

    private Map<String, String> verificationCodes = new HashMap<>();

    public boolean registerUser(User user) {
        // 验证用户名和邮箱不为空
        if (user.getUsername() == null || user.getEmail() == null) {
            return false;
        }

        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return false;
        }

        // 检查邮箱是否已存在
        if (userMapper.findByEmail(user.getEmail()) != null) {
            return false;
        }

        // 生成随机ID
        Random rand = new Random();
        int randomId = rand.nextInt(100000);
        user.setId(randomId);

        // 对密码进行MD5加密
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        userMapper.save(user);
        return true;
    }

    @Override
    public boolean sendVerificationCode(String email) {
        if (verificationCodes.containsKey(email)) {
            long expirationTime = Long.parseLong(verificationCodes.get(email));
            if (System.currentTimeMillis() < expirationTime) {
                return false; // 10分钟内已发送过验证码
            }
        }

        String verificationCode = generateVerificationCode();
        if (sendEmail(email, "验证码邮件", "您的验证码是：" + verificationCode)) {
            long expirationTime = System.currentTimeMillis() + 600000; // 10分钟后过期
            verificationCodes.put(email, String.valueOf(expirationTime));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean loginUser(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            // 验证登录密码
            String hashedPassword = hashPassword(password);
            return hashedPassword.equals(user.getPassword());
        }
        return false;
    }

    @Override
    public boolean isEmailRegistered(String email) {
        User user = userMapper.findByEmail(email);
        return user != null;
    }

    private String generateVerificationCode() {
        // 生成随机验证码逻辑
        Random rand = new Random();
        int code = rand.nextInt(900000) + 100000;
        return String.valueOf(code);
    }


    //md5加密
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xFF & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean sendEmail(String to, String subject, String content) {
        if (to == null || to.isEmpty() || !to.contains("@")) {
            // 邮件地址为空或无效
            return false;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1532328736@qq.com"); // 设置邮件的地址
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}