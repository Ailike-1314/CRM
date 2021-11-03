package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.service.PermissionService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import com.yjxxt.crm.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    PermissionService permissionService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest req){
        //获取cookie的id
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(req);
        //获取用户对象
        User user=userService.selectByPrimaryKey(userId);
        //存储实现记住登录
        req.setAttribute("user",user);
        //获取用户的权限资源
        List<String> list=permissionService.selectByUserIdGetModelId(userId);
        for (String id: list) {
            System.out.println(id+"权限码~");
        }
        //存储到session
        req.getSession().setAttribute("permissions",list);
        return "main";
    }

    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

}
