package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.UserType;
import com.example.wmsspringbootproject.model.query.SysRoleQuery;
import com.example.wmsspringbootproject.model.vo.UserTypeVO;

import java.util.List;
import java.util.Set;

public interface SysRoleService {

    Integer getMaxDataScope(Set<String> roles);
    Result<IPage<UserTypeVO>> getUserType(SysRoleQuery query);

    SysRole getSysRoleByRid(int id);
}
