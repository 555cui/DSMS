package com.zeroclub.dao;

import com.zeroclub.entity.Device;

public interface DeviceDao extends DaoBase<Device> {
    void delete(Device device);
}
