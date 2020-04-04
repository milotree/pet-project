package com.lijin.service;

import com.lijin.dao.ManagerDao;
import com.lijin.dao.PetDao;
import com.lijin.dao.SalerDao;
import com.lijin.entity.Manager;
import com.lijin.entity.Pet;
import com.lijin.entity.Saler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
//@ComponentScan("com.lijin.service")
public class ManagerService {
    @Autowired
    private ManagerDao managerDao;


    public void setManagerDao(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    //////////////////////查询管理员登录信息
    public boolean findManager(Manager manager) {
        Manager manager1 = managerDao.findByMnameAndMpass(manager.getMname(), manager.getMpass());
        return (manager1 == null) ? false : true;
    }


}
