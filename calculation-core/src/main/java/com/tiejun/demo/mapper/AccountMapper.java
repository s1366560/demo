package com.tiejun.demo.mapper;

import com.tiejun.demo.domain.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface AccountMapper {

    Account selectByAccount(@Param("account") String account);

    int insert(@Param("accountNumber") String accountNumber, @Param("balance") BigDecimal balance);

    Account selectByPrimaryKey(@Param("id") Long id);

    int updateByPrimaryKey(@Param("record") Account record);
}
