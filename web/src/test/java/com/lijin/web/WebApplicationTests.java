package com.lijin.web;


import com.lijin.dao.ManagerDao;
import com.lijin.entity.Manager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebApplicationTests {

    @Autowired
    private ManagerDao managerDao;

    @Test
    void contextLoads() {
        Manager manager = new Manager();
        manager.setMname("milomilo");
        manager.setMpass("123456");
        managerDao.save(manager);
    }

    /**
     * save方法，（保存或者更新）
     * 如果没有主键(id)就是保存，有主键(id)就是更新
     */

}
