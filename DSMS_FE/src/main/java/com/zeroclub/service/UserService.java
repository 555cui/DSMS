package com.zeroclub.service;

import com.zeroclub.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    boolean checkPassword(User user);
    boolean checkRole(User user, String action);
    boolean isExist(User user);

    User getOne(User user);
    User getById(String id);
    List<User> filter(Map<String, Object> param);

    User save(User user);
    String deleteOne(User user);
    String deleteList(String[] ids);
}
