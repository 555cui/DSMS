package com.zeroclub.dao;

import com.zeroclub.entity.Schedule;

public interface ScheduleDao extends DaoBase<Schedule> {
    void delete(Schedule schedule);
}
