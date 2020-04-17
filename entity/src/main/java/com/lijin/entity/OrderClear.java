package com.lijin.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 订单详情表
 */
@Entity
@Table(name = "pet_order_clear")
public class OrderClear {
    @Id
    @Column(name = "oid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oid;//订单编号

    @Column(name = "oprice")
    private Integer oprice;//订单金额

    @Column(name = "otime")
    private String otime;//下单时间

    @Column(name = "nameandnum")
    private String nameandnum;//宠物详情的json字符串

    @Column(name = "ostatus")
    private Integer ostatus;//订单状态

    /**
     * 配置订单到用户的多对一关系
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
    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    @JsonIgnore
    @JSONField(serialize = false)
    private User user;

   /* @ManyToOne(targetEntity = OrderGoods.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "oid", referencedColumnName = "oid")
    @JsonIgnore
    private Saler saler;
*/

    @Override
    public String toString() {
        return "OrderClear{" +
                "oid=" + oid +
                ", oprice=" + oprice +
                ", otime=" + otime +
                ", nameandnum='" + nameandnum + '\'' +
                '}';
    }

    public Integer getOstatus() {
        return ostatus;
    }

    public void setOstatus(Integer ostatus) {
        this.ostatus = ostatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Integer getOprice() {
        return oprice;
    }

    public void setOprice(Integer oprice) {
        this.oprice = oprice;
    }

    public String getOtime() {
        return otime;
    }

    public void setOtime(String otime) {
        this.otime = otime;
    }

    public String getNameandnum() {
        return nameandnum;
    }

    public void setNameandnum(String nameandnum) {
        this.nameandnum = nameandnum;
    }
}
