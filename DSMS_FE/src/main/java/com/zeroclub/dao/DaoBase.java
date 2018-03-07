package com.zeroclub.dao;

import java.util.List;
import java.util.Map;

public interface DaoBase<T> {
    //insert a entity
    void insert(T t);

    //delete entitys
    void deleteOne(String id);
    void deleteList(String[] ids);

    //select entitys
    T getOne(T t);
    List<T> getList(Map<String, Object> param);

    //update a entity
    void update(T t);
}
