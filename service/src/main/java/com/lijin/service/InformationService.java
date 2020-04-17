package com.lijin.service;

import com.lijin.dao.InformationDao;
import com.lijin.entity.Information;
import com.lijin.entity.Pet;
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

    public Information saveComment(Information information){
        Information save = informationDao.save(information);
        return save;
    }
}
