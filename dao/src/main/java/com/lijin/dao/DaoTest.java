package com.lijin.dao;

import com.lijin.entity.EntityTest;

public class DaoTest {
    public String showDao(){
        //创建EntityTest对象
        EntityTest entityTest = new EntityTest();
        return  entityTest.showEntity()+"这是dao";
    }
}
