package com.zeroclub.controller;

import com.zeroclub.entity.Media;
import com.zeroclub.entity.User;
import com.zeroclub.service.CashService;
import com.zeroclub.service.MediaService;
import com.zeroclub.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class UploadController {
    @Resource
    private MediaService mediaService;
    @Autowired
    private CashService cashService;
    @Autowired
    private PathConfig pathConfig;

    @RequestMapping(value = "/{type}", method = RequestMethod.POST)
    @ResponseBody
    public Map uploadFile (
            @RequestParam("file")MultipartFile tempFile,
            @CookieValue(value="dsms_token", required=false) String token,
            @PathVariable("type") String type
    ){
        User user = (User)cashService.get(token);
        if (user == null)return ReturnMap.getFalieReturn(10, "你还没登陆");

        Media media = MediaInfo.getInfo(tempFile, type, user, pathConfig.getPath());
        if (media == null)return  ReturnMap.getFalieReturn(32, "create file error");
        media = mediaService.save(media);

        return ReturnMap.getSuccessReturn(media);
    }
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String uploadTest(@RequestParam("file")MultipartFile tempFile){
        if (MediaInfo.check(tempFile))return "success";
        return "error";
    }
}
