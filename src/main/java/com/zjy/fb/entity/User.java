package com.zjy.fb.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
public class User {
    //唯一id
    @Id
    //主键生成,自动分配唯一一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "id_generator")
    //自定义主键生成策略
    @GenericGenerator(name = "id_generator", strategy = "assigned")
    //它指定了主键 id 的一些属性,包括列名为 "id",不可更新 (updatable = false),不可为空 (nullable = false)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "邮箱不能为空")
    private String email;

    private String password;
}