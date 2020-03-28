package com.lijin.web;

import com.lijin.dao.UserDao;
import com.lijin.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserDao userDao;

    @Test
    void addUser(){
        User user = new User();
        user.setUname("王伟");
        user.setUpass("123456");
        user.setUemail("145025@163.com");
        user.setUtel("13875317696");
        user.setUpro("安化二中");
        userDao.save(user);
    }
    @Test
    void testFindByUproAndUtelAndUemail(){
        User user = userDao.findByUproAndUtelAndUemailAndUname("安化二中", "13875317696", "145025@163.com", "王伟");
        System.out.println(user);
    }
}
