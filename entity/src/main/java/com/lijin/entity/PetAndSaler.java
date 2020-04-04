package com.lijin.entity;

public class PetAndSaler {
    private Integer id;
    private String ptype;
    private Integer page;
    private String paddress;
    private Integer pprice;
    private Integer pnum;
    private Integer ptime;
    private String pimg;
    private String pname;
    private String sname;
    private String swechat;

    public PetAndSaler(){}

    public PetAndSaler(Integer id, String ptype, Integer page, String paddress, Integer pprice, Integer pnum, Integer ptime, String pimg, String pname, String sname, String swechat) {
        this.id = id;
        this.ptype = ptype;
        this.page = page;
        this.paddress = paddress;
        this.pprice = pprice;
        this.pnum = pnum;
        this.ptime = ptime;
        this.pimg = pimg;
        this.pname = pname;
        this.sname = sname;
        this.swechat = swechat;
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

    @Override
    public String toString() {
        return "PetAndSaler{" +
                "id=" + id +
                ", ptype='" + ptype + '\'' +
                ", page=" + page +
                ", paddress='" + paddress + '\'' +
                ", pprice=" + pprice +
                ", pnum=" + pnum +
                ", ptime=" + ptime +
                ", pimg='" + pimg + '\'' +
                ", pname='" + pname + '\'' +
                ", sname='" + sname + '\'' +
                ", swechat='" + swechat + '\'' +
                '}';
    }
}
