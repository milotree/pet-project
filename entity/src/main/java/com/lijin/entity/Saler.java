package com.lijin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 1.实体类和表的映射关系
 *
 * @Entity
 * @Table 2.类中属性和表中字段的映射关系
 * @Id
 * @GeneratedValue
 * @Column
 */
@Entity
@Table(name = "pet_saler")
public class Saler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sid")
    private Integer sid;
    @Column(name = "sname")
    private String sname;
    @Column(name = "swechat")
    private String swechat;
    /**
     * 放弃外键维护全
     * mappedBy：对方配置关系的属性名称
     */
    @OneToMany(mappedBy = "saler", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Pet> pets = new HashSet<>();

    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public String toString() {
        return "Saler{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                ", swechat='" + swechat + '\'' +
                '}';
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSwechat() {
        return swechat;
    }

    public void setSwechat(String swechat) {
        this.swechat = swechat;
    }
}
