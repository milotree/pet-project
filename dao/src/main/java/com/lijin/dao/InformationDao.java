package com.lijin.dao;

import com.lijin.entity.Information;
import com.lijin.entity.Pet;
import com.lijin.entity.PetAndComment;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InformationDao extends JpaRepository<Information, Integer>, JpaSpecificationExecutor<Information> {
    List<Information> findByPid(Integer pid);

    //无条件查询评论信息和用户
    @Query(value = "select new com.lijin.entity.PetAndComment(a.infoid,a.infoname,a.infocontent,b.pname)" +
            "from Information a left join Pet b on a.pid = b.id ")
    List<PetAndComment> serachByUnameAndPname();

    //带条件查询评论信息和用户（宠物名）
    @Query(value = "select new com.lijin.entity.PetAndComment(a.infoid,a.infoname,a.infocontent,b.pname)" +
            "from Information a left join Pet b on a.pid = b.id where a.infoname like %?1% ")

    List<PetAndComment> serachByUname(@Param("uname")String uname);
    //带条件查询评论信息和用户（用户名）
    @Query(value = "select new com.lijin.entity.PetAndComment(a.infoid,a.infoname,a.infocontent,b.pname)" +
            "from Information a left join Pet b on a.pid = b.id where b.pname like %?1% ")

    List<PetAndComment> serachByPname(@Param("pname")String pname);
    //带条件查询评论信息和用户（宠物名+用户名）
    @Query(value = "select new com.lijin.entity.PetAndComment(a.infoid,a.infoname,a.infocontent,b.pname)" +
 "from Information a left join Pet b on a.pid = b.id where b.pname like %?1% and a.infoname like %?2%")
    List<PetAndComment> serachByUnameAndPname(@Param("pname")String pname,@Param("uname")String uname);
}
