package com.zeroclub.dao.impl;

import com.zeroclub.dao.DeviceDao;
import com.zeroclub.entity.Device;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceDaoImpl extends DaoBaseImpl<Device> implements DeviceDao {
    public DeviceDaoImpl(){
        this.setNs("Device");
    }

    public void delete(Device device) {
        this.getSqlSession().delete("com.zeroclub.dao.DeviceDao.delete", device);
    }
}
