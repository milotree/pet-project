package com.lijin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "pet_information")
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "infoid")
    private Integer infoid;

    @Column(name = "infoname")
    private String infoname;
    @Column(name = "infocontent")
    private String infocontent;
    @Column(name = "pid")
    private Integer pid;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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
}
