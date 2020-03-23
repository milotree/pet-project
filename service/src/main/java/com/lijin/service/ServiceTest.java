package com.lijin.service;

import com.lijin.dao.DaoTest;

public class ServiceTest {
    public String showService(){
        //创建DaoTest的对象
        DaoTest daoTest = new DaoTest();
        return daoTest.showDao()+"这是service";
    }
}
