package com.lijin.dao;


import com.lijin.entity.User;
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
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUproAndUtelAndUemailAndUname(String pro,String tel,String email,String uname);

    User findByUtelAndUpass(String tel, String upass);

    List<User> findByUtel(String tel);

    List<User> findByUtelAndUid(String utel,Integer uid);

    List<User> findByUid(Integer uid);
}
