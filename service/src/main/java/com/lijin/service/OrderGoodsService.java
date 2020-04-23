package com.lijin.service;

import com.lijin.dao.OrderGoodsDao;
import com.lijin.entity.OrderGoods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootApplication
public class OrderGoodsService {
  @Autowired
  private OrderGoodsDao orderGoodsDao;

    public void setOrderGoodsDao(OrderGoodsDao orderGoodsDao) {
        this.orderGoodsDao = orderGoodsDao;
    }

    public List<OrderGoods> findAll(){
        //添加排序
        //创建排序对象，需要调用构造方法，实例化sort对象
        //参数1:排序的顺序（倒叙：Sort.Direction.DESC，正序：Sort.Direction.ASC）
        //参数2：排序的属性名（   ）
        Sort sort =Sort.by(Sort.Direction.ASC,"oostatus");
        List<OrderGoods> list = orderGoodsDao.findAll(sort);
//        list.stream().forEach((goods)-> System.out.println(goods.toString()));
        return list;
    }

    /**
     * 保存一个brief 订单
     * @param orderGoods
     * @return
     */
    public OrderGoods saveOne(OrderGoods orderGoods){
        OrderGoods goods = orderGoodsDao.save(orderGoods);
        return goods;
    }
    /**
     * 查询一个brief订单
     */
    public OrderGoods findOne(String ooid){
        OrderGoods goods = orderGoodsDao.getOne(Integer.valueOf(ooid));
        return goods;
    }

    /**
     * 根据条件搜索订单(方法重载1)
     */
    public List<OrderGoods> findGoodsBy(String uname){

        List<OrderGoods> allByUname = orderGoodsDao.findAllByUnameLike("%"+uname+"%");
        return allByUname;
    }
    /**
     * 根据条件搜索订单(方法重载2)
     */
    public List<OrderGoods> findGoodsBy(Integer ooid){
        List<OrderGoods> allByOoid = orderGoodsDao.findAllByOoid(ooid);
        return allByOoid;
    }
    /**
     * 根据条件搜索订单(方法重载3)
     */
    public List<OrderGoods> findGoodsBy(Integer ooid,String uname){
        List<OrderGoods> allByOoidAndUname = orderGoodsDao.findAllByOoidAndUnameLike(ooid, "%"+uname+"%");
        return allByOoidAndUname;
    }


}
