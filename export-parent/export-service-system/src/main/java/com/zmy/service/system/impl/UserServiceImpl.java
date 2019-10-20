package com.zmy.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zmy.dao.system.ModuleDao;
import com.zmy.dao.system.UserDao;
import com.zmy.domain.system.Module;
import com.zmy.domain.system.User;
import com.zmy.service.system.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ModuleDao moduleDao;

    /**
     * @param companyId 根据企业id
     * @param pageNum   当前页
     * @param pageSize  页大小
     * @return
     */
    @Override
    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize) {

        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        //调用dao
        List<User> list = userDao.findAll(companyId);

        return new PageInfo<>(list);
    }

    /**
     * 根据部门id查询部门
     *
     * @param id
     * @return
     */
    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    /**
     * 根据公司id查询所有部门
     *
     * @param companyId
     * @return
     */
    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    /**
     * //保存
     *
     * @param user
     */
    @Override
    public void save(User user) {
        user.setId(UUID.randomUUID().toString());
        //添加用户的时候，需要对用户的密码加密，加盐，
        // 这样就可以使用添加的用户登陆了
        if(user.getPassword()!=null){
            String encodePassword = new Md5Hash(user.getPassword(), user.getEmail()).toString();
            user.setPassword(encodePassword);
        }
        userDao.save(user);
    }

    /**
     * //修改
     *
     * @param user
     */
    @Override
    public void update(User user) {
        userDao.update(user);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public boolean delete(String id) {

        //-- 先根据删除的用户id查询用户角色中间表
        //SELECT COUNT(1) FROM pe_role_user WHERE user_id='1'
        //-- 如果没有查到，可以删除
        //DELETE FROM pe_user WHERE user_id='1'
        long count = userDao.findUserRoleByUserId(id);

        //判断
        if(count==0){
            //可以删除
            userDao.delete(id);
            return true;
        }
        return false;
    }

    /**
     * 修改用户角色信息
     * @param userId
     * @param roleIds
     */
    @Override
    public void updateUserRoles(String userId, String[] roleIds) {

        //根据userId删除中间表信息(先解除用户角色关系)
        userDao.deleteUserRoleByUserId(userId);

        //循环角色id列表，向中间表保存数据（给用户添加角色）
        //2) 给用户添加角色
        if (roleIds != null && roleIds.length>0){
            for (String roleId : roleIds) {
                userDao.saveUserRole(userId,roleId);
            }
        }

    }

    /**
     * 根据用户输入的邮箱查找用户
     * @param email
     * @return
     */
    @Override
    public List<User> findByEmail(String email) {
        //调用dao
        return userDao.findByEmail(email);
    }

    /**
     * 获取当前用户所拥有的权限（菜单）
     *       1.根据用户id查询当前用户
     *       2.根据用户的degree级别判断
     *       3.如果degree==0 （内部的sass管理）
     * *   根据模块中的belong字段进行查询，belong = "0";
     * *  4.如果degree==1 （租用企业的管理员）
     * *   根据模块中的belong字段进行查询，belong = "1";
     * *  5.其他的用户类型
     * *   借助RBAC的数据库模型，多表联合查询出结果
     * @param userId
     * @return
     */
    @Override
    public List<Module> findUserModuleByUserId(String userId) {
        //得到用户
        User user = userDao.findById(userId);

        //根据用户的degree级别判断
        if(user.getDegree()==0){
            //3.如果degree==0 （内部的sass管理）
            return moduleDao.findByBelong("0");
        }else if(user.getDegree()==1){
            //4.如果degree==1 (租用企业管理）
            return moduleDao.findByBelong("1");
        }else {
            //5.其他的用户类型
            return moduleDao.findUserModuleByUserId(userId);
        }
    }
}
