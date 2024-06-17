package com.example.wmsspringbootproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wmsspringbootproject.model.entity.Users;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<Users> {
    Users getAuthorityInfo(String username);

    Users getRootUser();
}
