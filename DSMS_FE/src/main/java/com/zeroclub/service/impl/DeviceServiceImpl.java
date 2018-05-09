package com.zeroclub.service.impl;

import com.zeroclub.dao.DeviceDao;
import com.zeroclub.dao.DeviceGroupDao;
import com.zeroclub.entity.Device;
import com.zeroclub.entity.DeviceGroup;
import com.zeroclub.service.DeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Resource
    private DeviceDao deviceDao;
    @Resource
    private DeviceGroupDao deviceGroupDao;

    public Device save(Device device) {
        if (device.getId()==null || "".equals(device.getId())){
            device.setId(UUID.randomUUID().toString());
            deviceDao.insert(device);
        }
        else deviceDao.update(device);
        return device;
    }

    public DeviceGroup save(DeviceGroup deviceGroup) {
        if (deviceGroup.getId()==null || "".equals(deviceGroup.getId())){
            deviceGroup.setId(UUID.randomUUID().toString());
            deviceGroupDao.insert(deviceGroup);
        }
        else deviceGroupDao.update(deviceGroup);
        return deviceGroup;
    }

    public void delete(Device device) {
        deviceDao.delete(device);
    }

    public void delete(DeviceGroup deviceGroup) {
        if (!"default".equals(deviceGroup.getId()))
            deviceGroupDao.delete(deviceGroup);
    }

    public Device getOne(Device device) {
        return deviceDao.getOne(device);
    }

    public DeviceGroup getOne(DeviceGroup deviceGroup) {
        return deviceGroupDao.getOne(deviceGroup);
    }

    public List<Device> getList(Device device) {
        return deviceDao.getList(device);
    }

    public List<DeviceGroup> getList(DeviceGroup deviceGroup) {
        return deviceGroupDao.getList(deviceGroup);
    }
}

