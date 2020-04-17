package com.lijin.dao;


import com.lijin.entity.OrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderGoodsDao extends JpaRepository<OrderGoods, Integer>, JpaSpecificationExecutor<OrderGoods> {
}
