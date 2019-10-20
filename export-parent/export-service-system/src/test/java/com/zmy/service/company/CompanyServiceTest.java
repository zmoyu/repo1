package com.zmy.service.company;

import com.github.pagehelper.PageInfo;
import com.zmy.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//classpath:加载当前项目类路径下的配置文件
//classpath*:加载所有项目类路径下的配置文件
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    Company company = new Company();

    @Test
    public void findAll() {
        System.out.println(companyService.getClass());
        System.out.println(companyService.findAll());
    }

    @Test
    public void save() {
        company.setId("123");
        companyService.save(company);
    }

    @Test
    public void findByPage() {
        int pageNum = 1;
        int pageSize = 2;
        PageInfo<Company> pageInfo = companyService.findByPage(pageNum, pageSize);
        System.out.println("pageInfo = " + pageInfo);
    }
}
