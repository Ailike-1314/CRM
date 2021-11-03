package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.Module;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import com.yjxxt.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Resource
    ModuleMapper moduleMapper;
    @Resource
    PermissionMapper permissionMapper;


    //被下面方法代替
    public List<TreeDto> findAll(){
       return moduleMapper.findAll();
    }

    //查询资源，显示资源
    public List<TreeDto> findAll(Integer roleId){
        //获取全部资源
        List<TreeDto> listTree=moduleMapper.findAll();
        //获取角色的已经拥有的权限
        List<Integer> listId=permissionMapper.selectByRoleId(roleId);
        //循环判端
        for (TreeDto tree: listTree) {
            if(listId.contains(tree.getId())){
                tree.setChecked(true);
            }
        }
        return listTree;
    }

    /*查询全部菜单*/
    public Map<String, Object> findModule() {
        Map<String,Object> map=new HashMap<>();
        List<Module> listM=new ArrayList<>();
        //查出全部module资源
        listM=moduleMapper.findModule();
        //存入map集合中
        map.put("code",0);
        map.put("msg","success");
        map.put("data",listM);
        map.put("count",listM.size());
        return map;
    }

    /*添加*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveModule(Module module){
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请输入菜单名!");
        Integer grade =module.getGrade();
        AssertUtil.isTrue(null== grade|| !(grade==0||grade==1||grade==2),"菜单层级不合法!");
        AssertUtil.isTrue(null !=moduleMapper.queryModuleByGradeAndModuleName(module.getGrade(),module.getModuleName()),"该层级下菜单重复!");
        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请指定二级菜单url值");
            AssertUtil.isTrue(null !=moduleMapper.queryModuleByGradeAndUrl(module.getGrade(),module.getUrl()),"二级菜单url不可重复!");
        }
        if(grade !=0){
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null==parentId || null==selectByPrimaryKey(parentId),"请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入权限码!");
        AssertUtil.isTrue(null !=moduleMapper.queryModuleByOptValue(module.getOptValue()),"权限码重复!");

        module.setIsValid((byte)1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(module)<1,"菜单添加失败!");
    }


    /*修改*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateModule(Module module){
        AssertUtil.isTrue(null == module.getId() || null== selectByPrimaryKey(module.getId()),"待更新记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(module.getModuleName()),"请指定菜单名称!");
        Integer grade =module.getGrade();
        AssertUtil.isTrue(null== grade|| !(grade==0||grade==1||grade==2),"菜单层级不合法!");
        Module temp =moduleMapper.queryModuleByGradeAndModuleName(grade,module.getModuleName());
        if(null !=temp){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该层级下菜单已存在!");
        }

        if(grade==1){
            AssertUtil.isTrue(StringUtils.isBlank(module.getUrl()),"请指定二级菜单url值");
            temp =moduleMapper.queryModuleByGradeAndUrl(grade,module.getUrl());
            if(null !=temp){
                AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"该层级下url已存在!");
            }
        }

        if(grade !=0){
            Integer parentId = module.getParentId();
            AssertUtil.isTrue(null==parentId || null==selectByPrimaryKey(parentId),"请指定上级菜单!");
        }
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()),"请输入权限码!");

        temp =moduleMapper.queryModuleByOptValue(module.getOptValue());
        if(null !=temp){
            AssertUtil.isTrue(!(temp.getId().equals(module.getId())),"权限码已存在!");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(module)<1,"菜单更新失败!");
    }



    /*删除*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteModuleById(Integer mid){
        Module temp =selectByPrimaryKey(mid);
        AssertUtil.isTrue(null == mid || null == temp,"待删除记录不存在!");
        /**
         * 如果存在子菜单 不允许删除
         */
        int count = moduleMapper.countSubModuleByParentId(mid);
        AssertUtil.isTrue(count>0,"存在子菜单，不支持删除操作!");
        //  权限表
        count =permissionMapper.countPermissionsByModuleId(mid);
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionsByModuleId(mid)<count,"菜单删除失败!");
        }
        temp.setIsValid((byte) 0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"菜单删除失败!");

    }

    public List<Map<String, Object>> queryAllModulesByGrade(Integer grade) {
        return  moduleMapper.selectAllModuleByGrade(grade);
    }
}
