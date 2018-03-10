package com.zeroclub.dao.impl;

import com.zeroclub.dao.GroupDao;
import com.zeroclub.entity.Group;
import org.springframework.stereotype.Repository;

//@Repository
public class GroupDaoImpl extends DaoBaseImpl<Group> implements GroupDao {
    public GroupDaoImpl(){
        super();
        setNs("Group");
    }
}
