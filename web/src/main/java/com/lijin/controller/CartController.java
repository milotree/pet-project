package com.lijin.controller;

import com.alibaba.fastjson.JSONObject;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.lijin.entity.*;
import com.lijin.filter.AlipayConfig;
import com.lijin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PetService petService;
    @Autowired
    private SalerService salerService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderClearService orderClearService;
    @Autowired
    private OrderGoodsService orderGoodsService;

    private static Map<String,String> map = new HashMap<>();

    public void setOrderGoodsService(OrderGoodsService orderGoodsService) {
        this.orderGoodsService = orderGoodsService;
    }

    public void setOrderClearService(OrderClearService orderClearService) {
        this.orderClearService = orderClearService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSalerService(SalerService salerService) {
        this.salerService = salerService;
    }

    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 一次一次添加购物车商品到redis缓存中，并返回json数据到前端展示
     *
     * @param pid
     * @param pname
     * @param pprice
     * @param pnum
     * @param pimg
     * @return
     */
    @RequestMapping(value = "addCart", method = RequestMethod.POST)
    @ResponseBody
    public String redisAdd(String uid,String pid, String pname, String pprice, String pnum, String pimg) {

        System.out.println(pid + "--" + pname + "--" + pprice + "--" + pnum + "--" + pimg);
        //1.从redis中获得值，数据的形式为json字符串
        List<String> list2 = new ArrayList<>();
        List list3 = new ArrayList();//用于存储新的购物车详情信息
        Pet onePet1 = petService.findOnePet(pid);
        if(onePet1.getPnum()<=0){
            return null;
        }
//        String petListJson = redisTemplate.boundValueOps(pid).get();//获取键对应的值
        String userListJson = redisTemplate.boundValueOps(uid).get();//获取键对应的值
        String json;
        String bigjson;

        System.out.println("购物车添加方法进行中");
        //2.判断对应用户的redis中是否存在数据
        if (userListJson==null){
            System.out.println("用户对应购物车不存在(第一次购买)");
            UserAndCart userAndCart = new UserAndCart();//创建购物车的总表id
            userAndCart.setUid(Integer.valueOf(uid));//设置购物车总表的id
            RedisCart cart = new RedisCart();       //创建一个新的购物车详情表对象
            cart.setGoodsId(Integer.valueOf(pid));//设置购物车详情表的id值
            cart.setGoodsImg(pimg);                //设置购物车详情表的图片
            cart.setGoodsName(pname);              //设置购物车详情表的宠物名
            cart.setGoodsNum(Integer.valueOf(pnum));//设置购物车详情表的宠物数量
            cart.setGoodsPrice(Integer.valueOf(pprice));//设置购物车详情表的宠物价格
            json = JSONObject.toJSONString(cart);      //将购物车详情表的对象转化为json字符串
            map.put(pid+onePet1.getPname()+uid,json);
            System.out.println("map信息存入1");
            list2.add(map.get(pid+onePet1.getPname()+uid));
            userAndCart.setRedisCart(list2);//将redis的详情表的json数组存入总表中
            bigjson = JSONObject.toJSONString(userAndCart);//将总表数据转化为json
            System.out.println("转化为list添加到redis中");
            redisTemplate.boundValueOps(uid).set(bigjson);//转化为json字符串，在添加进redis中
        }else {
            System.out.println("购物车对应的对象存在，其map为+"+map.get(pid+onePet1.getPname()+uid));
            UserAndCart userAndCart = JSONObject.parseObject(userListJson, UserAndCart.class);//反序列化为购物车总表对象
            List<String> redisCart = userAndCart.getRedisCart();//得到购物车详情json数组
            if (map.get(pid+onePet1.getPname()+uid)!=null){//如果map中的数据不为空，则说明存在过此商品，宠物已存入json对象字符串中
                System.out.println("购物车的map存在");
                String newjson = "";
                for (String s : redisCart) {    //遍历每个购物车json字符串数组
                    RedisCart cart = JSONObject.parseObject(s, RedisCart.class);//反序列化为购物车详情表对象
                    Pet onePet = petService.findOnePet(pid);//查询后台数据库对应的宠物信息
                    if (cart.getGoodsId()==Integer.valueOf(pid)) {//查找是否为所需商品
                        //获取所有的key，然后得到所有的值
                        cart.setGoodsNum(cart.getGoodsNum() + Integer.valueOf(pnum));//修改其商品数量
                        cart.setGoodsPrice(cart.getGoodsPrice() + Integer.valueOf(pprice));//修改其价格
                        newjson = JSONObject.toJSONString(cart);       //将购物车对象再次转换为json数组
                        if (Integer.valueOf(pnum) + cart.getGoodsNum() > onePet.getPnum()){//如果后台宠物数量不足
                            newjson=map.get(pid+onePet1.getPname()+uid);//将没有修改的json重新写入
                            System.out.println("此商品不能再多了");
                        }
                    }else {
                        cart.setGoodsNum(cart.getGoodsNum());//修改其商品数量
                        cart.setGoodsPrice(cart.getGoodsPrice());//修改其价格
                        newjson = JSONObject.toJSONString(cart);       //将购物车对象再次转换为json数组
                    }
                    map.put(pid+onePet1.getPname()+uid,newjson);//无论是否修改，都把json
                    System.out.println("新的map信息存入");
                    list3.add(map.get(pid+onePet1.getPname()+uid));
                }
            } else {//map中不存在此数据，将商品信息添加至map和redis中，
                System.out.println("不存在此map");
                RedisCart cart = new RedisCart();
                cart.setGoodsId(Integer.valueOf(pid));//设置购物车详情表的id值
                cart.setGoodsImg(pimg);                //设置购物车详情表的图片
                cart.setGoodsName(pname);              //设置购物车详情表的宠物名
                cart.setGoodsPrice(Integer.valueOf(pprice));//设置购物车详情表的宠物价格
                cart.setGoodsNum(Integer.valueOf(pnum));//设置购物车详情表的宠物数量
                String toJSONString = JSONObject.toJSONString(cart);//将新的购物车信息转化为json
                map.put(pid+onePet1.getPname()+uid,toJSONString);//将其信息添加入新的json
                System.out.println("新的map存入，值为+"+map.get(pid+onePet1.getPname()+uid));
                for (String s : redisCart){
                    list3.add(s);
                }
                list3.add(map.get(pid+onePet1.getPname()+uid));//在将其加入新的购物车详情表中
                System.out.println("新的list3内容是"+list3.toString());
            }
            userAndCart.setRedisCart(list3);
            bigjson = JSONObject.toJSONString(userAndCart);//将总表数据转化为json
            System.out.println("转化新的list3添加到redis中"+bigjson);
            redisTemplate.boundValueOps(uid).set(bigjson);//转化为json字符串，在添加进redis中
        }
        String newcart =  redisTemplate.boundValueOps(uid).get();
        UserAndCart userAndCart = JSONObject.parseObject(newcart, UserAndCart.class);//反序列化为购物车总表对象
        System.out.println(userAndCart.getRedisCart().toString()+"这是新的购物车的信息");
        return userAndCart.getRedisCart().toString();
/*         Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        System.out.println("全部删除");
        map.clear();
        return null;*/
    }


    /**
     * 添加多次同一商品
     *
     * @param pid
     * @param pname
     * @param pprice
     * @param pnum
     * @param pimg
     * @return
     */
    @RequestMapping(value = "addCartMany", method = RequestMethod.POST)
    @ResponseBody
    public String redisAddMany(String uid,String pid, String pname, String pprice, String pnum, String pimg) {
          System.out.println(pid + "--" + pname + "--" + pprice + "--" + pnum + "--" + pimg);
        //1.从redis中获得值，数据的形式为json字符串
        List<String> list2 = new ArrayList<>();
        List list3 = new ArrayList();//用于存储新的购物车详情信息
        Pet onePet1 = petService.findOnePet(pid);
        if(onePet1.getPnum()<=0){
            return null;
        }
//        String petListJson = redisTemplate.boundValueOps(pid).get();//获取键对应的值
        String userListJson = redisTemplate.boundValueOps(uid).get();//获取键对应的值
        String json;
        String bigjson;

        System.out.println("购物车添加方法进行中");
        //2.判断对应用户的redis中是否存在数据
        if (userListJson==null){
            System.out.println("用户对应购物车不存在(第一次购买)");
            UserAndCart userAndCart = new UserAndCart();//创建购物车的总表id
            userAndCart.setUid(Integer.valueOf(uid));//设置购物车总表的id
            RedisCart cart = new RedisCart();       //创建一个新的购物车详情表对象
            cart.setGoodsId(Integer.valueOf(pid));//设置购物车详情表的id值
            cart.setGoodsImg(pimg);                //设置购物车详情表的图片
            cart.setGoodsName(pname);              //设置购物车详情表的宠物名
            cart.setGoodsNum(Integer.valueOf(pnum));//设置购物车详情表的宠物数量
            cart.setGoodsPrice(Integer.valueOf(pprice));//设置购物车详情表的宠物价格
            json = JSONObject.toJSONString(cart);      //将购物车详情表的对象转化为json字符串
            map.put(pid+onePet1.getPname()+uid,json);
            System.out.println("map信息存入1");
            list2.add(map.get(pid+onePet1.getPname()+uid));
            userAndCart.setRedisCart(list2);//将redis的详情表的json数组存入总表中
            bigjson = JSONObject.toJSONString(userAndCart);//将总表数据转化为json
            System.out.println("转化为list添加到redis中");
            redisTemplate.boundValueOps(uid).set(bigjson);//转化为json字符串，在添加进redis中
        }else {
            System.out.println("购物车对应的对象存在，其map为+"+map.get(pid+onePet1.getPname()+uid));
            UserAndCart userAndCart = JSONObject.parseObject(userListJson, UserAndCart.class);//反序列化为购物车总表对象
            List<String> redisCart = userAndCart.getRedisCart();//得到购物车详情json数组
            if (map.get(pid+onePet1.getPname()+uid)!=null){//如果map中的数据不为空，则说明存在过此商品，宠物已存入json对象字符串中
                System.out.println("购物车的map存在");
                String newjson = "";
                for (String s : redisCart) {    //遍历每个购物车json字符串数组
                    RedisCart cart = JSONObject.parseObject(s, RedisCart.class);//反序列化为购物车详情表对象
                    Pet onePet = petService.findOnePet(pid);//查询后台数据库对应的宠物信息
                    if (cart.getGoodsId()==Integer.valueOf(pid)) {//查找是否为所需商品
                        //获取所有的key，然后得到所有的值
                        cart.setGoodsNum(cart.getGoodsNum() + Integer.valueOf(pnum));//修改其商品数量
                        cart.setGoodsPrice(cart.getGoodsPrice() + Integer.valueOf(pprice));//修改其价格
                        newjson = JSONObject.toJSONString(cart);       //将购物车对象再次转换为json数组
                        if (Integer.valueOf(pnum) + cart.getGoodsNum() > onePet.getPnum()){//如果后台宠物数量不足
                            newjson=map.get(pid+onePet1.getPname()+uid);//将没有修改的json重新写入
                            System.out.println("此商品不能再多了");
                        }
                    }else {
                        cart.setGoodsNum(cart.getGoodsNum());//修改其商品数量
                        cart.setGoodsPrice(cart.getGoodsPrice());//修改其价格
                        newjson = JSONObject.toJSONString(cart);       //将购物车对象再次转换为json数组
                    }
                    map.put(pid+onePet1.getPname()+uid,newjson);//无论是否修改，都把json
                    System.out.println("新的map信息存入");
                    list3.add(map.get(pid+onePet1.getPname()+uid));
                }
            } else {//map中不存在此数据，将商品信息添加至map和redis中，
                System.out.println("不存在此map");
                RedisCart cart = new RedisCart();
                cart.setGoodsId(Integer.valueOf(pid));//设置购物车详情表的id值
                cart.setGoodsImg(pimg);                //设置购物车详情表的图片
                cart.setGoodsName(pname);              //设置购物车详情表的宠物名
                cart.setGoodsPrice(Integer.valueOf(pprice));//设置购物车详情表的宠物价格
                cart.setGoodsNum(Integer.valueOf(pnum));//设置购物车详情表的宠物数量
                String toJSONString = JSONObject.toJSONString(cart);//将新的购物车信息转化为json
                map.put(pid+onePet1.getPname()+uid,toJSONString);//将其信息添加入新的json
                System.out.println("新的map存入，值为+"+map.get(pid+onePet1.getPname()+uid));
                for (String s : redisCart){
                    list3.add(s);
                }
                list3.add(map.get(pid+onePet1.getPname()+uid));//在将其加入新的购物车详情表中
                System.out.println("新的list3内容是"+list3.toString());
            }
            userAndCart.setRedisCart(list3);
            bigjson = JSONObject.toJSONString(userAndCart);//将总表数据转化为json
            System.out.println("转化新的list3添加到redis中"+bigjson);
            redisTemplate.boundValueOps(uid).set(bigjson);//转化为json字符串，在添加进redis中
        }
        String newcart =  redisTemplate.boundValueOps(uid).get();
        UserAndCart userAndCart = JSONObject.parseObject(newcart, UserAndCart.class);//反序列化为购物车总表对象
        System.out.println(userAndCart.getRedisCart().toString()+"这是新的购物车的信息");
        return userAndCart.getRedisCart().toString();













        //1.从redis中获得值，数据的形式为json字符串
       /* List<String> list = new ArrayList<>();
        String userListJson = redisTemplate.boundValueOps(uid).get();//获取键对应的值
        String json;
        System.out.println("购物车添加方法进行中");
        //2.判断redis中是否存在数据
        if (null == userListJson) {
            //不存在，将前端获取过来的商品信息填入对象,存入redis中
            System.out.println("存在此用户的购物车,添加新商品");
            RedisCart redisCart = new RedisCart();
            redisCart.setGoodsId(Integer.valueOf(pid));
            redisCart.setGoodsImg(pimg);
            redisCart.setGoodsName(pname);
            redisCart.setGoodsNum(Integer.valueOf(pnum));
            redisCart.setGoodsPrice(Integer.valueOf(pprice) * Integer.valueOf(pnum));
            json = JSONObject.toJSONString(redisCart);
            redisTemplate.boundValueOps(pid).set(json);

        } else {
            //存在，将此json字符串转为购物车对象,修改信息，在存入redis中
            System.out.println("购物车存在此商品，修改信息");
            RedisCart cart = JSONObject.parseObject(userListJson, RedisCart.class);
            Pet onePet = petService.findOnePet(pid);
            if ((Integer.valueOf(pnum) + cart.getGoodsNum()) > onePet.getPnum()) {
                //获取所有的key，然后得到所有的值
                System.out.println("此商品不能再多了");
            } else {
                cart.setGoodsNum(Integer.valueOf(pnum) + cart.getGoodsNum());
                cart.setGoodsPrice(Integer.valueOf(pprice) * Integer.valueOf(pnum) + cart.getGoodsPrice());
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
        return list.toString();*/
    }


    /*
    删除购物车商品
     */
    @RequestMapping(value = "delCart", method = RequestMethod.POST)
    @ResponseBody
    public String redisDel(String pid,String uid) {
        System.out.println("删除指定购物车商品"+pid+";;;;"+uid);
        //1.判断是否存在存在商品
        Pet onePet1 = petService.findOnePet(pid);
        String userListJson = redisTemplate.boundValueOps(uid).get();
        System.out.println(userListJson+"存在uid对应的数据");
        UserAndCart userAndCart = JSONObject.parseObject(userListJson, UserAndCart.class);//反序列化为购物车总表对象
        List<String> redisCart = userAndCart.getRedisCart();
        List list2 = new ArrayList();
        for (String s : redisCart) {
            RedisCart cart = JSONObject.parseObject(s, RedisCart.class);//反序列化为购物车详情表对象
            if (Integer.valueOf(pid)==cart.getGoodsId()){
                System.out.println("找到指定的购物车数据并删除成功");

            }else {
                System.out.println(cart.getGoodsId());
                list2.add(JSONObject.toJSONString(cart));
                System.out.println(list2);
            }
        }
        for (Map.Entry<String, String> item : map.entrySet()){
            System.out.println("这是循环遍历的map+"+item);
            System.out.println("这是要对比的地方"+item.getKey()+":"+(pid+onePet1.getPname()+uid));
            if (item.getKey().equals((pid+onePet1.getPname()+uid))){
                System.out.println("找打指定的map删除");
                map.remove(item.getKey());
            }

        }
        /*for (String s1 : map.keySet()) {
            if (s1.equals()){
                System.out.println("删除指定的map的数据");
                map.remove(s1);
            }
        }*/
        System.out.println(list2.toString()+"ddhgdhrdhd");
        userAndCart.setRedisCart(list2);
        System.out.println(list2.toString());
        String bigjson = JSONObject.toJSONString(userAndCart);//将总表数据转化为json
        System.out.println("转化新的list3添加到redis中"+bigjson);
        redisTemplate.boundValueOps(uid).set(bigjson);//转化为json字符串，在添加进redis中
        String newcart =  redisTemplate.boundValueOps(uid).get();
        UserAndCart newuserAndCart = JSONObject.parseObject(newcart, UserAndCart.class);//反序列化为购物车总表对象
        System.out.println(newuserAndCart.getRedisCart().toString()+"这是新的购物车的信息");
        return newuserAndCart.getRedisCart().toString();
    }

    /*
    登录显示所有的购物车信息
     */
    @RequestMapping(value = "showCart", method = RequestMethod.POST)
    @ResponseBody
    public String redisShow(String uid) {
        System.out.println("显示所有购物车商品，方法进行+"+uid);
        if (uid!=null){
            String newcart =  redisTemplate.boundValueOps(uid).get();
            if (newcart!=null){
                UserAndCart userAndCart = JSONObject.parseObject(newcart, UserAndCart.class);//反序列化为购物车总表对象
                System.out.println(userAndCart.getRedisCart().toString()+"这是新的购物车的信息");
                List<String> redisCart = userAndCart.getRedisCart();
                for (String s : redisCart) {
                    RedisCart cart = JSONObject.parseObject(s, RedisCart.class);//反序列化为购物车详情表对象
                    map.put(cart.getGoodsId()+cart.getGoodsName()+uid,cart.toString());
                }
                return userAndCart.getRedisCart().toString();
            }else {
                return null;
            }}
        return null;

    }

    /**
     * 指定用户清理删除所有购物车商品
     */
    @RequestMapping(value = "DelAllCart", method = RequestMethod.POST)
    @ResponseBody
    public void redisDelAll(String uid) {
        System.out.println("进入删除部分代码块+"+uid);
        String newcart =  redisTemplate.boundValueOps(uid).get();
        if (newcart!=null){
            UserAndCart userAndCart = JSONObject.parseObject(newcart, UserAndCart.class);//反序列化为购物车总表对象
            List<String> redisCart = userAndCart.getRedisCart();
            List list2 = new ArrayList();
            for (String s : redisCart) {
                RedisCart cart = JSONObject.parseObject(s, RedisCart.class);//反序列化为购物车详情表对象
                for (String s1 : map.keySet()) {
                    System.out.println(s1);
                    if (s1.equals(cart.getGoodsImg()+cart.getGoodsName()+uid)){
                        System.out.println("删除指定的map的数据");
                        map.remove(s1);
                    }
                }
            }
            System.out.println("删除map成功");
            List list = new ArrayList();
            userAndCart.setRedisCart(list);
            String bigjson = JSONObject.toJSONString(userAndCart);//将总表数据转化为json
            System.out.println("转化新的list3添加到redis中"+bigjson);
            redisTemplate.boundValueOps(uid).set(bigjson);//转化为json字符串，在添加进redis中
            System.out.println("删除所有购物车商品，方法进行");
        }else {
            System.out.println("为空");
        }
       /* for (String s1 : map.keySet()) {
            if (s1.equals(pid+uid)){
                System.out.println("删除指定的map的数据");
                map.remove(s1);
            }
        }*/
    }

    /**
     * 清理删除所有购物车商品
     */
  /*  @RequestMapping(value = "DelAllCartAll", method = RequestMethod.POST)
    @ResponseBody
    public void redisDelAll() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }*/

    /**
     * 发起商品购买请求(添加至用户订单)
     */
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    @ResponseBody
    public String emptyCart(String uid, String time) {
        System.out.println("开始将购物车添加至订单");
        String newcart =  redisTemplate.boundValueOps(uid).get();
        UserAndCart userAndCart = JSONObject.parseObject(newcart, UserAndCart.class);//反序列化为购物车总表对象
        List<String> redisCart = userAndCart.getRedisCart();//得到购物车详情json数组

        JSONObject jo = new JSONObject();
        Integer sumPrice = 0;//用来获取总价格
        List<SUPN> list = new ArrayList<>();//存储宠物数据
//        Set<String> keys = redisTemplate.keys("*");//获取redis中所有的数据
        for (String s : redisCart) {
            RedisCart cart = JSONObject.parseObject(s, RedisCart.class);//反序列化为购物车详情表对象
            SUPN supn = new SUPN();
          /*  String thisPet = redisTemplate.opsForValue().get(key);//得到redis中键对应的json数组
            RedisCart cart = JSONObject.parseObject(thisPet, RedisCart.class);//转化为java对象*/
            Pet onePet = petService.findOnePet(String.valueOf(cart.getGoodsId()));//根据id查询对应的动物
            supn.setSname(onePet.getSaler().getSname());//设置卖家姓名
            supn.setUname(userService.findUser(uid).getUname());//设置用户姓名
            supn.setPname(cart.getGoodsName());//设置宠物姓名
            supn.setPnum(cart.getGoodsNum());//设置宠物数量
            supn.setPprice(onePet.getPprice());//设置宠物价格
            sumPrice += cart.getGoodsPrice();
            onePet.setPnum(onePet.getPnum() - cart.getGoodsNum());
            petService.petSave(onePet);//修改存在的宠物信息
            list.add(supn);
        }
        /*for (String key : keys) {
        }*/
        OrderClear orderClear = new OrderClear();
        orderClear.setOprice(sumPrice);//设置总价
        orderClear.setOtime(time);//设置下单时间
        orderClear.setOstatus(0);//表示未付款 0 为未付款 1为付款 -1为收货
        orderClear.setUser(userService.findUser(uid));//设置用户
        orderClear.setNameandnum(JSONObject.toJSONString(list));//设置宠物数据的json数组
        OrderClear orderClear1 = orderClearService.saveOrderClear(orderClear);
        if (orderClear1 != null) {
            jo.put("msg", "提交订单成功");
//            redisTemplate.delete(keys);
            if (newcart!=null){
                List list2 = new ArrayList();
                for (String s : redisCart) {
                    RedisCart cart = JSONObject.parseObject(s, RedisCart.class);//反序列化为购物车详情表对象
                    for (Map.Entry<String, String> item : map.entrySet()){
                        System.out.println("这是循环遍历的map+"+item);
                        System.out.println("加入订单模块这是要对比的地方"+item.getKey()+":"+(cart.getGoodsId()+cart.getGoodsName()+uid ));
                        if (item.getKey().equals(cart.getGoodsId()+cart.getGoodsName()+uid)){
                            System.out.println("找打指定的map删除");
                            String remove = map.remove(item.getKey());
                            System.out.println(remove+"1111111111");
                        }
                    }

                }
                System.out.println("删除map成功");
                List list3 = new ArrayList();
                userAndCart.setRedisCart(list3);
                String bigjson = JSONObject.toJSONString(userAndCart);//将总表数据转化为json
                System.out.println("转化新的list3添加到redis中"+bigjson);
                redisTemplate.boundValueOps(uid).set(bigjson);//转化为json字符串，在添加进redis中
                System.out.println("删除所有购物车商品，方法进行");
            }
        } else {
            jo.put("msg", "提交订单失败");
        }
        return jo.toJSONString();
    }

    //查询自身订单
    @RequestMapping(value = "findSelfOrder", method = RequestMethod.POST)
    @ResponseBody
    public List<OrderClear> findAllSelfOrder(String uid) {
        User user = userService.findUser(uid);
        List<OrderClear> all = orderClearService.findAll(user);
        return all;
    }

    /**
     * 阿里支付代码块
     * @param oid   订单号
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "aliplay", method = RequestMethod.POST)
    public void payOrder(String oid, HttpServletResponse response, HttpServletRequest request) throws Exception {
        OrderClear one = orderClearService.findOne(Integer.valueOf(oid));
        System.out.println("支付方法执行");
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = String.valueOf(one.getOid());
        //付款金额，必填
        String total_amount = String.valueOf(one.getOprice());
        //订单名称，必填
//        List<SUPN> list = JSONObject.parseArray(one.getNameandnum(), SUPN.class);//将存入的json字符串转为对象数组
        String subject = "千宠网宠物购买";
        //商品描述，可空
        String body = "";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String head = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'></head><body>";
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        String buttom = "</body></html>";
        String content = head + result + buttom;
        response.setHeader("content-type", "text/html;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(content.getBytes("UTF-8"));
        out.close();
//        response.getWriter().println(head + result + buttom);
    }

    /**
     * 阿里支付的return_url跳转方法
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/payloading", method = RequestMethod.GET)
    public void payLoading(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("支付成功, 进入异步通知接口...");
        response.setContentType("text/html; charset=utf-8");//千万不要忘了设编码,否则密钥报错!!!!!!
        PrintWriter out = response.getWriter();
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String str : requestParams.keySet()) {
            String name = str;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        if (!signVerified) {
            System.out.println("验签失败");
            out.print("fail");
            return;
        }
        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

        //交易状态
        System.out.println("交易成功");
        OrderClear one = orderClearService.findOne(Integer.valueOf(out_trade_no));
        one.setOstatus(1);//设置为已付款状态
        orderClearService.saveOrderClear(one);
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setOid(one.getOid());//设置商品表oid
        orderGoods.setOoprice(one.getOprice());//设置价格
        orderGoods.setUname(one.getUser().getUname());//设置买家名
        orderGoods.setOostatus(0);//0为后台未审核通过，1为审核通过
        orderGoodsService.saveOne(orderGoods);
        out.print("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "</body>\n" +
                "<script>\n" +
                "    location.href=\"http://localhost:8080/pet-user/checkout.html\";\n" +
                "</script>\n" +
                "</html>");
    }

    /**
     * 改变订单的审核状态
     * @param oid
     * @return
     */
    @RequestMapping(value = "ChangeOrder", method = RequestMethod.POST)
    @ResponseBody
    public String ChangeOrder(String oid) {
        System.out.println("修改订单为已签收状态");
        OrderClear one = orderClearService.findOne(Integer.valueOf(oid));
        one.setOstatus(-1);
        orderClearService.saveOrderClear(one);
        JSONObject jo = new JSONObject();
        jo.put("msg", "success");
        return jo.toJSONString();
    }

    /**
     * 前台导航栏查找指定宠物信息
     * @param request
     * @param response
     * @param pname
     * @throws IOException
     */
    @RequestMapping(value = "findPet", method = RequestMethod.GET)
    public void findPet(HttpServletRequest request, HttpServletResponse response, String pname) throws IOException {
        System.out.println("查找指定宠物" + pname);
        response.setContentType("text/html; charset=utf-8");//千万不要忘了设编码,否则密钥报错!!!!!!
        PrintWriter out = response.getWriter();
        out.print("<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "</body>\n" +
                "<script>\n" +
                "    location.href=\"http://localhost:8080/pet-user/shop-page.html?msg=" + pname + "\";\n" +
                "</script>\n" +
                "</html>");
        out.close();
    }

    /**
     * 前端删除指定未支付的订单
     * @param oid
     * @return
     */
    @RequestMapping(value = "cancelOrder", method = RequestMethod.POST)
    @ResponseBody
    public String cancelOrder(String oid) {
        System.out.println("删除所有未付款的订单");
        OrderClear one = orderClearService.findOne(Integer.valueOf(oid));

        if (one.getOstatus() == 0) {
            List<SUPN> list = JSONObject.parseArray(one.getNameandnum(), SUPN.class);//将存入的json字符串转为对象数组
            for (SUPN supn : list) {
                String pname = supn.getPname();//得到名字
                String sname = supn.getSname();//得到卖家姓名
                Saler saler = salerService.findBySname(sname);//得到卖家姓名
                Pet pet = petService.findByPname(pname, saler);//查询出宠物信息
                pet.setPnum(pet.getPnum() + supn.getPnum());
                petService.petSave(pet);
            }
            orderClearService.DelAll(one.getOid());
        }
        JSONObject jo = new JSONObject();
        jo.put("msg", "success");
        return jo.toJSONString();
    }
}
