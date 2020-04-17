package com.lijin.service;

import com.lijin.dao.OrderGoodsDao;
import com.lijin.entity.OrderGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class OrderGoodsService {
  @Autowired
  private OrderGoodsDao orderGoodsDao;

    public void setOrderGoodsDao(OrderGoodsDao orderGoodsDao) {
        this.orderGoodsDao = orderGoodsDao;
    }

    public List<OrderGoods> findAll(){
        List<OrderGoods> list = orderGoodsDao.findAll();
        return list;
    }

    public OrderGoods saveOne(OrderGoods orderGoods){
        OrderGoods goods = orderGoodsDao.save(orderGoods);
        return goods;
    }

}
