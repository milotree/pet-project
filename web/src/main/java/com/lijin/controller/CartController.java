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
    public String redisAdd(String pid, String pname, String pprice, String pnum, String pimg) {
        System.out.println(pid + "--" + pname + "--" + pprice + "--" + pnum + "--" + pimg);
        //1.从redis中获得值，数据的形式为json字符串
        List<String> list = new ArrayList<>();
        String userListJson = redisTemplate.boundValueOps(pid).get();//获取键对应的值
        String json;
        System.out.println("购物车添加方法进行中");
        JSONObject jo = new JSONObject();
        //2.判断redis中是否存在数据
        if (null == userListJson) {
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
        } else {
            //存在，将此json字符串转为购物车对象,修改信息，在存入redis中
            System.out.println("购物车存在此商品，修改信息");
            RedisCart cart = JSONObject.parseObject(userListJson, RedisCart.class);
            Pet onePet = petService.findOnePet(pid);
            if (onePet.getPnum() <= cart.getGoodsNum()) {
                //获取所有的key，然后得到所有的值
                System.out.println("此商品不能再多了");
            } else {
                cart.setGoodsNum(cart.getGoodsNum() + Integer.valueOf(pnum));
                cart.setGoodsPrice(cart.getGoodsPrice() + Integer.valueOf(pprice));
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
    public String redisAddMany(String pid, String pname, String pprice, String pnum, String pimg) {
        System.out.println(pid + "--" + pname + "--" + pprice + "--" + pnum + "--" + pimg);
        //1.从redis中获得值，数据的形式为json字符串
        List<String> list = new ArrayList<>();
        String userListJson = redisTemplate.boundValueOps(pid).get();//获取键对应的值
        String json;
        System.out.println("购物车添加方法进行中");
        //2.判断redis中是否存在数据
        if (null == userListJson) {
            //不存在，将前端获取过来的商品信息填入对象,存入redis中
            RedisCart redisCart = new RedisCart();
            redisCart.setGoodsId(Integer.valueOf(pid));
            redisCart.setGoodsImg(pimg);
            redisCart.setGoodsName(pname);
            redisCart.setGoodsNum(Integer.valueOf(pnum));
            redisCart.setGoodsPrice(Integer.valueOf(pprice) * Integer.valueOf(pnum));
            json = JSONObject.toJSONString(redisCart);
            redisTemplate.boundValueOps(pid).set(json);
            System.out.println("购物车不存在此商品,添加新商品");
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
        return list.toString();
    }


    /*
    删除购物车商品
     */
    @RequestMapping(value = "delCart", method = RequestMethod.POST)
    @ResponseBody
    public String redisDel(String pid) {
        System.out.println("删除指定购物车商品");
        //1.判断是否存在存在商品
        String userListJson = redisTemplate.boundValueOps(pid).get();
        JSONObject jo = new JSONObject();
        List<String> list = new ArrayList<>();
        if (null == userListJson) {
            jo.put("message", "不存在此商品");
            return jo.toString();
        } else {
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
    @ResponseBody
    public String redisShow() {
        System.out.println("显示所有购物车商品，方法进行");
        List<String> list = new ArrayList<>();
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);
        if (null != keys&&!keys.equals("[]")) {
            System.out.println("显示购物车商品");
            for (String key : keys) {
                System.out.println(redisTemplate.opsForValue().get(key));
                list.add(redisTemplate.opsForValue().get(key));
            }
            System.out.println(list.toString());
            return list.toString();
        } else {
            System.out.println("没有商品");
            return list.toString();
        }
    }

    /**
     * 清理删除所有购物车商品
     */
    @RequestMapping(value = "DelAllCart", method = RequestMethod.POST)
    @ResponseBody
    public void redisDelAll() {
        System.out.println("删除所有购物车商品，方法进行");
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 发起商品购买请求(添加至用户订单)
     */
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    @ResponseBody
    public String emptyCart(String uid, String time) {
        System.out.println("开始将购物车添加至订单");
        JSONObject jo = new JSONObject();
        Integer sumPrice = 0;//用来获取总价格
        List<SUPN> list = new ArrayList<>();//存储宠物数据
        Set<String> keys = redisTemplate.keys("*");//获取redis中所有的数据
        for (String key : keys) {
            SUPN supn = new SUPN();

            String thisPet = redisTemplate.opsForValue().get(key);//得到redis中键对应的json数组
            RedisCart cart = JSONObject.parseObject(thisPet, RedisCart.class);//转化为java对象
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
        OrderClear orderClear = new OrderClear();
        orderClear.setOprice(sumPrice);//设置总价
        orderClear.setOtime(time);//设置下单时间
        orderClear.setOstatus(0);//表示未付款 0 为未付款 1为付款 -1为收货
        orderClear.setUser(userService.findUser(uid));//设置用户
        orderClear.setNameandnum(JSONObject.toJSONString(list));//设置宠物数据的json数组
        OrderClear orderClear1 = orderClearService.saveOrderClear(orderClear);
        if (orderClear1 != null) {
            jo.put("msg", "提交订单成功");
            redisTemplate.delete(keys);
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

    @RequestMapping(value = "/payloading", method=RequestMethod.GET)
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

        if(!signVerified) {
            System.out.println("验签失败");
            out.print("fail");
            return ;
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

    @RequestMapping(value = "ChangeOrder", method = RequestMethod.POST)
    public String ChangeOrder(String oid) {
        System.out.println("修改订单为已签收状态");
        OrderClear one = orderClearService.findOne(Integer.valueOf(oid));
        one.setOstatus(-1);
        orderClearService.saveOrderClear(one);
        JSONObject jo = new JSONObject();
        jo.put("msg","success");
        return jo.toJSONString();
    }

    @RequestMapping(value = "findPet", method = RequestMethod.GET)
    public void findPet(HttpServletRequest request, HttpServletResponse response,String pname) throws IOException {
        System.out.println("查找指定宠物"+pname);
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
                "    location.href=\"http://localhost:8080/pet-user/shop-page.html?msg="+pname+"\";\n" +
                "</script>\n" +
                "</html>");
        out.close();
    }

}
