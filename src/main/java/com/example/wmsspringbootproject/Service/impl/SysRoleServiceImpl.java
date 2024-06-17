package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.converter.SysRoleConverter;
import com.example.wmsspringbootproject.converter.UserConverter;
import com.example.wmsspringbootproject.mapper.SysRoleMapper;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.Warehouses;
import com.example.wmsspringbootproject.model.form.UserTypeForm;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.query.SysRoleQuery;
import com.example.wmsspringbootproject.model.vo.UserTypeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    private final SysRoleConverter converter;
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
        userTypeVOIPage.setRecords(sysRoleList.getRecords().stream().map(converter::entity2Vo).toList());
        userTypeVOIPage.setPages(sysRoleList.getPages());
        userTypeVOIPage.setCurrent(sysRoleList.getCurrent());
        userTypeVOIPage.setSize(sysRoleList.getSize());
        userTypeVOIPage.setTotal(sysRoleList.getTotal());
        return Result.success(userTypeVOIPage);
    }

    @Override
    public Result<UserTypeVO> getSysRoleByRid(int id) {
        SysRole sysRole=this.getById(id);
        return sysRole ==null ? Result.failed("没有该角色") : Result.success(converter.entity2Vo(sysRole));
    }

    @Override
    public Result<Boolean> saveSysRole(UserTypeForm form) {
        SysRole sysRole =converter.form2Entity(form);
        sysRole.setDeleted(1);
        Boolean result=this.save(sysRole);
        return Result.success(result);
    }

    @Override
    public Result<Boolean> updateSysRole(UserTypeForm form) {
        SysRole sysRole =this.getById(form.getId());
        if(sysRole !=null){
            SysRole target=converter.form2Entity(form);
            target.setStatus(TextUtil.textIsEmpty(form.getStatus()) ? sysRole.getStatus() : form.getStatus());
            target.setDeleted(TextUtil.textIsEmpty(form.getDeleted()) ? sysRole.getDeleted() : form.getDeleted());
            Boolean result=this.baseMapper.updateById(target)>0;
            return result ? Result.success(result) : Result.failed("更新角色信息失败");
        }
        return Result.failed("角色信息不存在");
    }

    @Override
    public Result<Boolean> removeSysRole(String ids) {
        String[] idArray=ids.split(",");
        if(idArray.length>1){
            for (String id : idArray) {
                SysRole sysRole=this.getById(id);
                sysRole.setDeleted(2);
                this.baseMapper.updateById(sysRole);
            }
            return Result.success();
        }else{
            SysRole sysRole=this.getById(ids);
            sysRole.setDeleted(2);
            Boolean result=this.baseMapper.updateById(sysRole)>0;
            return result ? Result.success(result) : Result.failed("删除角色信息失败");
        }
    }

}
