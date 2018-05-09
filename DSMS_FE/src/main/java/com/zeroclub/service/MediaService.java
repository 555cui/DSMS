package com.zeroclub.service;

import com.zeroclub.entity.Media;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MediaService {
    Media getById(String id);
    List<Media> getMediaList(Media media);
    Media getOne(Media media);
    @Transactional
    Media save(Media media);
    void delete(Media media);
}
