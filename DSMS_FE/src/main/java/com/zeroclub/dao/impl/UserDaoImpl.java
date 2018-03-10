package com.zeroclub.dao.impl;

import com.zeroclub.dao.UserDao;
import com.zeroclub.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends DaoBaseImpl<User> implements UserDao {
    public UserDaoImpl(){
        super();
        setNs("User");
    }
}
