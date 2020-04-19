package com.lijin.service;

import com.lijin.dao.InformationDao;
import com.lijin.entity.Information;
import com.lijin.entity.Pet;
import com.lijin.entity.PetAndComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class InformationService {
    @Autowired
    private InformationDao informationDao;
    //根据不同宠物查询所有评论
    public List<Information> findAll(String pid){
        List<Information> all = informationDao.findByPid(Integer.valueOf(pid));
        return all;
    }

    /**
     * 保存对用用户的评论
     * @param information
     * @return
     */
    public Information saveComment(Information information){
        Information save = informationDao.save(information);
        return save;
    }
    /**
     * 无条件查询所有评论
     */
    public List<PetAndComment> findAll(){
        List<PetAndComment> petAndComments = informationDao.serachByUnameAndPname();
        return petAndComments;
    }

    /**
     * 通过宠物名字模糊查询所有评论
     * @param pname
     * @return
     */
    public List<PetAndComment> findAllByPname(String pname){
        List<PetAndComment> petAndComments = informationDao.serachByPname(pname);
        return petAndComments;
    }

    /**
     * 通过用户名模糊查询所有评论
     * @param uname
     * @return
     */
    public List<PetAndComment> findAllByUname(String uname){
        List<PetAndComment> petAndComments = informationDao.serachByUname(uname);
        return petAndComments;
    }

    /**
     * 通过用户名和宠物名模糊查询所有评论
     * @param pname
     * @param uname
     * @return
     */
    public List<PetAndComment> findAllByPnameAndUname(String pname,String uname){
        List<PetAndComment> petAndComments = informationDao.serachByUnameAndPname(pname, uname);
        return petAndComments;
    }

    public void delComment(Integer infoid){
        informationDao.deleteById(infoid);
    }

}
