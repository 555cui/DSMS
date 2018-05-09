package com.zeroclub.controller;

import com.zeroclub.entity.Template;
import com.zeroclub.entity.User;
import com.zeroclub.service.CashService;
import com.zeroclub.service.TemplateService;
import com.zeroclub.util.ReturnMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/template")
public class TemplateController {
    @Resource
    private TemplateService templateService;
    @Autowired
    private CashService cashService;

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public Map checkTemplateName(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody Template template
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        template.setUser(user);

        if (templateService.isExistTemplate(template)){
            return ReturnMap.getFalieReturn(41, "模板已存在");
        }
        else return ReturnMap.getSuccessReturn("模板名可以使用");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map getTemplateById(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String templateId
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        Template template = new Template();
        template.setUser(user);
        template.setId(templateId);

        template = templateService.getOne(template);

        return ReturnMap.getSuccessReturn(template);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Map newTemplate(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody Template template
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        template.setUser(user);
        templateService.save(template);

        return ReturnMap.getSuccessReturn(template);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteTemplate(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String templateId
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        Template template = new Template();
        template.setUser(user);
        template.setId(templateId);

        templateService.delete(template);

        return ReturnMap.getSuccessReturn("success delete");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map updateTemplate(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id,
            @RequestBody Template template
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        template.setUser(user);
        template.setId(id);

        template = templateService.save(template);

        return ReturnMap.getSuccessReturn(template);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteList(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody List<Template> templates
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        for(Template template: templates){
            template.setUser(user);
            templateService.delete(template);
        }

        return ReturnMap.getSuccessReturn("success delete");
    }
}
