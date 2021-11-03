package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Permission;
import com.yjxxt.crm.bean.Role;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.mapper.RoleMapper;
import com.yjxxt.crm.query.RoleQuery;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired(required = false)
    RoleMapper roleMapper;
    @Autowired(required = false)
    ModuleMapper moduleMapper;

    @Autowired(required = false)
    PermissionMapper permissionMapper;

    /*查询全部角色*/
    public List<Map<String,Object>> selectAll(Integer userId){
       return roleMapper.findeAll(userId);
    }

    /*条件查询*/
    public Map<String,Object> selectRoles(RoleQuery roleQuery){
        //创建map
        Map<String,Object> map =new HashMap<String,Object>();
        //查数据并分页
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> pageInfo=new PageInfo<>(roleMapper.selectByParams(roleQuery));
        map.put("code",0);
        map.put("msg","success");
        map.put("data",pageInfo.getList());
        map.put("count",pageInfo.getTotal());
        return map;
    }

    /*添加角色*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void insertRole(Role role){
        //判断是否为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"角色名不能为空~");
        //是否重复
        Role role1=roleMapper.selectRoleName(role.getRoleName());
        AssertUtil.isTrue(role1!=null,"角色已存在");
        //设置默认值
        role.setIsValid(1);
        role.setUpdateDate(new Date());
        role.setCreateDate(new Date());
        System.out.println("准备添加~~~~~~~");
        //判断是否插入成功
        AssertUtil.isTrue(insertHasKey(role)<1,"插入失败~");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void changeRoles(Role role){
        //验证当前对象是否存在
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp == null || role.getId()==null, "待修改记录不存在");
        //1.角色名非空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空");
        //2.角色名唯一
        Role temp2 = roleMapper.selectRoleName(role.getRoleName());
        AssertUtil.isTrue(temp2 != null && !(temp2.getId().equals(role.getId())), "角色已经存在");
        //3.设定默认值
        role.setUpdateDate(new Date());
        //4.修改是否成功
        AssertUtil.isTrue(updateByPrimaryKeySelective(role) < 1, "修改失败了");
    }

//    删除
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer id) {
        Role role= roleMapper.selectByPrimaryKey(id);
        //判断
        AssertUtil.isTrue(id==null || role==null,"删除记录不存在~");
        //默认
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        System.out.println(role.toString());
        //删除是否成功
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)<1,"删除失败~");
    }

    //添加权限
    /*需要判断添加权限之前，有没有权限*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId,Integer[] mids) {
        //判断
        System.out.println(roleId+"---------------------------------"+mids);
        AssertUtil.isTrue(roleId==null|| roleMapper.selectByPrimaryKey(roleId)==null,"请选择角色");
        //统计权限
        int count =permissionMapper.countRoleModule(roleId);
        if(count>0){
            //直接删除
            AssertUtil.isTrue(permissionMapper.deleteModule(roleId)!=count,"资源分配失败");
        }
        //定义一个list集合进行存储
        List<Permission> list=new ArrayList<>();
        if(mids!=null || mids.length>0){
            for (Integer mid: mids) {
                Permission permission=new Permission();
                permission.setRoleId(roleId);
                permission.setModuleId(mid);
                //通过module的mid获取OptValue
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                list.add(permission);
            }
        }
        if(list!=null){
            //添加
            AssertUtil.isTrue(permissionMapper.insertBatch(list)!=list.size(),"授权失败~");
        }




    }

}
