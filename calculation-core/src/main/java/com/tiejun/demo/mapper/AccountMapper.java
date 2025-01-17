package com.tiejun.demo.mapper;

import com.tiejun.demo.domain.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface AccountMapper {

    Account selectByAccount(@Param("account") String account);

    int insert(@Param("accountNumber") String accountNumber, @Param("balance") BigDecimal balance);

    int deleteByPrimaryKey(@Param("id") Long id);

    int insert(@Param("record") Account record);

    int insertSelective(@Param("record") Account record);

    Account selectByPrimaryKey(@Param("id") Long id);

    int updateByPrimaryKeySelective(@Param("record") Account record);

    int updateByPrimaryKey(@Param("record") Account record);
}
