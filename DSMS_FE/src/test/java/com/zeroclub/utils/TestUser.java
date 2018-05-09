package com.zeroclub.utils;

import com.zeroclub.entity.User;

public class TestUser {
    public static User getTestUser(){
        User user = new User();
        user.setId("00000000-0000-0000-0000-00000001");
        user.setName("test");
        return user;
    }
}
