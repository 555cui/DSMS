package com.zeroclub.dao;

import java.util.List;

public interface DaoBase<T> {
    //insert a entity
    void insert(T t);

    //delete entitys
    void delete(String id);
    void deleteList(String[] ids);

    //select entitys
    T getOne(T t);
    T getById(String id);
    List<T> getList(T t);

    //update a entity
    void update(T t);
}
