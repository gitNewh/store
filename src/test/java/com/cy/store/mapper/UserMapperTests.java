package com.cy.store.mapper;

import com.cy.store.entity.User;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert(){
        User user = new User();
        user.setUsername("user013");
        user.setPassword("123456");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }

    @Test
    public void getUserByUsername(){
        User user = userMapper.getUserByUsername("user01");
        System.out.println(user);
    }

    @Test
    public void updatePasswordByUid(){
        Integer uid = 7;
        String password = "123456";
        String modifiedUser = "超级管理员";
        Date modifiedTime = new Date();
        Integer rows = userMapper.updatePasswordByUid(uid, password, modifiedUser, modifiedTime);
    }

    @Test
    public void findByUid(){
        Integer uid = 7;
        User user = userMapper.findByUid(uid);
        System.out.println(user);

    }



}
