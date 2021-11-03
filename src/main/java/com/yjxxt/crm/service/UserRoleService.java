package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserRoleService extends BaseService<UserRole,Integer> {

    @Autowired(required = false)
    UserRoleMapper userRoleMapper;
}
