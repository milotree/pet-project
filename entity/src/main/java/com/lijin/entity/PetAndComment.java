package com.lijin.entity;

public class PetAndComment {

    private Integer infoid;

    private String infoname;

    private String infocontent;

    private String pname;//宠物名

    public PetAndComment(){}

    public PetAndComment(Integer infoid, String infoname, String infocontent, String pname) {
        this.infoid = infoid;
        this.infoname = infoname;
        this.infocontent = infocontent;
        this.pname = pname;
    }

    @Override
    public String toString() {
        return "PetAndComment{" +
                "infoid=" + infoid +
                ", infoname='" + infoname + '\'' +
                ", infocontent='" + infocontent + '\'' +
                ", pname='" + pname + '\'' +
                '}';
    }

    public Integer getInfoid() {
        return infoid;
    }

    public void setInfoid(Integer infoid) {
        this.infoid = infoid;
    }

    public String getInfoname() {
        return infoname;
    }

    public void setInfoname(String infoname) {
        this.infoname = infoname;
    }

    public String getInfocontent() {
        return infocontent;
    }

    public void setInfocontent(String infocontent) {
        this.infocontent = infocontent;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
