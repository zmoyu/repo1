package com.zmy.dao.cargo;

import com.zmy.domain.cargo.Factory;
import com.zmy.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/applicationContext-dao.xml")
public class FactoryDaoTest {

    @Autowired
    private FactoryDao factoryDao;

    @Test
    public void selectById() {
        System.out.println(factoryDao.selectByPrimaryKey("1"));
    }

    @Test
    public void selectAll() {
        //条件:厂家名称
        FactoryExample example = new FactoryExample();
        FactoryExample.Criteria criteria = example.createCriteria();
        criteria.andFactoryNameEqualTo("升华");

        List<Factory> list = factoryDao.selectByExample(example);
        System.out.println("list = " + list);
    }

    // 添加一个厂家
    // insert into co_factory (id, ctype, full_name, factory_name,
    // contacts, phone, mobile, fax, address, inspector, remark,
    // order_no, state, create_by, create_dept, create_time, update_by,
    // update_time ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )
    @Test
    public void insert() {
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setCtype("test..");
        factory.setFactoryName("玻璃厂..");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());

        //添加
        factoryDao.insert(factory);
    }

    // 添加一个厂家, 动态插入(会对象的属性，如果有值，才生成插入sql)
    // insert into co_factory ( id, ctype, factory_name, create_time, update_time )
    // values ( ?, ?, ?, ?, ? )
    @Test
    public void insertSelective() {
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setCtype("test..");
        factory.setFactoryName("玻璃厂..");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());

        //添加
        factoryDao.insertSelective(factory);
    }

    @Test
    public void update() {
        factoryDao.updateByPrimaryKey(null);
        //动态插入，动态插入(会对象的属性，如果有值，才生成插入sql)想改几条sql就生成那几条值

        Factory factory = new Factory();
        factory.setId("1");
        factory.setCtype("货物");
        factory.setFactoryName("祁县华光厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        factoryDao.updateByPrimaryKeySelective(factory);

        //删除
        factoryDao.deleteByPrimaryKey(null);
    }
}
