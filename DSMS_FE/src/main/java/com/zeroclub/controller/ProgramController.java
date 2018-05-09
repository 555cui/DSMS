package com.zeroclub.controller;

import com.zeroclub.entity.*;
import com.zeroclub.service.DeviceService;
import com.zeroclub.service.ProgramService;
import com.zeroclub.service.impl.RedisCash;
import com.zeroclub.util.ReturnMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/program")
public class ProgramController {
    @Resource
    private ProgramService programService;
    @Resource
    private RedisCash cashService;
    @Resource
    private DeviceService deviceService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Map getProgramList(
            @CookieValue(value = "dsms_token", required = false)String token,
            @RequestBody Schedule schedule
    ){
        User user = (User)cashService.get(token);
        if (user==null)return ReturnMap.getFalieReturn(10, "你还没有登陆");
        schedule.setUser(user);

        int[] week = {64, 1, 2, 4, 8, 16, 32};
        Calendar cal = Calendar.getInstance();
        cal.setTime(schedule.getStartDate());
        int w = cal.get(Calendar.DAY_OF_WEEK)-1;
        schedule.setWeek(week[w]);

        return ReturnMap.getSuccessReturn(programService.getList(schedule));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    private Map getPlaying(
            @PathVariable String id
    ){
        DeviceState state = (DeviceState)cashService.get(id);
        Device device = new Device();
        device.setId(id);
        device = deviceService.getOne(device);
        if (device==null){
            cashService.delete(id);
            return ReturnMap.getFalieReturn(61, "设备未登记");
        }
        if (state==null){
            state = new DeviceState();
            state.setDevice(device);
        }
        else device = state.getDevice();


        Schedule schedule = new Schedule();
        //星期
        int[] week = {64, 1, 2, 4, 8, 16, 32};
        Calendar cal = Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK)-1;
        schedule.setWeek(week[w]);
        //日期
        schedule.setStartDate(cal.getTime());
        //使用startTime保存当前时间
        List<Program> list = new ArrayList<Program>();
        Program program = new Program();
        program.setStartTime(String.format("%02d:00", cal.get(Calendar.HOUR_OF_DAY)));
        list.add(program);
        schedule.setProgram(list);
        //设备
        schedule.setDevice(device);

        program = programService.getOne(schedule);

        if (program!=null){
            state.setType("playing");
            cashService.set(id, state, 60);
            return ReturnMap.getSuccessReturn(program);
        }
        state.setType("ready");
        cashService.set(id, state, 60);
        return ReturnMap.getFalieReturn(60, "no program");
    }
}
