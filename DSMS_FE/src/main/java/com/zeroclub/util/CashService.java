package com.zeroclub.util;

public interface CashService {
    int check(String id, String token);
    String set(String key);
    void remove(String id);
}
