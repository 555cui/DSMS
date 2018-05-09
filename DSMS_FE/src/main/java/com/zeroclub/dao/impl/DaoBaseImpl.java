package com.zeroclub.dao.impl;

import com.zeroclub.dao.DaoBase;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public class DaoBaseImpl<T> extends SqlSessionDaoSupport implements DaoBase<T> {
    private String ns;

    void setNs(String ns){
        this.ns = ns;
    }

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public void insert(T t) {
        this.getSqlSession().insert("com.zeroclub.dao."+ns+"Dao.insert", t);
    }

    public void delete(String id) {
        this.getSqlSession().delete("com.zeroclub.dao."+ns+"Dao.delete", id);
    }

    public void deleteList(String[] ids) {
        this.getSqlSession().delete("com.zeroclub.dao."+ns+"Dao.deleteList", ids);
    }

    public T getOne(T t) {
        return this.getSqlSession().selectOne("com.zeroclub.dao."+ns+"Dao.getOne", t);
    }

    public T getById(String id) {
        return this.getSqlSession().selectOne("com.zeroclub.dao."+ns+"Dao.getById", id);
    }

    public List<T> getList(T t) {
        return this.getSqlSession().selectList("com.zeroclub.dao."+ns+"Dao.getList", t);
    }
    public void update(T t) {
        this.getSqlSession().update("com.zeroclub.dao"+ns+"Dao.update", t);
    }
}
