package com.zeroclub.service.impl;

import com.zeroclub.dao.DeviceGroupDao;
import com.zeroclub.dao.TemplateGroupDao;
import com.zeroclub.dao.UserDao;
import com.zeroclub.entity.DeviceGroup;
import com.zeroclub.entity.TemplateGroup;
import com.zeroclub.entity.User;
import com.zeroclub.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private TemplateGroupDao templateGroupDao;
    @Resource
    private DeviceGroupDao deviceGroupDao;

    public boolean isExist(User user) {
        return userDao.getOne(user) != null;
    }

    public User getOne(User user) {
        return userDao.getOne(user);
    }

    public List<User> getList(User user) {
        return userDao.getList(user);
    }

    public User getById(String id) {
        return userDao.getById(id);
    }

    @Transactional
    public User save(User user) {
        if (user.getId() != null && !"".equals(user.getId())){
            userDao.update(user);
            return userDao.getOne(user);
        }
        user.setId(UUID.randomUUID().toString());
        userDao.insert(user);
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setId(UUID.randomUUID().toString());
        templateGroup.setName("default");
        templateGroup.setUser(user);
        templateGroupDao.insert(templateGroup);
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setId(UUID.randomUUID().toString());
        deviceGroup.setName("default");
        deviceGroup.setUser(user);
        deviceGroupDao.insert(deviceGroup);
        return user;
    }

    public void delete(String id) {
        userDao.delete(id);
    }

    public void deleteList(String[] ids) {
        userDao.deleteList(ids);
    }
}
