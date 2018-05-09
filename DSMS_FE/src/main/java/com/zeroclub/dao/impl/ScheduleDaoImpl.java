package com.zeroclub.dao.impl;

import com.zeroclub.dao.ScheduleDao;
import com.zeroclub.entity.Schedule;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleDaoImpl extends DaoBaseImpl<Schedule> implements ScheduleDao {
    ScheduleDaoImpl(){
        setNs("Schedule");
    }

    public void delete(Schedule schedule) {
        this.getSqlSession().delete("com.zeroclub.dao.ScheduleDao.delete", schedule);
    }
}
