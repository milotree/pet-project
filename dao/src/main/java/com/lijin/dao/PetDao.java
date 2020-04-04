package com.lijin.dao;

import com.lijin.entity.Pet;
import com.lijin.entity.PetAndSaler;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 符合Spring Data Jpa的dao层接口规范
 * JpaRepository<操作的实体类类型，实体类中主键属性的类型>
 * *封装了基本的CRUD操作
 * JpaSpecificationExecutor<操作的实体类类型>
 * *封装了复杂查询（分页）
 */
public interface PetDao extends JpaRepository<Pet,Integer>, JpaSpecificationExecutor<Pet> {

    @Query(value = "select new com.lijin.entity.PetAndSaler(b.id,b.ptype,b.page,b.paddress,b.pprice,b.pnum,b.ptime,b.pimg,b.pname,a.sname,a.swechat)"
    +"from Pet b left join Saler a on b.saler.sid = a.sid")
    List<PetAndSaler> getPetsAndSaler(/*@Param("scheduleId") Long scheduleId*/);

    //删除用户
    void deleteById(Integer id);
}
