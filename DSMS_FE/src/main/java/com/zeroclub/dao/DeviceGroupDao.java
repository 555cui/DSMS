package com.zeroclub.dao;

import com.zeroclub.entity.DeviceGroup;

public interface DeviceGroupDao extends DaoBase<DeviceGroup> {
    void delete(DeviceGroup deviceGroup);
}
