package com.lijin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lijin.entity.Pet;
import com.lijin.entity.RedisCart;
import com.lijin.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PetService petService;

    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    /*
      一次一次添加购物车商品到redis缓存中，并返回json数据到前端展示
     */
    @RequestMapping(value = "addCart", method = RequestMethod.POST)
    public String redisAdd(String pid, String pname, String pprice, String pnum, String pimg) {
        System.out.println(pid+"--"+pname+"--"+pprice+"--"+pnum+"--"+pimg);
        //1.从redis中获得值，数据的形式为json字符串
        List<String> list = new ArrayList<>();
        String userListJson = redisTemplate.boundValueOps(pid).get();//获取键对应的值
        String json;
        System.out.println("购物车添加方法进行中");
        JSONObject jo = new JSONObject();
        //2.判断redis中是否存在数据
        if (null==userListJson){
            //不存在，将前端获取过来的商品信息填入对象,存入redis中
            RedisCart redisCart = new RedisCart();
            redisCart.setGoodsId(Integer.valueOf(pid));
            redisCart.setGoodsImg(pimg);
            redisCart.setGoodsName(pname);
            redisCart.setGoodsNum(Integer.valueOf(pnum));
            redisCart.setGoodsPrice(Integer.valueOf(pprice));
            json = JSONObject.toJSONString(redisCart);
            redisTemplate.boundValueOps(pid).set(json);
            System.out.println("购物车不存在此商品,添加新商品");
        }else{
            //存在，将此json字符串转为购物车对象,修改信息，在存入redis中
            System.out.println("购物车存在此商品，修改信息");
            RedisCart cart = JSONObject.parseObject(userListJson, RedisCart.class);
            Pet onePet = petService.findOnePet(pid);
            if (onePet.getPnum()<=cart.getGoodsNum()){
                //获取所有的key，然后得到所有的值
                System.out.println("此商品不能再多了");
            }else {
                cart.setGoodsNum(cart.getGoodsNum()+Integer.valueOf(pnum));
                cart.setGoodsPrice(cart.getGoodsPrice()+Integer.valueOf(pprice));
                json = JSONObject.toJSONString(cart);
                redisTemplate.boundValueOps(pid).set(json);
            }
        }
        //获取所有的key，然后得到所有的值
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(redisTemplate.opsForValue().get(key));
            list.add(redisTemplate.opsForValue().get(key));
        }
        System.out.println(list.toString());
        return list.toString();
    }

    /**
     * 添加多次商品
     * @param pid
     * @param pname
     * @param pprice
     * @param pnum
     * @param pimg
     * @return
     */
    @RequestMapping(value = "addCartMany", method = RequestMethod.POST)
    public String redisAddMany(String pid, String pname, String pprice, String pnum, String pimg) {
        System.out.println(pid+"--"+pname+"--"+pprice+"--"+pnum+"--"+pimg);
        //1.从redis中获得值，数据的形式为json字符串
        List<String> list = new ArrayList<>();
        String userListJson = redisTemplate.boundValueOps(pid).get();//获取键对应的值
        String json;
        System.out.println("购物车添加方法进行中");
        JSONObject jo = new JSONObject();
        //2.判断redis中是否存在数据
        if (null==userListJson){
            //不存在，将前端获取过来的商品信息填入对象,存入redis中
            RedisCart redisCart = new RedisCart();
            redisCart.setGoodsId(Integer.valueOf(pid));
            redisCart.setGoodsImg(pimg);
            redisCart.setGoodsName(pname);
            redisCart.setGoodsNum(Integer.valueOf(pnum));
            redisCart.setGoodsPrice(Integer.valueOf(pprice)*Integer.valueOf(pnum));
            json = JSONObject.toJSONString(redisCart);
            redisTemplate.boundValueOps(pid).set(json);
            System.out.println("购物车不存在此商品,添加新商品");
        }else{
            //存在，将此json字符串转为购物车对象,修改信息，在存入redis中
            System.out.println("购物车存在此商品，修改信息");
            RedisCart cart = JSONObject.parseObject(userListJson, RedisCart.class);
            Pet onePet = petService.findOnePet(pid);
            if (Integer.valueOf(pnum)>cart.getGoodsNum()){
                //获取所有的key，然后得到所有的值
                System.out.println("此商品不能再多了");
            }else{
                cart.setGoodsNum(Integer.valueOf(pnum));
                cart.setGoodsPrice(Integer.valueOf(pprice)*Integer.valueOf(pnum));
                json = JSONObject.toJSONString(cart);
                redisTemplate.boundValueOps(pid).set(json);
            }
        }
        //获取所有的key，然后得到所有的值
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(redisTemplate.opsForValue().get(key));
            list.add(redisTemplate.opsForValue().get(key));
        }
        System.out.println(list.toString());
        return list.toString();
    }


    /*
    删除购物车商品
     */
    @RequestMapping(value = "delCart", method = RequestMethod.POST)
    public String redisDel(String pid){
        System.out.println("删除指定购物车商品");
        //1.判断是否存在存在商品
        String userListJson = redisTemplate.boundValueOps(pid).get();
        JSONObject jo = new JSONObject();
        List<String> list = new ArrayList<>();
        if (null==userListJson){
            jo.put("message","不存在此商品");
            return jo.toString();
        }else {
            redisTemplate.delete(pid);
        }
        Set<String> keys = redisTemplate.keys("*");
        for (String key : keys) {
            System.out.println(redisTemplate.opsForValue().get(key));
            list.add(redisTemplate.opsForValue().get(key));
        }
        System.out.println(list.toString());
        return list.toString();
    }
    /*
    登录显示所有的购物车信息
     */
    @RequestMapping(value = "showCart", method = RequestMethod.POST)
    public String redisShow(){
        System.out.println("显示所有购物车商品，方法进行");
        List<String> list = new ArrayList<>();
        Set<String> keys = redisTemplate.keys("*");
        if (null!=keys){
            for (String key : keys) {
                System.out.println(redisTemplate.opsForValue().get(key));
                list.add(redisTemplate.opsForValue().get(key));
            }
            System.out.println(list.toString());
            return list.toString();
        }else {
            return null;
        }
    }

    @RequestMapping(value = "DelAllCart", method = RequestMethod.POST)
    public void redisDelAll(){
        System.out.println("删除所有购物车商品，方法进行");
        Set<String> keys = redisTemplate.keys("*");
        if (keys!=null){
            redisTemplate.delete(keys);
        }
    }
}
