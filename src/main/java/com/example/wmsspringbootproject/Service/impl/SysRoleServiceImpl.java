package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.converter.ProductTypeConverter;
import com.example.wmsspringbootproject.converter.UserConverter;
import com.example.wmsspringbootproject.mapper.SysRoleMapper;
import com.example.wmsspringbootproject.model.entity.ProductTypes;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.query.SysRoleQuery;
import com.example.wmsspringbootproject.model.vo.UserTypeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    private final UserConverter converter;
    @Override
    public Integer getMaxDataScope(Set<String> roles) {
        return this.baseMapper.getMaxDataScope(roles);
    }
    @Override
    public Result<IPage<UserTypeVO>> getUserType(SysRoleQuery query) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        Page<SysRole> sysRolePage = new Page<>(query.getPageNum(), query.getPageSize());
        if (query != null) {
            queryWrapper.gt(SysRole::getId, 0);
            if (!TextUtil.textIsEmpty(query.getKeyword())) {
                queryWrapper.and(wrapper -> wrapper.like(SysRole::getName, query.getKeyword()));
            }
            queryWrapper.and(wrapper -> wrapper.eq(SysRole::getDeleted, 1));
        }
        IPage<SysRole> sysRoleList = this.page(sysRolePage, queryWrapper);
        IPage<UserTypeVO> userTypeVOIPage = new Page<>();
        userTypeVOIPage.setRecords(sysRoleList.getRecords().stream().map(converter::entity3Vo).toList());
        userTypeVOIPage.setPages(sysRoleList.getPages());
        userTypeVOIPage.setCurrent(sysRoleList.getCurrent());
        userTypeVOIPage.setSize(sysRoleList.getSize());
        userTypeVOIPage.setTotal(sysRoleList.getTotal());
        return Result.success(userTypeVOIPage);
    }

    @Override
    public SysRole getSysRoleByRid(int id) {
        return this.lambdaQuery().eq(SysRole::getId, id).getEntity();
    }

}
