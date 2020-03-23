package com.lijin.entity;


import javax.persistence.*;

/**
 * 1.实体类和表的映射关系
 *  @Entity
 *  @Table
 * 2.类中属性和表中字段的映射关系
 *  @Id
 *  @GeneratedValue
 *  @Column
 */
@Entity
@Table(name = "pet_manager")
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mid")
    private Integer mid;//管理员主键
    @Column(name = "mname")
    private String mname;
    @Column(name = "mpass")
    private String mpass;



    @Override
    public String toString() {
        return "Manager{" +
                "mid=" + mid +
                ", mname='" + mname + '\'' +
                ", mpass='" + mpass + '\'' +
                '}';
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getMpass() {
        return mpass;
    }

    public void setMpass(String mpass) {
        this.mpass = mpass;
    }
}
