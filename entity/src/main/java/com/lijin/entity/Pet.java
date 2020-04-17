package com.lijin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pet_pet")
//@Data @EqualsAndHashCode @ToString(exclude = "roleList")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private Integer id;//宠物主键

    @Column(name = "ptype")
    private String ptype;//宠物类型

    @Column(name = "page")
    private Integer page;//宠物年龄

    @Column(name = "paddress")
    private String paddress;//宠物地址

    @Column(name = "pprice")
    private Integer pprice;//宠物价格

    @Column(name = "pnum")
    private Integer pnum;//宠物数量

    @Column(name = "ptime")
    private Integer ptime;//发售时间（时间戳）

    @Column(name = "pimg")
    private String pimg;//图片地址

    @Column(name = "pname")
    private String pname;//宠物名

    /**
     * 配置宠物到卖家的多对一关系
     * 使用注解配置多对一关系
     * 1.配置表关系，
     *
     * @ManyToOne：配置表的多对一 fetch = FetchType.LAZY：：懒加载，加载一个实体时，定义懒加载的属性不会马上从数据库中加载。
     * targetEntity：对方的实体类字节码
     * 2.配置外键（中间表）
     * name：外键名称
     * referencedColumnName：外键对应的表的主键名称
     * <p>
     * *配置外键的过程，配置到了多的一方，就会在多的一方维护外键
     */
    @ManyToOne(targetEntity = Saler.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sid", referencedColumnName = "sid")
    @JsonIgnore
    @JSONField(serialize = false)
    private Saler saler;


    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", ptype='" + ptype + '\'' +
                ", page=" + page +
                ", paddress='" + paddress + '\'' +
                ", pprice=" + pprice +
                ", pnum=" + pnum +
                ", ptime=" + ptime +
                ", pimg='" + pimg + '\'' +
                ", pname='" + pname + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getPaddress() {
        return paddress;
    }

    public void setPaddress(String paddress) {
        this.paddress = paddress;
    }

    public Integer getPprice() {
        return pprice;
    }

    public void setPprice(Integer pprice) {
        this.pprice = pprice;
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public Integer getPtime() {
        return ptime;
    }

    public void setPtime(Integer ptime) {
        this.ptime = ptime;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Saler getSaler() {
        return saler;
    }
//    @JsonIgnore
    public void setSaler(Saler saler) {
        this.saler = saler;
    }
}
