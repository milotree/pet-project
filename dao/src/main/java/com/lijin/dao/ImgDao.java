package com.lijin.dao;

import com.lijin.entity.Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImgDao extends JpaRepository<Img, Integer>, JpaSpecificationExecutor<Img> {

}
