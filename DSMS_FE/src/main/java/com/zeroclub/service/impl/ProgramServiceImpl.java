package com.zeroclub.service.impl;

import com.zeroclub.dao.ProgramDao;
import com.zeroclub.entity.Program;
import com.zeroclub.entity.Schedule;
import com.zeroclub.service.ProgramService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {
    @Resource
    private ProgramDao programDao;
    public List<Program> getList(Schedule schedule) {
        return programDao.getList(schedule);
    }

    public Program getOne(Schedule schedule) {
        return programDao.getOne(schedule);
    }
}
