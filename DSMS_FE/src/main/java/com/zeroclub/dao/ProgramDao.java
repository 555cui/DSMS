package com.zeroclub.dao;

import com.zeroclub.entity.Program;
import com.zeroclub.entity.Schedule;

import java.util.List;

public interface ProgramDao extends DaoBase<Program> {
    Program getOne(Schedule schedule);
    List<Program> getList(Schedule schedule);
    void delete(Schedule schedule);
    void insert(Schedule schedule);
}
