package com.zjy.fb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FbApplication {

    public static void main(String[] args) {
        SpringApplication.run(FbApplication.class, args);
        System.out.println("启动成功");
    }

}
