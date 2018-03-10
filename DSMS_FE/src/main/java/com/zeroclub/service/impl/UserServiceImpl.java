package com.zeroclub.service.impl;

import com.zeroclub.dao.UserDao;
import com.zeroclub.entity.User;
import com.zeroclub.service.UserService;
import com.zeroclub.util.UUIDTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private UUIDTools uuidTools;
    public boolean checkPassword(User user) {
        if (userDao.getOne(user) != null) return true;
        return false;
    }

    public boolean checkRole(User user, String action) {
        return false;
    }

    public boolean isExist(User user) {
        if (userDao.getOne(user) != null) return true;
        return false;
    }

    public User getOne(User user) {
        return userDao.getOne(user);
    }

    public List<User> filter(Map<String, Object> param) {
        return userDao.getList(param);
    }

    public User save(User user) {
        if (user.getId() != null && !"".equals(user.getId())){
            userDao.update(user);
            return userDao.getOne(user);
        }
        user.setId(uuidTools.getUUID());
        userDao.insert(user);
        return userDao.getOne(user);
    }

    public String deleteOne(User user) {
        userDao.deleteOne(user);
        return "success";
    }

    public String deleteList(String[] ids) {
        userDao.deleteList(ids);
        return "success";
    }
}
