package com.zeroclub.test;

import com.zeroclub.entity.User;
import com.zeroclub.service.CashService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class RedisCashTest extends BaseTest {
    @Autowired
    private CashService cashService;
    private Logger logger = Logger.getLogger("com.zeroclub.test.RedisCashTest");

    @Test
    @Transactional
    @Rollback
    public void testSet(){
        User user = new User();
        user.setName("test");
        user.setId("aaaaaa");

        cashService.set("test", user);
    }

    @Test
    @Transactional
    @Rollback
    public void testGet(){
        User user = new User();
        user.setName("test");
        cashService.set("test", user);

        user = (User)cashService.get("test");
        logger.info(user);
    }
}
