package com.zeroclub.test;

import com.zeroclub.entity.*;
import com.zeroclub.service.TemplateService;
import com.zeroclub.utils.TestUser;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class TemplateServiceTest extends BaseTest{
    @Resource
    private TemplateService templateService;

    private Logger logger = Logger.getLogger("TemplateServiceTest");

    @Test
    @Transactional
    @Rollback
    public void testGetTemplateGroupList(){
        User user = TestUser.getTestUser();
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setUser(user);
        List<TemplateGroup> list = templateService.getList(templateGroup);
        for (TemplateGroup group:list){
            logger.info(group.getName());
            for (Template template: group.getTemplate()){
                logger.info(template.getName());
            }
        }
    }
    @Test
    @Transactional
    @Rollback
    public void testNewTemplateGroup(){
        User user = TestUser.getTestUser();
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setUser(user);
        templateGroup.setName("test");
        templateService.save(templateGroup);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteTemplateGroup(){
        User user = TestUser.getTestUser();
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setId("test");
        templateGroup.setUser(user);
        templateService.delete(templateGroup);
    }

    @Test
    @Transactional
    @Rollback
    public void testNewTemplate(){
        User user = TestUser.getTestUser();
        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setId("test");

        Template template = new Template();
        template.setName("test1");
        template.setUser(user);
        template.setGroup(templateGroup);
        template.setWidth(800);
        template.setHeight(600);
        template.setBackgroundColor("#000000");

        templateService.save(template);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteTemplate(){
        User user = TestUser.getTestUser();
        Template template = new Template();
        template.setId("test");
        template.setUser(user);
        templateService.delete(template);
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateTemplate(){
        User user = TestUser.getTestUser();
        Template template = new Template();
        template.setId("test");
        template.setUser(user);
        template.setName("testtest");

        TemplateElement templateElement = new TemplateElement();
        templateElement.setLeft(100);
        templateElement.setTop(100);
        templateElement.setWidth(100);
        templateElement.setHeight(100);
        templateElement.setzIndex(1);
        Media media = new Media();
        media.setId("67e45743-cbe6-4962-92b0-9d8231d265ec");
        templateElement.setMedia(media);
        List<TemplateElement> list = new ArrayList<TemplateElement>();
        list.add(templateElement);
        template.setElement(list);

        templateService.save(template);
    }
}
