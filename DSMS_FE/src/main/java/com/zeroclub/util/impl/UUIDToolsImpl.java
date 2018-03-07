package com.zeroclub.util.impl;

import com.zeroclub.util.UUIDTools;

import java.util.UUID;

public class UUIDToolsImpl implements UUIDTools {
    public String getUUID() {
        return UUID.randomUUID().toString();
    }
}
