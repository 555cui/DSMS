package com.zeroclub.util;

import java.util.Date;
import java.util.Map;

public interface ReturnMap {
    Map<String, Object> getSuccessReturn(Object data);
    Map<String, Object> getFalieReturn(int code, String msg, Object data);
    Map<String, Object> getSelectParam(Object data, Integer base, Integer size,
                                       Date createStart, Date createEnd,
                                       Date updateStart, Date updateEnd);
}
