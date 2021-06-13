package com.pan.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new LoginFilter()).addPathPatterns("/admin/**")
                    .excludePathPatterns("/admin", "/css/**", "/images/**", "/js/**", "/lib/**", "/admin/login", "/admin/logout");
        }
    }
