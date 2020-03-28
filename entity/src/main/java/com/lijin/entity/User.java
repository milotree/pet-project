package com.lijin.entity;

import javax.persistence.*;

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
}
