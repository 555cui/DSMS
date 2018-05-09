package com.zeroclub.dao.impl;

import com.zeroclub.dao.DeviceGroupDao;
import com.zeroclub.entity.DeviceGroup;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceGroupDaoImpl extends DaoBaseImpl<DeviceGroup> implements DeviceGroupDao {
    public DeviceGroupDaoImpl(){
        this.setNs("DeviceGroup");
    }

    public void delete(DeviceGroup deviceGroup) {
        this.getSqlSession().delete("com.zeroclub.dao.DeviceGroupDao.delete", deviceGroup);
    }
}
