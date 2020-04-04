package com.lijin.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class UserLoginInterceptor implements HandlerInterceptor {
    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler)throws Exception {

        HttpSession session = request.getSession(true);
        Object username=session.getAttribute("mname1");
        if(null!=username) {//已登录
            return true;
        }else {//未登录
            //直接重定向到登录页面
            response.sendRedirect("/pet-manager/login-Manager.html");
            return false;
        }
    }

}
