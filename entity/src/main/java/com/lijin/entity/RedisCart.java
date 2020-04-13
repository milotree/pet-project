package com.lijin.entity;
//当用户未登录时，存储类型为cookie
public class RedisCart {
    //商品id
    private Integer goodsId;
    //商品名称
    private String goodsName;
    //商品数量
    private Integer goodsNum;
    //商品价格
    private Integer goodsPrice;
    //商品图片
    private String goodsImg;

    @Override
    public String toString() {
        return "CookieCar{" +
                "goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsNum=" + goodsNum +
                ", goodsPrice=" + goodsPrice +
                ", goodsImg='" + goodsImg + '\'' +
                '}';
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Integer goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }
}
