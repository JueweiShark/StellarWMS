package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.UserType;

import java.util.List;
import java.util.Set;

public interface SysRoleService {

    Integer getMaxDataScope(Set<String> roles);
    Result<List<SysRole>> getUserType();
    SysRole getSysRoleByRid(int id);
}
