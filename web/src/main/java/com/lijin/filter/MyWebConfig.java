package com.lijin.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String savePath = Thread.currentThread().getContextClassLoader().getResource("") + "upload/";
        savePath ="file:"+savePath.substring(9,19)+ "upload/";

        registry.addResourceHandler("/petImage/**").addResourceLocations(savePath);
    }
}
