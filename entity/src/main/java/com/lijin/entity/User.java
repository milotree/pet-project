package com.lijin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pet_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "uname")
    private String uname;
    @Column(name = "upass")
    private String upass;
    @Column(name = "utel")
    private String utel;
    @Column(name = "uemail")
    private String uemail;
    @Column(name = "upro")
    private String upro;
    @Column(name = "utype")
    private Integer utype;
    /**
     * 放弃外键维护全
     * mappedBy：对方配置关系的属性名称
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    @JSONField(serialize = false)
    private Set<OrderClear> orderClears = new HashSet<>();

    public Set<OrderClear> getOrderClears() {
        return orderClears;
    }

    public void setOrderClears(Set<OrderClear> orderClears) {
        this.orderClears = orderClears;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", upass='" + upass + '\'' +
                ", utel='" + utel + '\'' +
                ", uemail='" + uemail + '\'' +
                ", upro='" + upro + '\'' +
                '}';
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    public String getUtel() {
        return utel;
    }

    public void setUtel(String utel) {
        this.utel = utel;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUpro() {
        return upro;
    }

    public void setUpro(String upro) {
        this.upro = upro;
    }

    public Integer getUtype() {
        return utype;
    }

    public void setUtype(Integer utype) {
        this.utype = utype;
    }
}
