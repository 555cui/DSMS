package com.zeroclub.test;

import com.alibaba.fastjson.JSON;
import com.zeroclub.entity.Group;
import com.zeroclub.entity.User;
import com.zeroclub.service.UserService;
import com.zeroclub.util.ReturnMap;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserServiceTest extends BaseTest{
    private Logger logger = Logger.getLogger("com.zeroclub.test.UserServiceTest");
    @Resource
    private UserService userService;
    @Resource
    private ReturnMap returnMap;

    @Test
    @Transactional
    @Rollback()
    public void testGetOne(){
        User user = new User();
        user.setName("root");
        user = userService.getOne(user);

        logger.info(JSON.toJSONString(user));

        user.setName("cui");
        user = userService.getOne(user);
        if (user == null){
            logger.info("no user");
        }
    }
    @Test
    @Transactional
    @Rollback()
    public void testSave(){
        User user = new User();
        user.setName("cui");
        user.setPassword("cui5501628");
        user.setEmail("cuijingchao0@126.com");
        userService.save(user);
    }

    @Test
    @Transactional
    @Rollback()
    public void testDeleteOne(){
        User user = new User();
        user.setName("cui");
        user = userService.getOne(user);
        userService.deleteOne(user);
    }

    @Test
    @Transactional
    @Rollback()
    public void testDeleteList(){
        User user = new User();
        user.setName("cui");
        List<User> users = userService.filter(returnMap.getSelectParam(user,null, null,
                null, null, null, null));
        logger.info(JSON.toJSONString(users));
        String[] ids = new String[users.size()];
        int i=0;
        for (User item:users){
            ids[i++] = item.getId();
        }
        userService.deleteList(ids);
    }

    @Test
    @Transactional
    @Rollback()
    public void testFilter(){
        Map map = returnMap.getSelectParam(null, 1,10,
                null,new Date(),null,null);
        List<User> users = userService.filter(map);
        logger.info(JSON.toJSONString(users));
    }

    @Test
    @Transactional
    @Rollback()
    public void testCheckPassword(){
        User user = new User();
        user.setName("root");
        user.setPassword("toor");
        if (userService.checkPassword(user)){
            logger.info("right");
            logger.info(JSON.toJSONString(userService.getOne(user)));
        }
        else logger.info("error");
        user.setPassword("wrong password");
        if (!userService.checkPassword(user)){
            logger.info("wrong");
        }
        else logger.info("error");
    }
}
