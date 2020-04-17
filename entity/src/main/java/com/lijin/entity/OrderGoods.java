package com.lijin.entity;

import javax.persistence.*;
/**
 * 订单详简略情表
 */
@Entity
@Table(name = "pet_order_goods")
public class OrderGoods {
    @Id
    @Column(name = "ooid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ooid;//编号

    @Column(name = "ooprice")
    private Integer ooprice;//总价格

    @Column(name = "oostatus")
    private Integer oostatus;//订单状态

    @Column(name = "oid")
    private Integer oid;//定点详情表id

    @Column(name = "uname")
    private String uname;//用户名


    public Integer getOoid() {
        return ooid;
    }

    public void setOoid(Integer ooid) {
        this.ooid = ooid;
    }

    public Integer getOoprice() {
        return ooprice;
    }

    public void setOoprice(Integer ooprice) {
        this.ooprice = ooprice;
    }

    public Integer getOostatus() {
        return oostatus;
    }

    public void setOostatus(Integer oostatus) {
        this.oostatus = oostatus;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
