package com.lijin.service;

import com.lijin.dao.OderClearDao;
import com.lijin.entity.OrderClear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class OrderClearService {
    @Autowired
    private OderClearDao oderClearDao;

    public List<OrderClear> findAll(){
        List<OrderClear> all = oderClearDao.findAll();
        return all;
    }
}
