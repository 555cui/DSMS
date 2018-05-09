package com.zeroclub.test;

import com.alibaba.fastjson.JSON;
import com.zeroclub.entity.User;
import com.zeroclub.service.UserService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class UserServiceTest extends BaseTest{
    @Resource
    UserService userService;

    Logger logger = Logger.getLogger("com.zeroclub.service.UserService");

    @Test
    @Transactional
    @Rollback
    public void testGetById(){
        User user = userService.getById("00000000-0000-0000-0000-00000000");
        logger.info(JSON.toJSONString(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetOne(){
        User user = new User();
        user.setName("test");

        user = userService.getOne(user);

        logger.info(JSON.toJSONString(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testSave(){
        User user = new User();
        user.setName("test1");
        user.setPassword("123456");
        user.setEmail("test1@localhost");

        user = userService.save(user);

        user = userService.getById(user.getId());
        logger.info(JSON.toJSONString(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdate(){
        User user = new User();
        user.setId("00000000-0000-0000-0000-00000000");
        user.setPassword("abcdefg");

        user = userService.save(user);

        user = userService.getById(user.getId());
        logger.info(JSON.toJSONString(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete(){
        userService.delete("00000000-0000-0000-0000-00000000");
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteList(){
        String[] ids = new String[1];
        ids[0] = "00000000-0000-0000-0000-00000000";

        userService.deleteList(ids);
    }
}
