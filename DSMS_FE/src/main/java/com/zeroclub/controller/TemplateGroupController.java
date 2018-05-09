package com.zeroclub.controller;

import com.zeroclub.entity.TemplateGroup;
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
@RequestMapping("templateGroup")
public class TemplateGroupController {
    @Resource
    private TemplateService templateService;
    @Autowired
    private CashService cashService;
    //----------------group-------------------------------------
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Map getTemplateGroupList(@CookieValue(value = "dsms_token", required = false) String token){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        TemplateGroup group = new TemplateGroup();
        group.setUser(user);

        List<TemplateGroup> groupList = templateService.getList(group);

        return ReturnMap.getSuccessReturn(groupList);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Map insertTemplate(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody TemplateGroup templateGroup
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        templateGroup.setUser(user);
        if (templateService.isExistGroup(templateGroup))
            return ReturnMap.getFalieReturn(41, "分组已存在");
        templateService.save(templateGroup);

        return ReturnMap.getSuccessReturn(templateGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteGroup(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        TemplateGroup templateGroup= new TemplateGroup();
        templateGroup.setId(id);
        templateGroup.setUser(user);

        templateService.delete(templateGroup);

        return ReturnMap.getSuccessReturn("success delete");
    }
}
