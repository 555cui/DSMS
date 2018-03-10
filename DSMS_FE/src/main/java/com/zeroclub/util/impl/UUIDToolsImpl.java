package com.zeroclub.util.impl;

import com.zeroclub.util.UUIDTools;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDToolsImpl implements UUIDTools {
    public String getUUID() {
        return UUID.randomUUID().toString();
    }
}
