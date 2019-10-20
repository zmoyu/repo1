package com.zmy.service.system;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.system.Role;

import java.util.List;

public interface RoleService {
    /**
     * 分页查询
     * @param companyId 企业id
     * @param pageNum   当前页
     * @param pageSize  页大小
     * @return
     */
    PageInfo<Role>  findByPage(String companyId,int pageNum,int pageSize);

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    Role findById(String id);

    /**
     * 根据id删除
     * @param id
     */
    void delete(String id);

    /**
     * 保存
     * @param role
     */
    void save(Role role);

    /**
     * 修改
     * @param role
     */
    void update(Role role);

    /**
     *通过角色Id和权限修改权限（角色分配权限）
     * @param roleId
     * @param moduleIds
     */
    void updateRoleModule(String roleId, String moduleIds);

    /**
     *根据公司id查询全部角色
     * @param CompanyId
     * @return
     */
    List<Role> findAll(String CompanyId);

    /**
     * 根据用户ID查询当前用户所具有的的角色
     * @param userId
     * @return
     */
    List<Role> findUserRole(String userId);
}
