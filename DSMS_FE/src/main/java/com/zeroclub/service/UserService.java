package com.zeroclub.service;

import com.zeroclub.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    boolean checkPassword(User user);
    boolean checkRole(User user, String action);
    boolean isExist(User user);

    List<User> getUserPage(User user, int base,int size);
    List<User> filter(Map<String, Object> param);

    String save(User user);
    String deleteOne(String id);
    String deleteList(String[] ids);
}
