package com.zeroclub.controller;

import com.alibaba.fastjson.JSON;
import com.zeroclub.entity.Program;
import com.zeroclub.entity.Schedule;
import com.zeroclub.entity.User;
import com.zeroclub.service.ScheduleService;
import com.zeroclub.service.impl.RedisCash;
import com.zeroclub.util.ReturnMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    @Resource
    private ScheduleService scheduleService;
    Logger logger = Logger.getLogger(ScheduleController.class);

    @Resource
    private RedisCash cashService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Map getSchedule(
            @CookieValue(value = "dsms_token", required = false)String token
    ){
        User user = (User) cashService.get(token);
        if (user==null)return ReturnMap.getFalieReturn(10, "你还没有登陆");
        Schedule schedule = new Schedule();
        schedule.setUser(user);
        logger.info(JSON.toJSONString(schedule));
        return ReturnMap.getSuccessReturn(scheduleService.getList(schedule));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Map insertSchedule(
            @CookieValue(value = "dsms_token", required = false)String token,
            @RequestBody Schedule schedule
    ){
        User user = (User)cashService.get(token);
        if (user==null)return ReturnMap.getFalieReturn(10, "你还没有登陆");

        schedule.setUser(user);

        schedule = scheduleService.save(schedule);

        return ReturnMap.getSuccessReturn(schedule);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map updateSchedule(
            @CookieValue(value = "dsms_token", required = false)String token,
            @RequestBody Schedule schedule,
            @PathVariable("id") String id
    ){
        User user = (User)cashService.get(token);
        if (user==null)return ReturnMap.getFalieReturn(10, "你还没有登陆");

        schedule.setId(id);
        schedule.setUser(user);

        schedule = scheduleService.save(schedule);

        return ReturnMap.getSuccessReturn(schedule);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map deleteSchedule(
            @CookieValue(value = "dsms_token", required = false)String token,
            @PathVariable("id") String id
    ){
        User user = (User)cashService.get(token);
        if (user==null)return ReturnMap.getFalieReturn(10, "你还没有登陆");

        Schedule schedule = new Schedule();
        schedule.setId(id);
        schedule.setUser(user);

        scheduleService.delete(schedule);

        return ReturnMap.getSuccessReturn("delete success");
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public Map checkPorgram(
            @CookieValue(value = "dsms_token", required = false)String token,
            @RequestBody Schedule schedule
    ){
        User user = (User)cashService.get(token);
        if (user==null)return ReturnMap.getFalieReturn(10, "你还没有登陆");
        schedule.setUser(user);

        return ReturnMap.getSuccessReturn(scheduleService.getList(schedule));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map deleteList(
            @CookieValue(value = "dsms_token", required = false)String token,
            @RequestBody String[] ids
    ){
        User user = (User)cashService.get(token);
        if (user==null)return ReturnMap.getFalieReturn(10, "你还没有登陆");
        scheduleService.deleteList(ids);
        return ReturnMap.getSuccessReturn("delete success");
    }
}
