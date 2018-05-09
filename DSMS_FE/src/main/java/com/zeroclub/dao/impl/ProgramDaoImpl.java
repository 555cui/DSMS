package com.zeroclub.dao.impl;

import com.zeroclub.dao.ProgramDao;
import com.zeroclub.entity.Program;
import com.zeroclub.entity.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProgramDaoImpl extends DaoBaseImpl<Program> implements ProgramDao {
    ProgramDaoImpl(){
        setNs("Program");
    }

    public Program getOne(Schedule schedule) {
        return this.getSqlSession().selectOne("com.zeroclub.dao.ProgramDao.getOne", schedule);
    }

    public List<Program> getList(Schedule schedule) {
        return this.getSqlSession().selectList("com.zeroclub.dao.ProgramDao.getList", schedule);
    }

    public void delete(Schedule schedule) {
        this.getSqlSession().delete("com.zeroclub.dao.ProgramDao.delete", schedule);
    }

    public void insert(Schedule schedule) {
        this.getSqlSession().insert("com.zeroclub.dao.Program.insert", schedule);
    }
}
