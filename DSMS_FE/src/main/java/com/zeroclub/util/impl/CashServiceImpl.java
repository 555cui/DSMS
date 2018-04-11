package com.zeroclub.util.impl;

import com.zeroclub.util.CashService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("cash")
public class CashServiceImpl implements CashService {
    private static Map<String, Token> map = new HashMap<String, Token>();

    public int check(String id, String token) {
        String token1 = map.get(id).getId();
        if (token == null){
            return 10;
        }
        if (token1 == null){
            map.remove(id);
            return 11;
        }
        if (!token.equals(token1)){
            return 12;
        }
        return 0;
    }

    public String set(String key) {
        map.put(key, new Token());
        return map.get(key).getId();
    }

    public void remove(String id) {
        map.remove(id);
    }
}
