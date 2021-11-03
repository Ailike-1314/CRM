package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    //通过名字进行查询
    User selectByuserName(String userName);

    //查询所有销售人员
   List<Map<String,Object>> selectSales();

}