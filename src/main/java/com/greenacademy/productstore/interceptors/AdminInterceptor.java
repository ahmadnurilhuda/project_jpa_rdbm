package com.greenacademy.productstore.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import com.greenacademy.productstore.models.Enums.Role;
import com.greenacademy.productstore.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {

        User user = (User) request.getSession().getAttribute("user");
        if(user == null || user.getRole() != Role.ADMIN) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
