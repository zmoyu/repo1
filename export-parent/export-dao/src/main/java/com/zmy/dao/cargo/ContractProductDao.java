package com.zmy.dao.cargo;

import com.zmy.domain.cargo.ContractProduct;
import com.zmy.domain.cargo.ContractProductExample;
import com.zmy.vo.ContractProductVo;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.util.List;

public interface ContractProductDao {

	//删除
    int deleteByPrimaryKey(String id);

	//保存
    int insertSelective(ContractProduct record);

	//条件查询
    List<ContractProduct> selectByExample(ContractProductExample example);

	//id查询
    ContractProduct selectByPrimaryKey(String id);

	//更新
    int updateByPrimaryKeySelective(ContractProduct record);

    //根据船期查询所有货物数据
    List<ContractProductVo> findByShipTime(@Param("companyId")String companyId,
                                           @Param("shipTime")String shipTime);
}