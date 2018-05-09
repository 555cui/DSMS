package com.zeroclub.util;

import java.util.UUID;

public class Token<T> {
    private String id;
    private T t;
    private int time;

    public Token(T t, int time){
        this.id = UUID.randomUUID().toString();
        this.t = t;
        this.time = time;
    }
}
