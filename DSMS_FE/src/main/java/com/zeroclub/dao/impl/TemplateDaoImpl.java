package com.zeroclub.dao.impl;

import com.zeroclub.dao.TemplateDao;
import com.zeroclub.entity.Template;
import org.springframework.stereotype.Repository;

@Repository
public class TemplateDaoImpl extends DaoBaseImpl<Template> implements TemplateDao {
    public TemplateDaoImpl(){
        this.setNs("Template");
    }

    public void deleteElement(String id) {
        this.getSqlSession().delete("com.zeroclub.dao.TemplateDao.deleteElement", id);
    }

    public void insertElement(Template template) {
        this.getSqlSession().insert("com.zeroclub.dao.TemplateDao.insertElement", template);
    }

    public void delete(Template template) {
        this.getSqlSession().delete("com.zeroclub.dao.TemplateDao.delete", template);
    }
}
