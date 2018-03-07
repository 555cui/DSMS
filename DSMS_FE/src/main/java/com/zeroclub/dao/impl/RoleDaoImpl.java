package com.zeroclub.dao.impl;

import com.zeroclub.dao.RoleDao;
import com.zeroclub.entity.Role;

public class RoleDaoImpl extends DaoBaseImpl<Role> implements RoleDao {
    public RoleDaoImpl(){
        super();
        setNs("Role");
    }
}
