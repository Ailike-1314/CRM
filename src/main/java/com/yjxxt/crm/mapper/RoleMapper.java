package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.query.RoleQuery;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {

    //查询全部角色
    List<Map<String,Object>> findeAll(Integer userId);

    Role selectRoleName(String roleName);
}