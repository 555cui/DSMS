package com.zeroclub.service;

import com.zeroclub.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    boolean check(Schedule schedule);
    Schedule save(Schedule schedule);
    void delete(Schedule schedule);
    List<Schedule> getList(Schedule schedule);
}
