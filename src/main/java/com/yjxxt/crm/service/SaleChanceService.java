package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.SaleChance;
import com.yjxxt.crm.mapper.SaleChanceMapper;
import com.yjxxt.crm.query.SaleChanceQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /*//分页显示*/
    public Map<String,Object> manyPage(SaleChanceQuery query){
        Map<String, Object> map = new HashMap<>();
        //设置分页
        PageHelper.startPage(query.getPage(), query.getLimit());
        //对数据进行分页
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }
   /* //添加商机*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance){
        //判断客户名称，机会来源，联系人，联系电话，
        checkInfo(saleChance.getCustomerName(),saleChance.getChanceSource(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //state状态是否分配 0 未分配 1已经分配
        if(StringUtils.isBlank(saleChance.getAssignMan())){
            saleChance.setDevResult(0);
            saleChance.setState(0);
        }
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setDevResult(1);
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
        }

        //设置默认值
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        saleChance.setIsValid(1);
        //判断插入是否成功
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败！");

    }

   /* //修改商机*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updataSale(SaleChance saleChance){
        //通过id判断是否存在
        SaleChance sc=selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(sc==null,"更新商机不存在！");
        //基础判断
        checkInfo(saleChance.getCustomerName(),saleChance.getChanceSource(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //是否分配]
        //原营销机会未分配，改为已经分配
        if(StringUtils.isBlank(sc.getAssignMan()) && saleChance.getAssignMan()!=null){
            sc.setState(1);
            sc.setDevResult(1);
            sc.setAssignTime(new Date());
        }
        //原营销机会已经分配，改为未分配
        if(StringUtils.isNotBlank(sc.getAssignMan()) && StringUtils.isBlank(sc.getAssignMan())){
            sc.setState(0);
            sc.setDevResult(0);
            sc.setAssignTime(null);
            sc.setAssignMan("");
        }
        //插入
        AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"添加失败！");
    }

    /*批量删除*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void  deleteall(Integer[] ids){
        //判断是否为空
        AssertUtil.isTrue((ids==null || ids.length==0),"删除的ID不存在！");
        System.out.println(Arrays.toString(ids));
        //删除是否成功
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) < 0,"删除失败~~~");
    }


    //校验
    private void checkInfo(String customerName, String chanceSource, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "用户名称不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(chanceSource),"机会来源不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"联系人不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"联系电话不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"电话号码不合法！");
    }
}
