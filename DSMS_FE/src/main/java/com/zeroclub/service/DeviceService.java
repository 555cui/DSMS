package com.zeroclub.service;

import com.zeroclub.entity.Device;
import com.zeroclub.entity.DeviceGroup;

import java.util.List;

public interface DeviceService {
    Device save(Device device);
    DeviceGroup save(DeviceGroup deviceGroup);

    void delete(Device device);
    void delete(DeviceGroup deviceGroup);

    Device getOne(Device device);
    DeviceGroup getOne(DeviceGroup deviceGroup);
    List<Device> getList(Device device);
    List<DeviceGroup> getList(DeviceGroup deviceGroup);
}
