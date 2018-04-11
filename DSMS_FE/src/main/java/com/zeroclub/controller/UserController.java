package com.zeroclub.controller;

import com.zeroclub.entity.User;
import com.zeroclub.service.UserService;
import com.zeroclub.util.CashService;
import com.zeroclub.util.ReturnMap;
import com.zeroclub.util.UUIDTools;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private ReturnMap returnMap;
    @Resource
    private UserService userService;
    @Resource
    private UUIDTools uuidTools;
    @Resource
    private CashService cash;

    private Logger logger = Logger.getLogger("com.zeroclub.contorller.UserController");

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map login(@RequestBody User user, HttpServletResponse response){
        logger.info(user.getName());
        logger.info(user.getPassword());
        user = userService.getOne(user);
        if (user != null) {
            String id = user.getId();
            String token = cash.set(id);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/user");
            response.addCookie(cookie);
            return returnMap.getSuccessReturn(id);
        }
        return returnMap.getFalieReturn(1,"user name or password error",null);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Map register(@RequestBody User user, HttpServletResponse response){
        user = userService.save(user);
        String token = cash.set(user.getId());
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/user");
        response.addCookie(cookie);
        return returnMap.getSuccessReturn(user.getId());
    }

    @RequestMapping(value = "/checkName", method = RequestMethod.POST)
    @ResponseBody
    public Map checkUserName(@RequestBody User user){
        if (!userService.isExist(user)){
            return returnMap.getSuccessReturn("name not use");
        }
        return returnMap.getFalieReturn(2,"name is exist", null);
    }

    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
    @ResponseBody
    public Map checkEmail(@RequestBody User user){
        if (!userService.isExist(user)){
            return returnMap.getSuccessReturn("email not use");
        }
        return returnMap.getFalieReturn(3,"email is exist", null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map logout(@PathVariable("id") String id){
        cash.remove(id);
        return returnMap.getSuccessReturn("success");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map gerUserInfo(
            @PathVariable("id") String userId,
            @CookieValue(value="token", required=false) String token
    ){
        int code = cash.check(userId, token);
        if(code != 0){
            return returnMap.getFalieReturn(code,"user logout", null);
        }
        User user = userService.getById(userId);
        user.setPassword("");
        return returnMap.getSuccessReturn(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map updateUser(
            @PathVariable("id") String userId,
            @CookieValue(value="token", required=false) String token,
            User user
    ){
        int code = cash.check(userId, token);
        if(code != 0){
            return returnMap.getFalieReturn(code,"user logout", null);
        }
        if(userService.getById(userId)==null){
            return returnMap.getFalieReturn(2, "user is not exist", null);
        }
        user.setId(userId);
        userService.save(user);
        return returnMap.getSuccessReturn("update success");
    }
}
