package com.lijin.entity;
/*
用于转码存入数据库中的json字段
（用户id，卖家id，宠物数量，名字）
 */
public class SUPN {
    private String uname;//用户姓名

    private String sname;//卖家姓名

    private Integer pnum;//宠物数量

    private String pname;//宠物名字

    private Integer pprice;//宠物价格

    public Integer getPprice() {
        return pprice;
    }

    public void setPprice(Integer pprice) {
        this.pprice = pprice;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
