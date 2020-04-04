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

    public List<Pet> findAll() {
        List<Pet> all = petDao.findAll();
        return all;
    }
    public List<PetAndSaler> findPetsAndSalers(){
        List<PetAndSaler> petsAndSaler = petDao.getPetsAndSaler();
        return petsAndSaler;
    }

    public void delPet(Integer id){
        petDao.deleteById(id);
    }
}
