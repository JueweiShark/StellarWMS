package com.example.wmsspringbootproject.Service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.mapper.SysRoleMapper;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.SysUserType;
import com.example.wmsspringbootproject.model.entity.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public Integer getMaxDataScope(Set<String> roles) {
        return this.baseMapper.getMaxDataScope(roles);
    }
    @Override
    public Result<List<SysRole>> getUserType() {
        List<SysRole> userTypeList = this.list(
                new LambdaQueryWrapper<SysRole>().select(
                        SysRole::getId,
                        SysRole::getName,
                        SysRole::getCode,
                        SysRole::getSort,
                        SysRole::getStatus,
                        SysRole::getDeleted,
                        SysRole::getDataScope
                )
        );
        if (CollectionUtil.isEmpty(userTypeList)) {
            return Result.success(Collections.EMPTY_LIST);
        }
        return Result.success(userTypeList);
    }

    @Override
    public SysRole getSysRoleByRid(int id) {
        return this.lambdaQuery().eq(SysRole::getId, id).getEntity();
    }

}
