package com.lijin.dao;


import com.lijin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUproAndUtelAndUemailAndUname(String pro,String tel,String email,String uname);

    User findByUtelAndUpass(String tel,String upass);
}
