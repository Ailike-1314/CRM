package com.yjxxt.crm.controller;


import com.yjxxt.crm.annotation.RequiredPermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    RoleService roleService;

/*跳转修改或查询页面*/
    @RequestMapping("addOrUpdatePage")
    //@ResponseBody
    public String insertRole(Integer id,Model model){
        System.out.println(id+"------------");
        //判断是否为空
        if(id!=null){
           Role role=roleService.selectByPrimaryKey(id);
           model.addAttribute("role",role);
           System.out.println(role);
        }
        return "role/add_update";
    }
    /*授权页面跳转*/
    @RequestMapping("toRoleGrant")
    public String toRoleGrant(Integer roleId,Model model){
        System.out.println("授权》》》》》》》》》》》》》》》》");
        if(roleId!=null){
            model.addAttribute("roleId",roleId);
        }
        return "role/grant";
    }


    /*查询全部角色信息*/
    @RequestMapping("findRoles")
    @ResponseBody
    public List<Map<String,Object>> findeAll(Integer userId){
        return roleService.selectAll(userId);
    }

    //跳转角色页面
    @RequestMapping("index")
    public String toRole(){
        return "role/role";
    }

    //条件查询
    //@RequiredPermission(code = "60")
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> findRoles(RoleQuery roleQuery){
       return roleService.selectRoles(roleQuery);
    }

    //添加
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo addRole(Role role){
        System.out.println(role);
        ResultInfo resultInfo=new ResultInfo();
        roleService.insertRole(role);
        resultInfo.setCode(200);
        return resultInfo;
    }

    //修改
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo changeRole(Role role){
        ResultInfo resultInfo=new ResultInfo();
        roleService.changeRoles(role);
        resultInfo.setCode(200);
        resultInfo.setMsg("修改成功~");
        return resultInfo;
    }

    /*删除*/
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer roleId,Integer[] mids){
        ResultInfo resultInfo=new ResultInfo();
        roleService.deleteRole(roleId);
        resultInfo.setCode(200);
        resultInfo.setMsg("删除成功~");
        return resultInfo;
    }

    /*添加权限*/
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
        System.out.println(roleId+"---------------------------------"+mids);
        ResultInfo resultInfo=new ResultInfo();
        roleService.addGrant(roleId,mids);
        resultInfo.setCode(200);
        resultInfo.setMsg("添加权限成功~");
        return resultInfo;
    }


}
