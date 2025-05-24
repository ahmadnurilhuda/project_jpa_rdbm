package com.greenacademy.productstore.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import com.greenacademy.productstore.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
        

        String requestURI = request.getRequestURI();

        System.out.println("\n\n\nrequestURI: " + requestURI);

        // simpan halaman sebelum login
        request.getSession().setAttribute("prevPage", requestURI);
        // simpan user yang sedang login
        User user = (User) request.getSession().getAttribute("user");

        // check if user is authenticated
        if(user == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
    
}
