package com.zeroclub.util.impl;

import java.util.UUID;

class Token {
    private String id;
    private long timestamp;

    Token(){
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis() + 1800;
    }

    String getId(){
        if (isCanUse()){
            this.timestamp = System.currentTimeMillis() + 1800;
            return id;
        }
        return null;
    }

    private boolean isCanUse(){
        return System.currentTimeMillis() < this.timestamp;
    }
}
