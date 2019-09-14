/**
 * uifuture.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.uifuture.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 传输测试的实体类
 *
 * @author chenhx
 * @version User.java, v 0.1 2018-08-08 下午 3:14
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -5135011481747489263L;
    private String username;
    private String password;
    private Integer age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}