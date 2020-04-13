package com.lijin.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lijin.entity.Pet;
import com.lijin.entity.PetAndSaler;
import com.lijin.entity.User;
import com.lijin.service.PetService;
import com.lijin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {
    //自动注入
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;

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
        /*Cookie cookie = new Cookie("cookieUser",  user.getUname());
        cookie.setMaxAge(0);
        cookie.setMaxAge(7 * 24 * 60 * 60);//设置cookie的最大生命周期为一周
        cookie.setPath("/");    //设置路径为全路径（这样写的好处是同一项目下的页面都可以访问该cookie）
        response.addCookie(cookie);*/
        JSONObject jo = new JSONObject();
        System.out.println("返回到前端的用户数据为" + JSONObject.toJSONString(user));
        return JSONObject.toJSONString(user);
    }

    //判断用户是否在线,
    //注解方式获取cookie中对应的key值
    /*@CrossOrigin
    @RequestMapping(value = "findOne", method = {RequestMethod.POST})
    public @ResponseBody
    String finOne(@CookieValue("cookieUser") String cookieUser) throws IOException {
        System.out.println("判断用户是否在线");
        JSONObject jo = new JSONObject();
            jo.put("uname",cookieUser);
        System.out.println("此方法传递到前端的内容是"+jo.toString());
        return  jo.toString();
    }*/

    //退出在线
   /* @RequestMapping(value = "logOut", method = {RequestMethod.POST})
    public String logOut(HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        JSONObject jo = new JSONObject();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookieUser")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");  //路径一定要写上，不然销毁不了
                    response.addCookie(cookie);
                }
            }
        }
        return jo.toString();
    }*/
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
    @ResponseBody
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
}
