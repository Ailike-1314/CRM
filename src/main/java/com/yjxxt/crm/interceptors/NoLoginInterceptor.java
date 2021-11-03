package com.yjxxt.crm.interceptors;

import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //根据cookie中id值登录拦截
        //获取cookie用户id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if(userId==null || userService.selectByPrimaryKey(userId)==null){
            throw new NoLoginException("未登录异常~");
        }
        return true;
    }
}
