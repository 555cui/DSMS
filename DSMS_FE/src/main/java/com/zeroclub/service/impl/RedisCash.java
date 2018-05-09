package com.zeroclub.service.impl;

import com.zeroclub.service.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("cashService")
public class RedisCash implements CashService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, 1800, TimeUnit.SECONDS);
    }

    public String set(Object value) {
        String key = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, value, 1800, TimeUnit.SECONDS);
        return key;
    }

    public Object get(String key) {
        if (key == null) return null;
        Object o = redisTemplate.opsForValue().get(key);
        if (o==null)return null;
        redisTemplate.expire(key, 1800, TimeUnit.SECONDS);
        return o;
    }

    public Object get(String key, int time) {
        if (key == null) return null;
        Object o = redisTemplate.opsForValue().get(key);
        if (o==null)return null;
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
        return o;
    }

    public Object read(String key) {
        if (key == null)return null;
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
