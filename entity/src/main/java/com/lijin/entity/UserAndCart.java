package com.lijin.entity;

import java.util.List;

public class UserAndCart {
    private Integer uid;
    private List<String> redisCart;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public List<String> getRedisCart() {
        return redisCart;
    }

    public void setRedisCart(List<String> redisCart) {
        this.redisCart = redisCart;
    }
}
