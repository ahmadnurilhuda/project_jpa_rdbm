package com.greenacademy.productstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.greenacademy.productstore.interceptors.AdminInterceptor;
import com.greenacademy.productstore.interceptors.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/register");

        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**");
        }
    } 
