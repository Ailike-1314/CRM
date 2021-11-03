package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    UserMapper userMapper;
    @Resource
    UserRoleMapper userRoleMapper;

    /*用户登录*/
    public UserModel userLogin(String userName,String userPwd){
        //判断用户名和密码是否为空
        checkNull(userName,userPwd);
        //检查是否已经存在
        User user=userMapper.selectByuserName(userName);
        AssertUtil.isTrue(user==null,"用户名不存在！");
        //检查密码
        checkPwd(userPwd,user.getUserPwd());
        return buildInfo(user);
    }
    //设置返回信息
    private UserModel buildInfo(User user) {
        UserModel um=new UserModel();
        um.setUserName(user.getUserName());
        um.setUserId(UserIDBase64.encoderUserID(user.getId()));//对ID加密
        um.setTrueName(user.getTrueName());
        return um;
    }
    //密码验证
    private void checkPwd(String userPwd, String userPwd1) {
        String enPwd=Md5Util.encode(userPwd);
        AssertUtil.isTrue(!enPwd.equals(userPwd1),"密码不正确！");
    }
    //判断是否为空
    private  void checkNull(String userName,String userPwd){
        //判断用户名和密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空！");
    }

    /*修改密码*/
    public void updatePwd(int userId,String oldPwd,String newPwd,String againPwd){
        //通过id获取用户
        System.out.println(userId);
        User user=userMapper.selectByPrimaryKey(userId);
        System.out.println(user);
        //检验并修改密码
        isOkForPwd(user,oldPwd,newPwd,againPwd);
        //修改密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //修改数据库内容
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"操作失败");

    }
    //检验密码
    private void isOkForPwd(User user, String oldPwd, String newPwd, String againPwd) {
        //判断用户是否为空
        AssertUtil.isTrue(user==null,"用户未登录或不存在！");
        //原密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原密码不能为空！");
        //新密码判断是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空！");
        //对原密码判断是否正确
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPwd))),"原密码不正确！");
        //对新密码和原密码进行判断
        AssertUtil.isTrue(oldPwd.equals(newPwd),"原密码和新密码不能一致！");
        //对再次输入判断是否为空
        AssertUtil.isTrue(StringUtils.isBlank(againPwd),"二次验证密码不能为空！");
        //判断新密码与二次输入
        AssertUtil.isTrue(!(newPwd.equals(againPwd)),"新密码与第二次输入不一致！");

    }

    /*查询所有销售人员*/
    public List<Map<String,Object>> selectSales(){
        System.out.println(userMapper.selectSales());
       return userMapper.selectSales();
    }

    /*多条件查询*/
    public Map<String,Object> manyIfselect(UserQuery userQuery){
        Map<String,Object> map=new HashMap<String,Object>();
        //设置分页
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        //对数据进行分页处理
        PageInfo<User> pageInfo=new PageInfo<User>(userMapper.selectByParams(userQuery));
        //基本数据
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        System.out.println(map);
        return map;
    }

    /*用户增加*/
    @Transactional(propagation =Propagation.REQUIRED)
    public void addUser(User user){
        //对用户名，邮箱，电话进行判断
        checkData(user.getUserName(),user.getEmail(),user.getPhone());
        //是否重复
        User us=userMapper.selectByuserName(user.getUserName());
        System.out.println("开始添加");
        AssertUtil.isTrue(us!=null,"此用户已存在~");
        //默认设置为有效
        user.setIsValid(1);
        //默认时间
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //密码加密
        user.setUserPwd(Md5Util.encode("123456"));
        //判断是否插入成功
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加失败");
        System.out.println(user.getId()+"--------------"+user.getRoleIds());
        //插入用户角色中间表信息
        insertUserRole(user.getId(),user.getRoleIds());
    }

    /*对中间表进行插入*/
    private void insertUserRole(Integer userId, String roleIds) {
        //判断是否存在
        AssertUtil.isTrue(StringUtils.isBlank(roleIds),"请选择角色~");
        //list集合进行批量插入
        List<UserRole> urlist=new ArrayList<>();
        //删除角色
        int  count=userRoleMapper.countRole(userId);
        if(count>0){
            userRoleMapper.deleteRoleById(userId);
        }
        //获取roleIds
        String[] ids=roleIds.split(",");
        //设置循环
        for (int i = 0; i <ids.length ; i++) {
            //设置一个用户角色的内容
            UserRole userRole=new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(Integer.parseInt(ids[i]));
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            //添加到集合中
            urlist.add(userRole);
        }
        //集合插入
        AssertUtil.isTrue(userRoleMapper.insertBatch(urlist)!=urlist.size(),"添加角色失败！");
    }

    //对用户名，邮箱，电话判断
    private void checkData(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空~");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"电话号码不能为空哟~");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"请输入正确的号码~");
    }

    /*修改用户*/
    @Transactional(propagation =Propagation.REQUIRED)
    public void update(User user){
        //修改时会传来一个id
        User temp=userMapper.selectByPrimaryKey(user.getId());
        //判断是否存在
        AssertUtil.isTrue(temp==null,"修改用户不存在~");
        //判断是否只是修改本条记录
        System.out.println("开始编辑啦");
        User us=userMapper.selectByuserName(user.getUserName());
        System.out.println(us.toString());
        AssertUtil.isTrue(!(user.getId().equals(us.getId()))&&us!=null,"用户名已经存在!");
        System.out.println(!(user.getId().equals(us.getId()))&&us!=null);
        //检查
        checkData(user.getUserName(),user.getEmail(),user.getPhone());
        user.setUpdateDate(new Date());
        //判断是否修改成功、
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"修改失败~");
        System.out.println("");
        //对角色的修改
        insertUserRole(user.getId(),user.getRoleIds());
    }

    /*批量删除*/
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletIds(Integer[] ids){
        //判断id
        AssertUtil.isTrue(ids.length==0||ids==null,"删除的用户不存在~");
        //删除角色
        //删除角色
        for (Integer userId: ids) {
            int  count=userRoleMapper.countRole(userId);
            if(count>0){
                userRoleMapper.deleteRoleById(userId);
            }
        }

        //判断是否删除成功
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<0,"删除失败~");
    }

}
