package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    //跳转添加或者修改页面
    @RequestMapping("addOrUpdatePage")
    public String toAdd(Integer id,Model model){
        System.out.println("执行力");
        //判断ID
        if(id!=null){
            System.out.println(id+"-----------------");
            //获取当前id对象
           User user= userService.selectByPrimaryKey(id);
           //存入对象
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }

    //用户管理
    @RequestMapping("index")
    public String toUsers(){
        return "user/user";
    }

    //修改密码页面跳转
    @RequestMapping("toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }

    //登录
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(User user){
        ResultInfo result=new ResultInfo();
            //捕获异常
            UserModel userModel=userService.userLogin(user.getUserName(),user.getUserPwd());
            //登录成功
            result.setResult(userModel);
        return result;
    }


    /*修改密码*/
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req,String oldPwd,String newPwd,String againPwd){

        ResultInfo result=new ResultInfo();
        //获取cookie获取用户ID
        int userId=LoginUserUtil.releaseUserIdFromCookie(req);
        //System.out.println(userId+"------------");
        //修改改密码
        //捕获异常
//        try{
            userService.updatePwd(userId,oldPwd,newPwd,againPwd);
//        }catch (ParamsException ex){
//            ex.printStackTrace();
//            result.setCode(ex.getCode());
//            result.setMsg(ex.getMsg());
//        }catch (Exception ex){
//            ex.printStackTrace();
//            result.setCode(500);
//            result.setMsg(ex.getMessage());
//        }
        return result;
    }


    //基本信息页面跳转
    @RequestMapping("toSettingPage")
    public String toSetting(HttpServletRequest req){
        //获取ID
        int userId=LoginUserUtil.releaseUserIdFromCookie(req);
        //获取对象
        User user = userService.selectByPrimaryKey(userId);
        //存储对象
        req.setAttribute("user",user);
        //跳转
        return "user/setting";
    }

    //基本信息修改
    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo updateSetting(User user){
        ResultInfo result=new ResultInfo();
        //基本信息修改
        userService.updateByPrimaryKeySelective(user);
        return result;
    }


    /*查询所有销售人员*/
    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String ,Object>> selectSales(){
        return userService.selectSales();
    }

    /*多条件查询*/
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> manyIfSelect(UserQuery userQuery){
        System.out.println("ddddddddddd");
        System.out.println(userService.manyIfselect(userQuery));
        return userService.manyIfselect(userQuery);
    }

    /*添加用户*/
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo addUser(User user){
        ResultInfo resultInfo=new ResultInfo();
        userService.addUser(user);
        resultInfo.setMsg("添加成功~");
        resultInfo.setCode(200);
        return resultInfo;
    }

    /*修改用户*/
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updataUser(User user){
        ResultInfo resultInfo=new ResultInfo();
        userService.update(user);
        resultInfo.setMsg("修改成功~");
        resultInfo.setCode(200);
        return resultInfo;
    }

    /*删除用户*/
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteIds(Integer[] ids){
        ResultInfo resultInfo=new ResultInfo();
         userService.deletIds(ids);
         resultInfo.setCode(200);
         resultInfo.setMsg("删除成功~~");
         return resultInfo;
    }
}
