package com.zeroclub.controller;

import com.zeroclub.entity.Media;
import com.zeroclub.entity.User;
import com.zeroclub.service.CashService;
import com.zeroclub.service.MediaService;
import com.zeroclub.util.ReturnMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/media")
public class MediaController {
    @Resource
    private MediaService mediaService;
    @Autowired
    private CashService cashService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Map getAllMedia(@CookieValue(value = "dsms_token", required = false) String token){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        Media media = new Media();
        media.setUser(user);
        List<Media> list = mediaService.getMediaList(media);
        return ReturnMap.getSuccessReturn(list);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map getMedia(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        Media media = new Media();
        media.setId(id);
        media.setUser(user);
        media = mediaService.getOne(media);
        if (media != null)return ReturnMap.getSuccessReturn(media);
        return ReturnMap.getFalieReturn(20,"media not exist");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map update(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id,
            @RequestBody Media media
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        media.setUser(user);
        media = mediaService.save(media);
        return ReturnMap.getSuccessReturn(media);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public Map delete(
            @CookieValue(value = "dsms_token", required = false) String token,
            @PathVariable("id") String id
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        Media media = mediaService.getById(id);
        media.setUser(user);
        mediaService.delete(media);
        return ReturnMap.getSuccessReturn(media);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public Map deleteList(@RequestBody  List<Media> medias,
                          @CookieValue(value = "dsms_token", required = false) String token){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        for(Media media: medias){
            media.setUser(user);
            mediaService.delete(media);
        }
        return ReturnMap.getSuccessReturn("delete success");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public Map insert(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody Media media
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        media.setUser(user);
        if (media.getType().equals("group")){
            int maxWidth = 0;
            int maxHeight = 0;
            for (Media m: media.getGroup()){
                if (m.getWidth()> maxWidth || m.getHeight()>maxHeight){
                    maxWidth = m.getWidth();
                    maxHeight = m.getHeight();
                }
            }
            media.setHeight(maxHeight);
            media.setWidth(maxWidth);
            media.setUrl(UUID.randomUUID().toString());
            media.setImageUrl(media.getGroup().get(0).getImageUrl());
        }
        mediaService.save(media);
        return ReturnMap.getSuccessReturn(media);
    }

    @RequestMapping(value = "/name", method = RequestMethod.POST)
    @ResponseBody
    public Map checkName(
            @CookieValue(value = "dsms_token", required = false) String token,
            @RequestBody Media media
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");
        media.setUser(user);
        media = mediaService.getOne(media);
        if (media == null)return ReturnMap.getSuccessReturn("媒体名可以使用");
        else return ReturnMap.getFalieReturn(30, "媒体名已被使用");
    }
}
