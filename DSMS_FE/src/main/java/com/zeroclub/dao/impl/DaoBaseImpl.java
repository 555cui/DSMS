package com.zeroclub.dao.impl;

import com.zeroclub.dao.DaoBase;

import java.util.List;
import java.util.Map;

public class DaoBaseImpl<T> implements DaoBase<T> {
    private String ns;

    public void setNs(String ns){
        this.ns = ns;
    }

    public void insert(T t) {

    }

    public void deleteOne(String id) {

    }

    public void deleteList(String[] ids) {

    }

    public T getOne(T t) {
        return null;
    }

    public List<T> getList(Map<String, Object> param) {
        return null;
    }

    public void update(T t) {

    }
}
