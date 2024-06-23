package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wmsspringbootproject.model.entity.SysUserType;

public interface SysUserTypeService extends IService<SysUserType> {

    SysUserType getUserTypeByUid(int id);
    Boolean updateUserType(int userId,int roleId);
    int addUserType(int userId, int roleId);
}
