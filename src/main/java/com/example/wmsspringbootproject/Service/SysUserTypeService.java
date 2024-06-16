package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.model.entity.SysUserType;

public interface SysUserTypeService {

    SysUserType getUserTypeByUid(int id);
    Boolean updateUserType(int userId,int roleId);
    int addUserType(int userId, int roleId);
}
