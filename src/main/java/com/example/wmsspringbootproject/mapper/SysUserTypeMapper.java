package com.example.wmsspringbootproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.SysUserType;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface SysUserTypeMapper extends BaseMapper<SysUserType> {

}
