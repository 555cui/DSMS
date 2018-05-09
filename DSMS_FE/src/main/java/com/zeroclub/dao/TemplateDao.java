package com.zeroclub.dao;

import com.zeroclub.entity.Template;

public interface TemplateDao extends DaoBase<Template> {
    void deleteElement(String id);
    void insertElement(Template template);
    void delete(Template template);
}
