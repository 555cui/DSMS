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

    public void deleteOne(T t) {
        this.getSqlSession().delete("com.zeroclub.dao."+ns+"Dao.deleteOne", t);
    }

    public void deleteList(String[] ids) {
        this.getSqlSession().delete("com.zeroclub.dao."+ns+"Dao.deleteList", ids);
    }

    public T getOne(T t) {
        T result = this.getSqlSession().selectOne("com.zeroclub.dao."+ns+"Dao.getOne", t);
        return result;
    }

    public List<T> getList(Map<String, Object> param) {
        List<T> result = this.getSqlSession().selectList("com.zeroclub.dao."+ns+"Dao.getList", param);
        return result;
    }

    public void update(T t) {
        this.getSqlSession().update("com.zeroclub.dao"+ns+"Dao.update", t);
    }
}
