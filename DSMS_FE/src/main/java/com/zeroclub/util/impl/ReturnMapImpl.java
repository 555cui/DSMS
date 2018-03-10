package com.zeroclub.util.impl;

import com.alibaba.fastjson.JSON;
import com.zeroclub.util.ReturnMap;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReturnMapImpl implements ReturnMap {
    public Map<String, Object> getSuccessReturn(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        result.put("code", 0);
        result.put("msg", "success");
        return result;
    }

    public Map<String, Object> getFalieReturn(int code, String msg, Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    public Map<String, Object> getSelectParam (Object data, Integer base, Integer size,
                                              Date createStart, Date createEnd,
                                              Date updateStart, Date updateEnd) {
        Map<String, Object> result =new HashMap<String, Object>();
        Map<String, Object> page =new HashMap<String, Object>();
        Map<String, Object> create =new HashMap<String, Object>();
        Map<String, Object> update =new HashMap<String, Object>();

        result.put("data", data);
        if (base != null && size != null){
            page.put("base", base);
            page.put("size", size);
            result.put("page", page);
        } else {
            result.put("page", null);
        }

        if (createStart == null && createEnd == null){
            result.put("create", null);
        } else {
            create.put("start", createStart);
            create.put("end", createEnd);
            result.put("create", create);
        }

        if (updateStart == null && updateEnd == null){
            result.put("update", null);
        } else {
            update.put("start", updateStart);
            update.put("end", updateEnd);
            result.put("update", update);
        }

        return result;
    }
}
