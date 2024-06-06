package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.mapper.SysRoleMapper;
import com.example.wmsspringbootproject.model.entity.SysRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public Integer getMaxDataScope(Set<String> roles) {
        return this.baseMapper.getMaxDataScope(roles);
    }
}
