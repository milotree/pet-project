package com.lijin.service;

import com.lijin.dao.PetDao;
import com.lijin.entity.Pet;
import com.lijin.entity.PetAndSaler;
import com.lijin.entity.Saler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;


import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    ///////////////查询所有宠物和卖家信息
    public List<PetAndSaler> findPetsAndSalers() {
        List<PetAndSaler> petsAndSaler = petDao.getPetsAndSaler();
        return petsAndSaler;
    }

    /////////////////删除宠物信息
    public void delPet(Integer id) {
        petDao.deleteById(id);
    }

    ///////////////带条件查询多个宠物信息（id）
    public PetAndSaler searchOneByCondition(Integer id) {
        PetAndSaler petAndSaler = petDao.getOnePetId(id);
        return petAndSaler;
    }

    ///////////////带条件查询多个宠物信息（id）
    public List<PetAndSaler> searchByCondition(Integer id) {
        List<PetAndSaler> petAndSalers = petDao.getByPetId(id);
        return petAndSalers;
    }

    ///////////////带条件查询多个宠物信息（type）
    public List<PetAndSaler> searchByCondition(String ptype) {
        List<PetAndSaler> petAndSalers = petDao.getByPetId(ptype);
        return petAndSalers;
    }

    ///////////////带条件查询多个宠物信息（id+type）
    public List<PetAndSaler> searchByCondition(Integer id, String ptype) {

        List<PetAndSaler> petAndSalerList = petDao.getByPetId(id, ptype);
        return petAndSalerList;
    }

    /////////查询所有宠物
    public List<Pet> findAllPet() {
        //添加排序
        //创建排序对象，需要调用构造方法，实例化sort对象
        //参数1:排序的顺序（倒叙：Sort.Direction.DESC，正序：Sort.Direction.ASC）
        //参数2：排序的属性名（   ）

        Sort sort =  Sort.by(Sort.Direction.DESC,"ptime");//倒叙
        List<Pet> all = petDao.findAll(sort);
        return all;
    }
    /////分页查询所有宠物
    public List petPage(Integer curr){
        Specification spec = null;
        //PageRquest是Pageable接口的实现类
        /**
         * 创建Pageable的过程中，要传入两个参数
         *      第一个参数：当前查询的页数
         *      第二个参数：每页查询的数量
         */
        Pageable pageable = PageRequest.of(curr,3);
        Page<Pet> pets = petDao.findAll((Specification<Pet>) null, pageable);
        List list = new ArrayList();
        list.add(pets.getContent());//添加得到的分页数据
        list.add(pets.getTotalPages());//添加返回得到的页总数
        return list;//返回两者一起的集合

    }
    ////分页+类别查询所以宠物
    public List petPtypeAndPage(Integer curr,String ptype){
        //PageRquest是Pageable接口的实现类
        /**
         * 创建Pageable的过程中，要传入两个参数
         *      第一个参数：当前查询的页数
         *      第二个参数：每页查询的数量
         */
        Specification<Pet> spec = new Specification<Pet>() {
            @Override
            public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //1.获取比较的属性(数据库字段)
                Path<Object> petid = root.get("ptype");
                //2.构造条查询       ：  select * from pet_pet where ??? = '***'
                return cb.equal(petid, ptype);//进行精准匹配（比较的属性名，比较的属性取值）
            }
        };
        Pageable pageable = PageRequest.of(curr,3);
        Page<Pet> pets = petDao.findAll(spec, pageable);
        List list = new ArrayList();
        list.add(pets.getContent());//添加得到的分页数据
        list.add(pets.getTotalPages());//添加返回得到的页总数
        return list;//返回两者一起的集合
    }
    /**
     * 根据id查询宠物信息
     */
    public Pet findOnePet(String pid){
        /**
         * 匿名内部类
         *
         *  自定义查询条件：
         *          1.实现Specification接口（提供泛型：查询的对象类型）
         *          2.实现toPredicate方法（构造查询条件）
         *          3.需要借助方法参数的两个参数（
         *          root：获取需要查询的对象属性
         *          CriteriaBuilder：构造查询条件，内部封装很多查询条件（模糊匹配，精准匹配）
         *          ）
         *
         *
         */
        Specification<Pet> spec = new Specification<Pet>() {
            @Override
            public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //1.获取比较的属性
                Path<Object> petid = root.get("id");
                //2.构造条查询       ：  select * from pet_pet where tel = '***'
                return cb.equal(petid, Integer.valueOf(pid));//进行精准匹配（比较的属性名，比较的属性取值）
            }
        };
        Optional<Pet> optionalPet = petDao.findOne(spec);
            return optionalPet.get();
    }
    /*
    根据宠物名进行模糊查找
     */
    public List<Pet> searchByName(Integer curr,String pname){
        Specification<Pet> spec = new Specification<Pet>() {
            @Override
            public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //1,查询宠物名
                Path<Object> thisname = root.get("pname");
                //2.模糊匹配

                Predicate like = cb.like(thisname.as(String.class),"%"+pname+"%");
                return like;
            }
        };
        Pageable pageable = PageRequest.of(curr,3);
        Page<Pet> pets = petDao.findAll(spec, pageable);
        List list = new ArrayList();
        list.add(pets.getContent());//添加得到的分页数据
        list.add(pets.getTotalPages());//添加返回得到的页总数
        for (Object o : list) {
            System.out.println(o);
        }
        return list;//返回两者一起的集合
    }

    public Pet findByPname(String pname, Saler saler){
        Pet pet = petDao.findByPnameAndSaler(pname,saler);
        return pet;
    }

    public Pet getOne(Integer pid){
        Pet one = petDao.getOne(pid);
        return one;
    }
}
