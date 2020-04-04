package com.lijin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lijin.entity.Manager;
import com.lijin.entity.Pet;
import com.lijin.entity.PetAndSaler;
import com.lijin.entity.Saler;
import com.lijin.service.ImgService;
import com.lijin.service.ManagerService;
import com.lijin.service.PetService;
import com.lijin.service.SalerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @RestController是@ResponseBody和@Controller的组合注解
 * @Controller是用来响应页面的，如果是string类型的方法，则springmvc会跳转到相应的页面（视图）
 * @ResponseBody是用来响应数据的，如果是对象类型的方法，则springmvc会将结果对象转成json格式输出给前端 本例中我使用的是@RestController注解，所以springmvc会将返回的对象Result自动转json返回给前端（底层默认是使用jsckson来实现数据格式转换的）
 */
@RestController
@RequestMapping("manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @Autowired
    private ImgService imgService;

    @Autowired
    private SalerService salerService;

    @Autowired
    private PetService petService;

    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    public void setSalerService(SalerService salerService) {
        this.salerService = salerService;
    }

    public void setManagerService(ManagerService managerService) {
        this.managerService = managerService;
    }

    public void setImgService(ImgService imgService) {
        this.imgService = imgService;
    }

    /*
    /////////////////////执行管理员登录方法
     */
    @RequestMapping(value = "login", method = {RequestMethod.POST}
            , produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String Managerlogin(@RequestBody Manager manager1, HttpServletRequest request) {
        managerService.findManager(manager1);
        request.getSession().setAttribute("mname1", "true");
        System.out.println("管理员登录方法执行，传递的参数为" + JSONObject.toJSONString(manager1));
        return JSONObject.toJSONString(manager1);
    }

    //退出并清除cookie
    @RequestMapping("logout")
    public String logOut(HttpServletRequest request) {
        System.out.println("管理员账号退出方法执行了...");
        JSONObject jo = new JSONObject();
        //判断seesion是否存在，不存在则退出
        if (request.getSession().getAttribute("mname1") != null) {
            System.out.println(request.getSession().getAttribute("mname1"));
            request.getSession().removeAttribute("mname1");
            jo.put("msg", "yes");
            return jo.toString();
        } else {
            jo.put("msg", "yes");
            return jo.toString();
        }
    }

    /*
    ///////////////////异步上传宠物图片
     */
    @RequestMapping(value = "imgUpload", method = RequestMethod.POST)
    public String imgUpload(@RequestParam(value = "pimg", required = true) MultipartFile photo, HttpServletRequest request) {
        JSONObject jo = new JSONObject();
        if (photo == null) {
            jo.put("type", "error");
            jo.put("msg", "选择要上传的文件！");
            return jo.toString();
        }
        if (photo.getSize() > 1024 * 1024 * 10) {
            jo.put("type", "error");
            jo.put("msg", "文件大小不能超过10M！");
            return jo.toString();
        }
        //获取文件后缀
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1, photo.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            jo.put("type", "error");
            jo.put("msg", "请选择jpg,jpeg,gif,png格式的图片！");
            return jo.toString();
        }
        //获取项目根目录加上图片目录 static/imgages/upload/
        String savePath = Thread.currentThread().getContextClassLoader().getResource("") + "upload/";
        savePath = savePath.substring(6);
        System.out.println(savePath);
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            System.out.println("创建了目录");
            savePathFile.mkdir();
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
        imgService.saveImg(savePath + filename);
        System.out.println("文件已保存");
        try {
            //将文件保存指定目录
            photo.transferTo(new File(savePath + filename));
        } catch (Exception e) {
            jo.put("type", "error");
            jo.put("msg", "保存文件异常！");
            e.printStackTrace();
            return jo.toString();
        }
        //删除上次传输的图片（发现上次的图片不对是，重新上传执行的逻辑）
        if (request.getSession().getAttribute("photoPosition") != null) {
            java.io.File myDelFile = new java.io.File(request.getSession().getAttribute("photoPosition").toString());
            myDelFile.delete();
            request.getSession().removeAttribute("photoPosition");
            System.out.println("成功删除上张图片");
        }
        request.getSession().setAttribute("photoPosition", savePath + filename);
        System.out.println("图片地址保存中" + request.getSession().getAttribute("photoPosition"));
        jo.put("type", "success");
        jo.put("msg", "上传图片成功！");
        jo.put("filepath", savePath);
        jo.put("filename", filename);
        System.out.println(jo.toString());
        return jo.toString();
    }

    /*//////////////////////////
    刷新或者退出时，删除异步上传存在的图片
     */
    @RequestMapping(value = "imgDel", method = RequestMethod.POST)
    public String imgDel(HttpServletRequest request) {
        System.out.println("删除异步上传存在的图片，代码块执行中======");
        JSONObject jo = new JSONObject();
        Object photoPosition = request.getSession().getAttribute("photoPosition");
        if (photoPosition != null) {
            java.io.File myDelFile = new java.io.File(photoPosition.toString());
            myDelFile.delete();
            request.getSession().removeAttribute("photoPosition");
            System.out.println("成功删除图片");
            jo.put("msg", "删除不需要的图片成功！");
        } else {
            jo.put("msg", "删除图片失败或不存在图片");
            System.out.println("删除图片失败，图片不存在或需要保存");
        }
        return jo.toString();
    }

    @RequestMapping(value = "imgDell", method = RequestMethod.POST)
    public void imgdelll() {
        System.out.println("啥也没干");
    }

    /*
    ///////////////添加宠物信息
   */
    @RequestMapping(value = "petAdd", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void petAdd(String pname,String ptype, String page, String paddress,String pprice, String pnum, String ptime,  String salers, HttpServletRequest request) {
        Pet pet = new Pet();
        Saler saler = salerService.findBySname(salers);//根据传递的卖家姓名，查找卖家信息
        pet.setPtype(ptype);//添加类型
        pet.setPage(Integer.valueOf(page));//添加年龄
        pet.setPaddress(paddress);//添加地址
        pet.setPprice(Integer.valueOf(pprice));//添加价格
        pet.setPnum(Integer.valueOf(pnum));//添加数量
        pet.setSaler(saler);//添加卖家
        pet.setPname(pname);//添加宠物名
        pet.setPtime(Integer.valueOf(ptime.substring(0, 10)));//添加上架商品时间
        pet.setPimg((String) request.getSession().getAttribute("photoPosition"));//添加图片位置
        request.getSession().removeAttribute("photoPosition");//删除图片地址储存的session信息，告诉删除图片的函数图片是所需求的
       petService.petSave(pet);
       System.out.println("收集到用户信息:" + pet.toString());
    }

    /*
    ////////////////查询卖家信息
     */
    @RequestMapping(value = "/salers")
    @ResponseBody
    public List<Saler> findAllSalers() {
        System.out.println("执行查询所有卖家");
        List<Saler> salers = salerService.findAll();
        return salers;
    }

    @RequestMapping(value = "findAllPet")
    @ResponseBody
    public List<PetAndSaler> findAllPet(){
        System.out.println("查询所有宠物信息");
        List<PetAndSaler> petsAndSalers = petService.findPetsAndSalers();
        for (PetAndSaler petsAndSaler : petsAndSalers) {
            System.out.println(petsAndSaler);
        }
        return petsAndSalers;
    }

    @RequestMapping(value = "petDel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void delPet(String pid){
        petService.delPet(Integer.valueOf(pid));
        System.out.println("删除数据代码块执行中==================");
    }

}
