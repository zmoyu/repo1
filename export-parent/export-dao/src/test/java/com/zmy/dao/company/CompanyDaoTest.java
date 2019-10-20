package com.zmy.dao.company;

import com.zmy.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class CompanyDaoTest {

    @Autowired
    private CompanyDao companyDao;

    Company company = new Company();

    @Test
    public void findAll() {
        companyDao.findAll();
    }

    @Test
    public void save() {
        company.setId("1000");
        companyDao.save(company);
    }
}
