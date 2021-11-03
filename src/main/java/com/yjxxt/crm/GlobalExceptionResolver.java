package com.yjxxt.crm;

import com.alibaba.fastjson.JSON;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {
      //登录异常处理
        if(ex instanceof NoLoginException){
            // 如果捕获的是未登录异常，则重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        //实例化ModelandView 异常则跳转error
        ModelAndView mv=new ModelAndView("error");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常~");

        if(handler instanceof HandlerMethod){
            //则返回页面
            HandlerMethod hm=(HandlerMethod) handler;
            //通过反射获取注解@responseBody对象
            ResponseBody resquestBody=hm.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断是否被注解
            if(resquestBody==null){
                //返回页面
                if(ex instanceof ParamsException){
                    ParamsException param=(ParamsException) ex;
                    //设置
                    mv.addObject("code",param.getCode());
                    mv.addObject("msg",param.getMsg());
                }

            }else{
                //返回json
                ResultInfo result=new ResultInfo();
                result.setCode(300);
                result.setMsg("系统异常~");
                //异常处理
                if(ex instanceof ParamsException){
                    ParamsException param=(ParamsException) ex;
                    //设置
                    result.setCode(param.getCode());
                    result.setMsg(param.getMsg());
                    // 设置响应类型和编码格式 （响应JSON格式）
                    resp.setContentType("application/json;charset=utf-8");

                    //通过数据流写出
                    PrintWriter out= null;
                    try {
                        out = resp.getWriter();
                        out.write(JSON.toJSONString(result));
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(out!=null){
                            out.close();
                        }
                    }
                    return null;
                }
            }
        }

        return mv;
    }
}
