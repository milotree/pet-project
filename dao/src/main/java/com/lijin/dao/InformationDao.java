package com.lijin.dao;

import com.lijin.entity.Information;
import com.lijin.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface InformationDao extends JpaRepository<Information, Integer>, JpaSpecificationExecutor<Information> {
    List<Information> findByPid(Integer pid);
}
