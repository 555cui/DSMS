package com.zeroclub.service.impl;

import com.alibaba.fastjson.JSON;
import com.zeroclub.dao.ProgramDao;
import com.zeroclub.dao.ScheduleDao;
import com.zeroclub.entity.Schedule;
import com.zeroclub.service.ScheduleService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Resource
    private ScheduleDao scheduleDao;
    @Resource
    private ProgramDao programDao;

    public boolean check(Schedule schedule) {
        List<Schedule> list = scheduleDao.getList(schedule);
        return list.size()<=0;
    }

    public List<Schedule> getList(Schedule schedule){
        return scheduleDao.getList(schedule);
    }

    public void deleteList(String[] ids) {
        scheduleDao.deleteList(ids);
    }

    @Transactional
    public Schedule save(Schedule schedule) {
        if (schedule.getId()==null || "".equals(schedule.getId())){
            schedule.setId(UUID.randomUUID().toString());
            scheduleDao.insert(schedule);
            if (schedule.getProgram().size()>0)
                programDao.insert(schedule);
            return schedule;
        }
        scheduleDao.update(schedule);
        programDao.delete(schedule);
        programDao.insert(schedule);
        return schedule;
    }

    public void delete(Schedule schedule) {
        scheduleDao.delete(schedule);
    }
}
