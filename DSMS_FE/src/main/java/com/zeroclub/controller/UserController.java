package com.zeroclub.controller;

import com.zeroclub.entity.User;
import com.zeroclub.service.UserService;
import com.zeroclub.util.ReturnMap;
import com.zeroclub.util.UUIDTools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String login(@PathVariable("id") String userId, ModelMap model){
        User user = new User();
        user.setId(userId);
        user = userService.getOne(user);
        if (user !=null){
            model.addAllAttributes(returnMap.getSuccessReturn(user));
        }
        model.addAllAttributes(returnMap.getFalieReturn(1, "user is not exist", null));
        return "index";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String checkPassword(@RequestParam Map<String, Object> param, ModelMap model){
        User user = new User();
        user.setName((String)param.get("name"));
        user.setPassword((String)param.get("password"));
        if (userService.checkPassword(user)) {
            user = userService.getOne(user);
            model.addAllAttributes(returnMap.getSuccessReturn(user.getId()));
        }
        else model.addAllAttributes(returnMap.getFalieReturn(1,"login faile",null));
        return "error";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Map registUser(User user){
        if (!userService.isExist(user)){
            user.setId(uuidTools.getUUID());
            user = userService.save(user);
            return returnMap.getSuccessReturn(user);
        }
        return returnMap.getFalieReturn(2, "user is exist", null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateUser(@PathVariable("id") String userId, @RequestParam User user, ModelMap model){
        user.setId(userId);
        if (userService.isExist(user)){
            userService.save(user);
            model.addAllAttributes(returnMap.getSuccessReturn(null));
        }
        model.addAllAttributes(returnMap.getFalieReturn(1,"user is not exist", null));
        return "error";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getList(@RequestParam Map<String, Object> param, ModelMap model){
        List<User> users = userService.filter(param);
        model.addAllAttributes(returnMap.getSuccessReturn(users));
        return "error";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map deleteUser(@PathVariable("id") String userId){
        User user = new User();
        user.setId(userId);
        if (userService.isExist(user)){
            userService.deleteOne(user);
            return returnMap.getSuccessReturn(null);
        }
        return returnMap.getFalieReturn(1, "user is not exist", null);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public Map deleteList(String[] ids) {
        if (userService.deleteList(ids).equals("success")) {
            return returnMap.getSuccessReturn(null);
        }
        return returnMap.getFalieReturn(3, "some errors", null);
    }
}
