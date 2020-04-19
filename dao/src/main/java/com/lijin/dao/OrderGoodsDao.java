package com.lijin.dao;


import com.lijin.entity.OrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderGoodsDao extends JpaRepository<OrderGoods, Integer>, JpaSpecificationExecutor<OrderGoods> {
    List<OrderGoods> findAllByOoid(Integer ooid);
    List<OrderGoods> findAllByOoidAndUnameLike(Integer ooid,String uname);
    List<OrderGoods> findAllByUnameLike(String uname);
}
