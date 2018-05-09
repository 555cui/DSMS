package com.zeroclub.service;

public interface CashService {
    void set(String key, Object value, long expire);
    void set(String key, Object value);
    String set(Object value);
    Object get(String key);
    Object get(String key, int timeout);
    Object read(String key);
    void delete(String key);
}
