package com.zeroclub.dao.impl;

import com.zeroclub.dao.TemplateGroupDao;
import com.zeroclub.entity.TemplateGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateGroupDaoImpl extends DaoBaseImpl<TemplateGroup> implements TemplateGroupDao {
    public TemplateGroupDaoImpl(){
        this.setNs("TemplateGroupDao");
    }

    public void delete(TemplateGroup templateGroup) {
        this.getSqlSession().delete("com.zeroclub.dao.TemplateGroup.delete", templateGroup);
    }
}
