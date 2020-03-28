package com.lijin.controller;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijin.entity.User;
import com.lijin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {

    //创建一个单例模式
    @Autowired
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();






    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    //注册方法
    @CrossOrigin
    @RequestMapping(value = "regist", method = {RequestMethod.POST})
    public @ResponseBody
    String userRegister(@RequestBody User user) {
        boolean b = userService.saveUser(user);
        JSONObject jo = new JSONObject();
        System.out.println(user.toString());
        if (b) {
            jo.put("msg", "yes");
        } else {
            jo.put("msg", "no");
        }
        System.out.println(jo.toString());
        return jo.toString();
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

    //登录方法
    @RequestMapping(value = "checkLogin", method = {
            RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String checkLogin(String utel, String upass, HttpServletResponse response) throws JsonProcessingException {
        System.out.println("登录方法执行了...");


        //通过电话和密码查询用户
        User user = userService.login(utel, upass);
        Cookie cookie = new Cookie("cookieUser",  user.getUname());
        System.out.println("cookie已写入");
        cookie.setMaxAge(0);
        cookie.setMaxAge(7 * 24 * 60 * 60);//设置cookie的最大生命周期为一周
        cookie.setPath("/");    //设置路径为全路径（这样写的好处是同一项目下的页面都可以访问该cookie）
        response.addCookie(cookie);
        JSONObject jo = new JSONObject();
        boolean b;
        if (user != null) {
            b = true;
        } else {
            b = false;
        }
        // 做响应，查询数据库
        if (b) {
            jo.put("msg", "yes");
        } else {
            jo.put("msg", "no");
        }
        System.out.println(jo.toString());
        return jo.toString();
    }

    //判断用户是否在线,
    //注解方式获取cookie中对应的key值
    @CrossOrigin
    @RequestMapping(value = "findOne", method = {RequestMethod.POST})
    public @ResponseBody
    String finOne(@CookieValue("cookieUser") String cookieUser) throws IOException {
        System.out.println("判断用户是否在线");
        JSONObject jo = new JSONObject();
            jo.put("uname",cookieUser);
        System.out.println("此方法传递到前端的内容是"+jo.toString());
        return  jo.toString();
    }

    //退出在线
    @RequestMapping(value = "logOut", method = {RequestMethod.POST})
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
    }

}
