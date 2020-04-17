package com.lijin.service;

import com.lijin.dao.UserDao;
import com.lijin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootApplication
public class UserService {
    @Autowired
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 保存用户
     * @param user 传入的user对象
     * @return 返回是否存入
     */
    public boolean saveUser(User user) {
        User user1 = userDao.save(user);
        if (null != user1) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean checkTel(String tel) {
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
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //1.获取比较的属性
                Path<Object> userTel = root.get("utel");
                //2.构造条查询       ：  select * from pet_user where tel = '***'
                return cb.equal(userTel, tel);//进行精准匹配（比较的属性名，比较的属性取值）
            }
        };
        Optional<User> one = userDao.findOne(spec);

        if (one.isPresent()) {
            return true;   //有同名存在
        } else {
            return false;    //无同名
        }

    }

    public User login(String utel,String upass){

        User user = userDao.findByUtelAndUpass(utel, upass);
        return user;
    }

    public User findUser(String uid){
        User user = userDao.getOne(Integer.valueOf(uid));
        return user;
    }
}
