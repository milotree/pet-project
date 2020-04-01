package com.lijin.service;

import com.lijin.dao.ImgDao;
import com.lijin.entity.Img;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.lijin.service")
public class ImgService {
    @Autowired
    private ImgDao imgDao;

    public void setImgDao(ImgDao imgDao) {
        this.imgDao = imgDao;
    }

    public boolean saveImg(String path) {
        Img img = new Img();
        img.setImg(path);
        Img img1 = imgDao.save(img);
        return (img1 == null) ? false : true;
    }

}
