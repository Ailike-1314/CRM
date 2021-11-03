package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    PermissionMapper permissionMapper;

    /*通过用户id获取角色资源权限*/
    public List<String> selectByUserIdGetModelId(Integer userId){
        return permissionMapper.selectByUserIdGetModelId(userId);
    }
}
