package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    int countRole(Integer userId);

    void deleteRoleById(Integer userId);
}