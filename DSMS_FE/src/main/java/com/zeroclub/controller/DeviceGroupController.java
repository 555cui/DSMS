package com.zeroclub.controller;

import com.zeroclub.entity.DeviceGroup;
import com.zeroclub.entity.User;
import com.zeroclub.service.CashService;
import com.zeroclub.service.DeviceService;
import com.zeroclub.service.UserService;
import com.zeroclub.util.ReturnMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/deviceGroup")
public class DeviceGroupController {
    @Resource
    private DeviceService deviceService;
    @Autowired
    private CashService cashService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Map getGroupList(@CookieValue(value = "dsms_token", required = false) String token){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setUser(user);

        List<DeviceGroup> list = deviceService.getList(deviceGroup);

        return ReturnMap.getSuccessReturn(list);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Map insertGroup(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody DeviceGroup deviceGroup
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        deviceGroup.setUser(user);
        deviceGroup = deviceService.save(deviceGroup);

        return ReturnMap.getSuccessReturn(deviceGroup);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteGroup(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setId(id);
        deviceGroup.setUser(user);

        deviceService.delete(deviceGroup);

        return ReturnMap.getSuccessReturn("success delete");
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public Map checkName(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody DeviceGroup deviceGroup
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        if (deviceService.getOne(deviceGroup)!=null)
            return ReturnMap.getFalieReturn(51, "device group exist");
        else return ReturnMap.getSuccessReturn("device group not use");
    }

    @RequestMapping(value = "/pass", method = RequestMethod.POST)
    @ResponseBody
    public Map checkPass(@RequestBody User user){
        if ("".equals(user.getName()) || user.getName() == null)
            return ReturnMap.getFalieReturn(1,"用户名或密码错误");
        if ("".equals(user.getPassword()) || user.getPassword() == null)
            return ReturnMap.getFalieReturn(1, "用户名或密码错误");
        user = userService.getOne(user);
        if (user==null)
            return ReturnMap.getFalieReturn(1,"用户名或密码错误");
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setUser(user);
        return ReturnMap.getSuccessReturn(deviceService.getList(deviceGroup));
    }
}
