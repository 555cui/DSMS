package com.zeroclub.service;

import com.zeroclub.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    boolean isExist(User user);

    User getOne(User user);
    User getById(String id);

    User save(User user);

    List<User> getList(User user);
    void delete(String id);
    void deleteList(String[] ids);
}
