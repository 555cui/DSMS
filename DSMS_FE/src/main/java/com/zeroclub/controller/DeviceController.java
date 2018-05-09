package com.zeroclub.controller;

import com.zeroclub.entity.Device;
import com.zeroclub.entity.DeviceState;
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
@RequestMapping("/device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;
    @Autowired
    private CashService cashService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map getDevice(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        Device device = new Device();
        device.setId(id);
        device.setUser(user);

        device=deviceService.getOne(device);
        return ReturnMap.getSuccessReturn(device);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Map insertDevice(@RequestBody Device device){
        User user = userService.getOne(device.getUser());
        device.setUser(user);
        device=deviceService.save(device);
        return ReturnMap.getSuccessReturn(device);
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public Map check(@RequestBody Device device){
        User user = userService.getOne(device.getUser());
        device.setUser(user);
        device = deviceService.getOne(device);
        if (device==null)
            return ReturnMap.getFalieReturn(63, "设备不存在");
        return ReturnMap.getSuccessReturn(device);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteDevice(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        Device device = new Device();
        device.setId(id);
        device.setUser(user);

        deviceService.delete(device);
        return ReturnMap.getSuccessReturn("delete success");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteList(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody List<Device> list
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        for(Device device: list){
            device.setUser(user);
            deviceService.delete(device);
        }
        return ReturnMap.getSuccessReturn("delete success");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map updateDevice(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id,
            @RequestBody Device device
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        device.setId(id);
        device.setUser(user);

        device = deviceService.save(device);

        return ReturnMap.getSuccessReturn(device);
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public Map checkName(@RequestBody Device device){
        if (device.getUser()==null)
            return ReturnMap.getFalieReturn(14, "请求数据不足");
        else if (device.getUser().getName()==null || "".equals(device.getUser().getName()))
            return ReturnMap.getFalieReturn(14, "请求数据不足");
        else if (device.getUser().getPassword()==null || "".equals(device.getUser().getPassword()))
            return ReturnMap.getFalieReturn(14, "请求数据不足");
        User user = userService.getOne(device.getUser());
        if (user == null)
            return ReturnMap.getFalieReturn(1, "用户名或密码错误");
        device.setUser(user);
        device = deviceService.getOne(device);
        if (device!=null)
            return ReturnMap.getFalieReturn(41, "设备名已存在");
        return ReturnMap.getSuccessReturn("设备名可以使用");
    }

    @RequestMapping(value = "/state", method = RequestMethod.POST)
    @ResponseBody
    public Map uploadState(@RequestBody DeviceState deviceState){
        if (deviceState.getUrl()==null){
            DeviceState d = (DeviceState)cashService.get(deviceState.getDevice().getId());
            deviceState.setUrl(d.getUrl());
        }
        cashService.set(deviceState.getDevice().getId(), deviceState, 5);
        return ReturnMap.getSuccessReturn("success update");
    }

    @RequestMapping(value = "/{id}/state", method = RequestMethod.GET)
    @ResponseBody
    public Map getState(@PathVariable("id") String id){
        DeviceState state = (DeviceState)cashService.read(id);
        if (state!=null){
            return ReturnMap.getSuccessReturn(state);
        }
        return ReturnMap.getFalieReturn(69, "offline");
    }
}
