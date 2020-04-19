package com.lijin.service;

import com.lijin.dao.OderClearDao;
import com.lijin.entity.OrderClear;
import com.lijin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class OrderClearService {
    @Autowired
    private OderClearDao oderClearDao;
    //带条件查询所有
    public List<OrderClear> findAll(User user) {
        List<OrderClear> all = oderClearDao.findByUser(user);
        return all;
    }
    //保存并返回保存对象
    public OrderClear saveOrderClear(OrderClear orderClear) {
        OrderClear clear = oderClearDao.save(orderClear);
        return clear;
    }
    //无条件查询所有
    public List<OrderClear> CheckAll(){
        List<OrderClear> all = oderClearDao.findAll();
        return all;
    }

    public void DelAll(Integer id){
    oderClearDao.deleteById(id);
    }

    public OrderClear findOne(Integer oid){
        OrderClear one = oderClearDao.getOne(oid);
        return one;
    }

    /**
     * 通过订单号查询详情信息
     * @param oid
     * @return
     */
    public OrderClear findAllByOrder(String oid){
        Optional<OrderClear> byId = oderClearDao.findById(Integer.valueOf(oid));
        OrderClear orderClear = byId.get();
        return orderClear;
    }




}
