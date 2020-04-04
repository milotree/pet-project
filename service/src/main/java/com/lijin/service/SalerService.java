package com.lijin.service;

import com.lijin.dao.SalerDao;
import com.lijin.entity.Saler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SalerService {

    @Autowired
    private SalerDao salerDao;

    public void setSalerDao(SalerDao salerDao) {
        this.salerDao = salerDao;
    }

    /////////////////////////查询所有卖家信息
    public List<Saler> findAll() {
        List<Saler> salers = salerDao.findAll();
        return salers;
    }
    /////////////通过卖家id进行查询
    public Saler findById(Integer sid){
        Saler saler = salerDao.findBySid(sid);
        return saler;
    }
    ////////根据买家姓名查询卖家信息
    public Saler findBySname(String sname){
        Saler saler = salerDao.findBySname(sname);
        return saler;
    }

}
