package com.zeroclub.service.impl;

import com.zeroclub.dao.TemplateDao;
import com.zeroclub.dao.TemplateGroupDao;
import com.zeroclub.entity.Template;
import com.zeroclub.entity.TemplateGroup;
import com.zeroclub.service.TemplateService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Resource
    private TemplateDao templateDao;
    @Resource
    private TemplateGroupDao templateGroupDao;

    public Template save(Template template) {
        if (template.getId()!=null && !"".equals(template.getId())){
            template.setUpdateTime(new Date());
            templateDao.update(template);
            templateDao.deleteElement(template.getId());
            if (template.getElement().size()>0)
                templateDao.insertElement(template);
            return template;
        }
        template.setId(UUID.randomUUID().toString());
        templateDao.insert(template);
        return template;
    }

    public void delete(Template template) {
        templateDao.delete(template);
    }

    public void deleteList(String[] ids) {
        templateDao.deleteList(ids);
    }

    public Template getOne(Template template) {
        return templateDao.getOne(template);
    }

    public Template getById(String id) {
        return templateDao.getById(id);
    }

    public boolean isExistTemplate(Template template) {
        return templateDao.getOne(template)!=null;
    }

    public boolean isExistGroup(TemplateGroup templateGroup) {
        return templateGroupDao.getOne(templateGroup)!=null;
    }

    public List<Template> getList(Template template) {
        return templateDao.getList(template);
    }

    public List<TemplateGroup> getList(TemplateGroup templateGroup) {
        return templateGroupDao.getList(templateGroup);
    }

    public TemplateGroup save(TemplateGroup templateGroup) {
        if (templateGroup.getId()!=null && !"".equals(templateGroup.getId())){
            templateGroupDao.update(templateGroup);
            return templateGroup;
        }
        templateGroup.setId(UUID.randomUUID().toString());
        templateGroupDao.insert(templateGroup);
        return templateGroup;
    }

    public void delete(TemplateGroup templateGroup) {
        if (!"default".equals(templateGroup.getId()))
            templateGroupDao.delete(templateGroup);
    }
}
