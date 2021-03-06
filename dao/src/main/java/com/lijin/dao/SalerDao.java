package com.lijin.dao;

import com.lijin.entity.Saler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 符合Spring Data Jpa的dao层接口规范
 * JpaRepository<操作的实体类类型，实体类中主键属性的类型>
 * *封装了基本的CRUD操作
 * JpaSpecificationExecutor<操作的实体类类型>
 * *封装了复杂查询（分页）
 */
public interface SalerDao extends JpaRepository<Saler,Integer>, JpaSpecificationExecutor<Saler> {
    Saler findBySid(Integer sid);

    Saler findBySname(String sname);

    List<Saler> findAllBySnameLike(String sname);
}
