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
@Table
public class Img {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "imgid")
    private Integer imgid;
    @Column(name = "img")
    private String img;

    @Override
    public String toString() {
        return "Img{" +
                "imgid=" + imgid +
                ", img='" + img + '\'' +
                '}';
    }

    public Integer getImgid() {
        return imgid;
    }

    public void setImgid(Integer imgid) {
        this.imgid = imgid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
