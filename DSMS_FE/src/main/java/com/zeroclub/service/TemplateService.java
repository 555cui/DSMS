package com.zeroclub.service;

import com.zeroclub.entity.Template;
import com.zeroclub.entity.TemplateGroup;

import java.util.List;

public interface TemplateService {
    Template save(Template template);
    void delete(Template template);
    void deleteList(String[] ids);
    Template getOne(Template template);
    Template getById(String id);
    boolean isExistTemplate(Template template);
    boolean isExistGroup(TemplateGroup templateGroup);
    List<Template> getList(Template template);
    List<TemplateGroup> getList(TemplateGroup templateGroup);
    TemplateGroup save(TemplateGroup templateGroup);
    void delete(TemplateGroup templateGroup);
}
