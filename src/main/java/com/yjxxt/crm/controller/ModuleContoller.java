package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleContoller extends BaseController {

    @Autowired
    ModuleService moduleService;

//    页面的跳转
    @RequestMapping("index")
    public String toModule(){
        return "module/module";
    }

    /*查询全部信息*/
    @RequestMapping("findModules")
    @ResponseBody
    public List<TreeDto> findeAll(Integer roleId){
        System.out.println(roleId);
        System.out.println("findModules____________");
        return moduleService.findAll(roleId);
    }

    /*查询module资源*/
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> findModule(){
        return moduleService.findModule();
    }

    // 添加资源页视图转发
    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade, Integer parentId, Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("parentId",parentId);
        return "module/add";
    }

    // 更新资源页视图转发
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id,Model model){
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveModule(Module module){
        moduleService.saveModule(module);
        return success("菜单添加成功");
    }


    @RequestMapping("queryAllModulesByGrade")
    @ResponseBody
    public List<Map<String,Object>> queryAllModulesByGrade(Integer grade){
        return moduleService.queryAllModulesByGrade(grade);
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("菜单更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModuleById(id);
        return success("菜单删除成功");
    }

}
