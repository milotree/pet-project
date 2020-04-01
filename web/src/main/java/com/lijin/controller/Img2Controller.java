package com.lijin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lijin.service.ImgService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
public class Img2Controller {
    @Autowired
    private ImgService imgService;
    public  String realPath;
    public void setImgService(ImgService imgService) {
        this.imgService = imgService;
    }
    @RequestMapping(value = "saveImg",method = RequestMethod.POST)
    @ResponseBody
    public String saveImg(MultipartFile photo, HttpServletRequest request) throws FileUploadException {
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
        String savePath = Thread.currentThread().getContextClassLoader().getResource("")+ "upload/";
       savePath = savePath.substring(6);
        System.out.println(savePath);
        realPath=savePath;
           File savePathFile = new File(savePath);
     if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
         System.out.println("创建了目录");
            savePathFile.mkdir();
        }
        String filename =  UUID.randomUUID().toString().replace("-", "") + "." + suffix;
        imgService.saveImg(savePath+filename);
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
        jo.put("type", "success");
        jo.put("msg", "上传图片成功！");
        jo.put("filepath", savePath);
        jo.put("filename", filename);
        System.out.println(jo.toString());
        return jo.toString();
    }

}
