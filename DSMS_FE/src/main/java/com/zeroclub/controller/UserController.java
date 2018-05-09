package com.zeroclub.controller;

import com.zeroclub.entity.User;
import com.zeroclub.service.CashService;
import com.zeroclub.service.UserService;
import com.zeroclub.util.ReturnMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private CashService cashService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Map gerUserInfo(@CookieValue(value = "dsms_token", required = false) String token) {
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        return ReturnMap.getSuccessReturn(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public Map updateUser(
            @RequestBody User userN,
            @CookieValue(value = "dsms_token", required = false) String token){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        userN.setId(user.getId());
        userN = userService.save(userN);
        return ReturnMap.getSuccessReturn(userN);
    }
}
