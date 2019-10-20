package com.zmy.service.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Module;
import com.zmy.domain.system.User;

import java.util.List;

public interface UserService {

    /**
     * 分页查询
     *
     * @param companyId 根据企业id
     * @param pageNum   当前页
     * @param pageSize  页大小
     * @return
     */
    PageInfo<User> findByPage(String companyId, int pageNum, int pageSize);

    /**
     * 根据id查询部门
     *
     * @param id
     * @return
     */
    User findById(String id);

    //根据公司id查询所有部门
    List<User> findAll(String companyId);

    //保存
    void save(User user);

    //修改
    void update(User user);

    //删除
    boolean delete(String id);

    //给用户分配角色（添加修改用户角色）
    void updateUserRoles(String userId, String[] roleIds);

    //根据用户输入的邮箱查找用户
    List<User> findByEmail(String email);

    //获取当前用户所拥有的权限（菜单）
    List<Module> findUserModuleByUserId(String userId);
}
