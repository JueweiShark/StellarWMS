package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysUserTypeService;
import com.example.wmsspringbootproject.mapper.SysUserTypeMapper;
import com.example.wmsspringbootproject.model.entity.SysUserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserTypeServiceImpl extends ServiceImpl<SysUserTypeMapper, SysUserType> implements SysUserTypeService {


    @Override
    public SysUserType getUserTypeByUid(int id) {
        return this.lambdaQuery().eq(SysUserType::getUserId, id).one();
    }

    @Override
    public Boolean updateUserType(int userId, int roleId) {
        SysUserType sysUserType=getUserTypeByUid(userId);
        if(sysUserType!=null ) {
            sysUserType.setRoleId(roleId);
            return this.lambdaUpdate().eq(SysUserType::getUserId, userId).update(sysUserType);
        }else{
            return false;
        }
    }
    @Override
    public int addUserType(int userId, int roleId) {
        SysUserType sysUserType=new SysUserType();
        sysUserType.setUserId(userId);
        sysUserType.setRoleId(roleId);
        return this.baseMapper.insert(sysUserType);
    }
}
