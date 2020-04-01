package com.lijin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lijin.entity.Img;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
public class ImgController  {
    @Autowired
    private ImgService imgService;

    public void setImgService(ImgService imgService) {
        this.imgService = imgService;
    }




    @RequestMapping(value = "saveImg1",method = RequestMethod.POST)
    public String saveImg(MultipartFile file, HttpServletRequest request) throws FileUploadException {
        JSONObject jo = new JSONObject();
        System.out.println("文件上传。。。");
        //使用fileupload帮助完成文件上传
        //上传的位置
        String path = request.getSession().getServletContext().getRealPath("/uploads/");
        //判断路径是否存在
        File dir = new File(path);
        if (!dir.exists()) {
            //创建该文件夹
            dir.mkdirs();
        }
        //分析request对象获取上传文件项
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        //解析request
        List<FileItem> fileItems = upload.parseRequest((RequestContext) request);
        //遍历list
        for (FileItem item : fileItems) {
            //进行判断。判断当前item对象是否为上传文件项
            if (item.isFormField()) {
                //说明普通表单项

            } else {
                //说明是上传文件项
                //获取上传文件的名称
                String fileName = item.getName();
                String uuid = UUID.randomUUID().toString().replace("-", "");
                fileName = uuid + "_" + fileName;
                //完成文件上传
                System.out.println("正在上传");
                boolean b = imgService.saveImg(path+fileName);
                jo.put ("msg",(b==true)?"yse":"no");
                //删除临时文件
                item.delete();
            }
        }
        return jo.toString();
    }
}
