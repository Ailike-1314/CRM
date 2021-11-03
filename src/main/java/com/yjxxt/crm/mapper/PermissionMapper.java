package com.yjxxt.crm.mapper;

import com.yjxxt.crm.base.BaseMapper;
import com.yjxxt.crm.bean.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    int countRoleModule(Integer roleId);

    int deleteModule(Integer roleId);

    List<Integer> selectByRoleId(Integer roleId);

    List<String> selectByUserIdGetModelId(Integer userId);

    int countPermissionsByModuleId(Integer mid);

    int deletePermissionsByModuleId(Integer mid);
}