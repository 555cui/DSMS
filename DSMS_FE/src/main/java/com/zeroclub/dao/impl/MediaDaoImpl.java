package com.zeroclub.dao.impl;

import com.zeroclub.dao.MediaDao;
import com.zeroclub.entity.Media;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MediaDaoImpl extends DaoBaseImpl<Media> implements MediaDao {
    public MediaDaoImpl(){
        setNs("Media");
    }

    public void deleteGroup(String id) {
        this.getSqlSession().delete("com.zeroclub.dao.deleteGroup", id);
    }

    public void insertGroup(Media media) {
        this.getSqlSession().insert("com.zeroclub.dao.insertGroup", media);
    }
}
