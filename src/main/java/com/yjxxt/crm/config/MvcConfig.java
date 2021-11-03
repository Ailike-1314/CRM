package com.yjxxt.crm.config;

import com.yjxxt.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    //创建拦截器对象
    @Bean
    public NoLoginInterceptor getInterceptro(){
        return new NoLoginInterceptor();
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(getInterceptro())
                .addPathPatterns("/**")
        // 用于设置不需要拦截的过滤规则
                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
    }
}
