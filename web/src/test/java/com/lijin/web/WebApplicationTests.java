package com.lijin.web;


import com.lijin.dao.ManagerDao;
import com.lijin.dao.UserDao;
import com.lijin.entity.Manager;
import com.lijin.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
class WebApplicationTests {

    @Autowired
    private ManagerDao managerDao;

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    void contextLoads() {


        /**
         * 查询是否有相同的电话号码
         */
        Specification<User> spec = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    //1.获取比较的属性
                    Path<Object> userTel = root.get("utel");
                    //2.构造条查询       ：  select * from pet_user where tel = '***'
                    return cb.equal(userTel, "13875");//进行精准匹配（比较的属性名，比较的属性取值）
                }
            };
//           Optional<User> one = userDao.findOne(spec);
        Optional<User> one = userDao.findOne(spec);
        System.out.println(one);
            if (one.isPresent()) {
                System.out.println("有相同的");
            } else {
                System.out.println("没有相同的");
        }
    }

    /**
     * save方法，（保存或者更新）
     * 如果没有主键(id)就是保存，有主键(id)就是更新
     */

}
