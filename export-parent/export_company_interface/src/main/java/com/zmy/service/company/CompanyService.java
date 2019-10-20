package com.zmy.service.company;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.company.Company;

import java.util.List;

/**
 * companyService接口
 */
public interface CompanyService {

    /**
     * 分页查询
     * @param pageNum  当前页
     * @param pageSize  页大小
     * @return 返回pageHelper提供的分页参数的pageInfo对象
     */
    PageInfo<Company> findByPage(int pageNum, int pageSize);

    //查询所有公司
    List<Company> findAll();

    //保存公司
    void save(Company company);

    //修改公司
    void update(Company company);

    /**
     * 根据id查询
     * @param id 主键
     * @return
     */
    Company findById(String id);

    /**
     * 删除企业
     * @param id
     */
    void toDelete(String id);

}
