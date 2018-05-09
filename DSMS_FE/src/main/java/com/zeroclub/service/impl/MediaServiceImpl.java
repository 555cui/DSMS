package com.zeroclub.service.impl;

import com.alibaba.fastjson.JSON;
import com.zeroclub.dao.MediaDao;
import com.zeroclub.entity.Media;
import com.zeroclub.service.MediaService;
import com.zeroclub.util.PathConfig;
import com.zeroclub.util.ReturnMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class MediaServiceImpl implements MediaService {
    Logger logger = Logger.getLogger("com.zeroclub.service.MediaService");
    @Resource
    private MediaDao mediaDao;

    @Autowired
    private PathConfig pathConfig;

    public Media getById(String id) {
        return mediaDao.getById(id);
    }

    public List<Media> getMediaList(Media media) {
        return mediaDao.getList(media);
    }

    public Media getOne(Media media) {
        return mediaDao.getOne(media);
    }

    public Media save(Media media) {
        if (media.getId() == null || "".equals(media.getId())){
            media.setId(UUID.randomUUID().toString());
            mediaDao.insert(media);
            if (media.getGroup() != null && media.getGroup().size()>0){
                mediaDao.insertGroup(media);
            }
        }
        else {
            mediaDao.update(media);
            if (media.getGroup() != null){
                mediaDao.deleteGroup(media.getId());
                if (media.getGroup().size()>0){
                    mediaDao.insertGroup(media);
                }
            }
        }
        return media;
    }

    public void delete(Media media) {
        File file = new File(pathConfig.getPath()+media.getUrl());
        if (file.exists()){
            file.delete();
        }
        file = new File(pathConfig.getPath()+media.getImageUrl());
        if (file.exists()){
            file.delete();
        }
        mediaDao.delete(media.getId());
    }
}
