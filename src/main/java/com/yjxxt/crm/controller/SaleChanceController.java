package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.service.SaleChanceService;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private UserService userService;

    //营销机会页面跳转
    @RequestMapping("index")
    public String toSale(){
        return "/saleChance/sale_chance";
    }


    //添加和修改页面
    @RequestMapping("addOrUpdateDialog")
    public String addorUpdata(Integer id, Model model){
        //如果id有值
        if(id!=null){
            //通过ID获取对象
            SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> manyPage(SaleChanceQuery query){
        return saleChanceService.manyPage(query);
    }

    /*添加商机*/
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(HttpServletRequest req, SaleChance saleChance){
        //获取cookie中的id
       int id= LoginUserUtil.releaseUserIdFromCookie(req);
       //通过id创建者
       String createName=userService.selectByPrimaryKey(id).getTrueName();
       //设置创建者
        saleChance.setCreateMan(createName);
        //添加
        saleChanceService.addSaleChance(saleChance);
        return success("添加成功");
    }

    /*修改营销机会*/
    @RequestMapping("updata")
    @ResponseBody
    public ResultInfo updata(SaleChance saleChance){
        saleChanceService.updataSale(saleChance);
        return success("修改成功");
    }

    /*批量删除*/
    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo delete(Integer[] ids){
        System.out.println(Arrays.toString(ids));
        saleChanceService.deleteall(ids);
        return success("删除成功~");
    }

}
