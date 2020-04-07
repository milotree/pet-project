package com.lijin.service;

import com.lijin.dao.PetDao;
import com.lijin.entity.Pet;
import com.lijin.entity.PetAndSaler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class PetService {

    @Autowired
    private PetDao petDao;


    public void setPetDao(PetDao petDao) {
        this.petDao = petDao;
    }

    /////////////////////上架保存宠物
    public boolean petSave(Pet pet) {
        Pet save = petDao.save(pet);
        return (save == null) ? false : true;
    }

    ///////////////查询所有宠物信息
    public List<PetAndSaler> findPetsAndSalers() {
        List<PetAndSaler> petsAndSaler = petDao.getPetsAndSaler();
        return petsAndSaler;
    }

    /////////////////删除宠物信息
    public void delPet(Integer id) {
        petDao.deleteById(id);
    }

    ///////////////带条件查询多个宠物信息（id）
    public PetAndSaler searchOneByCondition(Integer id){
        PetAndSaler petAndSaler = petDao.getOnePetId(id);
        return petAndSaler;
    }
    ///////////////带条件查询多个宠物信息（id）
    public List<PetAndSaler> searchByCondition(Integer id){
        List<PetAndSaler> petAndSalers = petDao.getByPetId(id);
        return petAndSalers;
    }
    ///////////////带条件查询多个宠物信息（type）
    public List<PetAndSaler> searchByCondition(String ptype){
        List<PetAndSaler> petAndSalers = petDao.getByPetId(ptype);
        return petAndSalers;
    }
    ///////////////带条件查询多个宠物信息（id+type）
    public List<PetAndSaler> searchByCondition(Integer id,String ptype){
        List<PetAndSaler> petAndSalerList = petDao.getByPetId(id, ptype);
        return petAndSalerList;
    }

}
