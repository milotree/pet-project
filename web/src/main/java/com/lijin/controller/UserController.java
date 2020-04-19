package com.lijin.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lijin.entity.*;
import com.lijin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {
    //自动注入
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private OrderClearService orderClearService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private SalerService salerService;

    public void setSalerService(SalerService salerService) {
        this.salerService = salerService;
    }

    public void setOrderGoodsService(OrderGoodsService orderGoodsService) {
        this.orderGoodsService = orderGoodsService;
    }

    public void setOrderClearService(OrderClearService orderClearService) {
        this.orderClearService = orderClearService;
    }

    public void setInformationService(InformationService informationService) {
        this.informationService = informationService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    //注册方法
    @CrossOrigin
    @RequestMapping(value = "regist", method = {RequestMethod.POST})
    public @ResponseBody
    String userRegister(@RequestBody User user) {
        user.setUtype(0);
        boolean b = userService.saveUser(user);
        JSONObject jo = new JSONObject();
        System.out.println(user.toString());
        System.out.println("注册方法执行，传递的参数为" + JSONObject.toJSONString(user));
        if (b) {
            return JSONObject.toJSONString(user);
        } else {
            jo.put("msg", "no");
            return jo.toString();
        }
    }


    //异步检测账号是否重名
    @CrossOrigin
    @RequestMapping(value = "checkReplace", method = {
            RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String testAjax1(String utel) {
        System.out.println("检验重名方法执行了...");
        System.out.println(utel);
        JSONObject jo = new JSONObject();
        boolean b = userService.checkTel(utel);
        // 做响应，查询数据库
        if (b) {
            jo.put("msg", "yes");
        } else {
            jo.put("msg", "no");
        }
        System.out.println(jo.toString());
        return jo.toString();
    }

    /*
    登录方法
     */
    @RequestMapping(value = "checkLogin", method = {
            RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String checkLogin(String utel, String upass) throws JsonProcessingException {
        System.out.println("登录方法执行了...");
        //通过电话和密码查询用户
        User user = userService.login(utel, upass);
        JSONObject jo = new JSONObject();
        if (user.getUtype()==1){
            System.out.println("此号已被冻结");
            jo.put("msg","此号因为非法交易被冻结");
            return jo.toJSONString();
        }
        /*Cookie cookie = new Cookie("cookieUser",  user.getUname());
        cookie.setMaxAge(0);
        cookie.setMaxAge(7 * 24 * 60 * 60);//设置cookie的最大生命周期为一周
        cookie.setPath("/");    //设置路径为全路径（这样写的好处是同一项目下的页面都可以访问该cookie）
        response.addCookie(cookie);*/

        System.out.println("返回到前端的用户数据为" + JSONObject.toJSONString(user));
        return JSONObject.toJSONString(user);
    }

    /*
    修改密码方法
     */
    @RequestMapping(value = "changePass", method = {
            RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String ChangePass(String utel, String oldpass,String newpass) throws JsonProcessingException {
        System.out.println("修改密码方法执行了...");
        //通过电话和密码查询用户
        User user = userService.login(utel, oldpass);
        user.setUpass(newpass);
        boolean b = userService.saveUser(user);
        JSONObject jo = new JSONObject();
        System.out.println("返回到前端的用户数据为" + JSONObject.toJSONString(user));
        return JSONObject.toJSONString(user);
    }


    /*
    查询所有宠物
     */
    @RequestMapping(value = "showIndex")
    @ResponseBody
    public List<Pet>  findAllPet() {
        System.out.println("前台主页查询列表执行");
        List<Pet> pets = petService.findAllPet();

        for (Pet pet : pets) {
            System.out.println(pet);
        }
        return pets;
    }
    /*
    /////查询指定id的宠物与卖家信息
     */
    @RequestMapping(value = "petDetails")
    public  PetAndSaler findPetAndSalers(String pid){
        System.out.println("查询当前卖家和宠物信息");
        PetAndSaler petAndSaler = petService.searchOneByCondition(Integer.valueOf(pid));
        return petAndSaler;
    }
    /*
        ///////////前台查询所有宠物数据
     */
    @RequestMapping(value = "petPage")
    @ResponseBody
    public List<Pet> petFindAll(){
        System.out.println("前台查询所有宠物信息");
        List<Pet> allPet = petService.findAllPet();
        return allPet;
    }

    /*
    //////分页查询
     */
    @RequestMapping(value = "petPages")
    @ResponseBody
    public String petPages(String currPage){

        System.out.println("前台查询分页");
        List list = petService.petPage(Integer.valueOf(currPage));
        System.out.println(JSONObject.toJSONString(list));
       return JSONObject.toJSONString(list);
    }

    /*
    ////////////////查询的指定类别的宠物信息,并分页
     */
    @RequestMapping(value = "ptype", method = RequestMethod.POST)
    @ResponseBody
    public String petPagesAndPtype(String currPage,String ptype){
        System.out.println("前台查询分页+类别");
        List list = petService.petPtypeAndPage(Integer.valueOf(currPage), ptype);

        return JSONObject.toJSONString(list);
    }
    /*
    //根据宠物名模糊查找并分页
     */
    @RequestMapping(value = "searchName", method = RequestMethod.POST)
    @ResponseBody
    public String searchByName(String currPage,String pname){
        System.out.println("前台查询分页+宠物名模糊查找");
        if (pname.equals("")||null==pname){//如果查询条件为空,则查询全部
            List list = petService.petPage(Integer.valueOf(currPage));
            return JSONObject.toJSONString(list);
        }else {//存在则带名字进行模糊查询
            List<Pet> pets = petService.searchByName(Integer.valueOf(currPage), pname);
            return JSONObject.toJSONString(pets);
        }
    }
    /*
    /////根据宠物显示评论
     */
    @RequestMapping(value = "findcomment", method = RequestMethod.POST)
    @ResponseBody
    public List<Information> findAllComment(String pid){
        System.out.println("查询宠物评论");
        List<Information> all = informationService.findAll(pid);
        return all;
    }
    /*
    保存对应的评论
     */
    @RequestMapping(value = "savecomment", method = RequestMethod.POST)
    @ResponseBody
    public void saveAllComment(HttpServletResponse response, String pid, String infoName, String infoContent) throws IOException {
        System.out.println(pid+infoName+infoContent);
        Information information = new Information();
        information.setInfoname(infoName);
        information.setInfocontent(infoContent);
        information.setPid(Integer.valueOf(pid));
        informationService.saveComment(information);
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
                "    location.href=\"http://localhost:8080/pet-user/product-details.html?pid="+pid+"\";\n" +
                "</script>\n" +
                "</html>");
        out.close();
    }

    /**
     * 后台通过oid查询详情订单信息
     * @param oid
     * @return
     */
    @RequestMapping(value = "findOrderByOid", method = RequestMethod.POST)
    @ResponseBody
    public OrderClear findOrderByOid(String oid){
        OrderClear order = orderClearService.findAllByOrder(oid);
        return order;
    }





}
