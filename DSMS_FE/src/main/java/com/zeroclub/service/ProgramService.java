package com.zeroclub.service;

import com.zeroclub.entity.Program;
import com.zeroclub.entity.Schedule;

import java.util.List;

public interface ProgramService {
    List<Program> getList(Schedule schedule);
    Program getOne(Schedule schedule);
}
