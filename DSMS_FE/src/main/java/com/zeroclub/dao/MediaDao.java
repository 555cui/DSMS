package com.zeroclub.dao;

import com.zeroclub.entity.Media;

public interface MediaDao extends DaoBase<Media> {
    void deleteGroup(String id);
    void insertGroup(Media media);
}
