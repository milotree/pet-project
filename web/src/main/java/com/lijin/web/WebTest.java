package com.lijin.web;

import com.lijin.service.ServiceTest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class WebTest {
    @RequestMapping("showAll")
    public String showAll(){
        //创建serviceTest的对象
        ServiceTest serviceTest = new ServiceTest();
        return serviceTest.showService()+"这是web";
    }
}
