package com.example.wmsspringbootproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.model.entity.Transactions;
import org.apache.ibatis.annotations.Mapper;

@Mapper
//@DataPermission(warehouseIdColumnName="id")
public interface TransactionMapper extends BaseMapper<Transactions> {
}
