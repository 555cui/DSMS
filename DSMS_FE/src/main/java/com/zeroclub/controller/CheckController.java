package com.zeroclub.controller;

import com.zeroclub.entity.User;
import com.zeroclub.service.CashService;
import com.zeroclub.service.UserService;
import com.zeroclub.util.ReturnMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/check")
public class CheckController {
    @Resource
    private UserService userService;
    @Autowired
    private CashService cashService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map login(@RequestBody User user, HttpServletResponse response){
        if ("".equals(user.getName()) || user.getName() == null)
            return ReturnMap.getFalieReturn(1,"用户名或密码错误");
        if ("".equals(user.getPassword()) || user.getPassword() == null)
            return ReturnMap.getFalieReturn(1, "用户名或密码错误");
        user = userService.getOne(user);
        if (user == null)
            return ReturnMap.getFalieReturn(1,"用户名或密码错误");
        String token = cashService.set(user);
        Cookie cookie = new Cookie("dsms_token", token);
        //cookie.setDomain("192.168.1.158");
        cookie.setPath("/");
        response.addCookie(cookie);
        return ReturnMap.getSuccessReturn("login success");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    @ResponseBody
    public Map logout(@CookieValue("dsms_token") String token){
        cashService.delete(token);
        return ReturnMap.getSuccessReturn("success");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Map register(@RequestBody User user, HttpServletResponse response){
        user = userService.save(user);
        user.setPassword("");
        String token = cashService.set(user);
        Cookie cookie = new Cookie("dsms_token", token);
        //cookie.setDomain("192.168.1.158");
        cookie.setPath("/");
        response.addCookie(cookie);
        return ReturnMap.getSuccessReturn(user.getId());
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public Map checkUserName(@RequestBody User user){
        if (!userService.isExist(user)){
            return ReturnMap.getSuccessReturn("用户名可以使用");
        }
        return ReturnMap.getFalieReturn(2,"用户名已存在");
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    @ResponseBody
    public Map checkEmail(@RequestBody User user){
        if (!userService.isExist(user)){
            return ReturnMap.getSuccessReturn("邮箱可以使用");
        }
        return ReturnMap.getFalieReturn(3,"邮箱已被注册");
    }
}
