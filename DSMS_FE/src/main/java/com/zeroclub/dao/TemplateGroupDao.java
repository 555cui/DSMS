package com.zeroclub.dao;

import com.zeroclub.entity.TemplateGroup;

public interface TemplateGroupDao extends DaoBase<TemplateGroup> {
    void delete(TemplateGroup templateGroup);
}
