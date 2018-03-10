package com.zeroclub.controller;

import com.alibaba.fastjson.JSON;
import com.zeroclub.util.ReturnMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {
    private Logger logger = Logger.getLogger("com.zeroclub.controller.TestController");
    @Resource
    private ReturnMap returnMap;

    @RequestMapping("/html2json")
    public String html2json(@RequestParam Map<String, Object> param, ModelMap model){
        logger.info(param);
        model.addAllAttributes(param);
        return "test";
    }
}
