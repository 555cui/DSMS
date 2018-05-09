package com.zeroclub.util;

import java.util.HashMap;
import java.util.Map;

public class ReturnMap {
    public static Map<String, Object> getSuccessReturn(Object data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        result.put("code", 0);
        result.put("msg", "success");
        return result;
    }

    public static Map<String, Object> getFalieReturn(int code, String msg) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", null);
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }
}
