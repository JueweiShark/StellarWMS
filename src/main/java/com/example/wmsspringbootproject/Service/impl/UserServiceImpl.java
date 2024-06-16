package com.example.wmsspringbootproject.Service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.Service.SysUserTypeService;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.ResultCode;
import com.example.wmsspringbootproject.model.entity.*;
import com.example.wmsspringbootproject.model.query.ProductQuery;
import com.example.wmsspringbootproject.model.vo.ProductVO;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.converter.UserConverter;
import com.example.wmsspringbootproject.mapper.UserMapper;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserConverter userConverter;

    private final SysRoleService roleService;
    private final SysUserTypeService sysUserTypeService;


//
@Override
public Result<IPage<UserVO>> UserList(UserQuery query) {
    LambdaQueryWrapper<Users> queryWrapper=new LambdaQueryWrapper<>();
    Page<Users> userPage=new Page<>(query.getPageNum(),query.getPageSize());
    if (query != null) {
        queryWrapper.gt(Users::getId,0);

        if (!TextUtil.textIsEmpty(query.getKeyword())) {
            queryWrapper.and(wrapper -> wrapper.like(Users::getName, query.getKeyword()));
        }
        if (!TextUtil.textIsEmpty(query.getNick_name())) {
            queryWrapper.and(wrapper -> wrapper.like(Users::getNickName, query.getNick_name()));
        }
        if (!TextUtil.textIsEmpty(query.getPhone())) {
            queryWrapper.and(wrapper -> wrapper.like(Users::getPhone, query.getPhone()));
        }
        if (!TextUtil.textIsEmpty(query.getEmail())) {
            queryWrapper.and(wrapper -> wrapper.like(Users::getEmail, query.getEmail()));
        }
        if (query.getStatus()!=0) {
            queryWrapper.and(wrapper -> wrapper.like(Users::getStatus, query.getStatus()));
        }
        if (query.getTypeId()!=0) {
            queryWrapper.and(wrapper -> wrapper.like(Users::getTypeId, query.getTypeId()));
        }
        if (!TextUtil.textIsEmpty(query.getCreateTime())) {
            queryWrapper.and(wrapper -> wrapper.ge(Users::getCreateTime, query.getCreateTime()));
        }
        if (!TextUtil.textIsEmpty(query.getEndTime())) {
            queryWrapper.and(wrapper -> wrapper.le(Users::getCreateTime, query.getEndTime()));
        }
        queryWrapper.and(wrapper -> wrapper.eq(Users::getDeleted, 1));
    }
    IPage<Users> userList =this.page(userPage,queryWrapper);
    IPage<UserVO> userVOIPage = new Page<>();
    userVOIPage.setRecords(userList.getRecords().stream().map(user -> {
        UserVO userVO = userConverter.entity2Vo(user);
        userVO.setTypeId(sysUserTypeService.getUserTypeByUid(userVO.getId()).getRoleId());
        return userVO;
    }).toList());
    userVOIPage.setPages(userList.getPages());
    userVOIPage.setCurrent(userList.getCurrent());
    userVOIPage.setSize(userList.getSize());
    userVOIPage.setTotal(userList.getTotal());
    return Result.success(userVOIPage);
}

    @Override
    public Result<Boolean> addUser(UserForm userForm) {
        String name = userForm.getName();
        userForm.setName(name);
        String email=userForm.getEmail();
        long count = this.count(new LambdaQueryWrapper<Users>()
                .eq(Users::getName, name)
        );
        long count1 = this.count(new LambdaQueryWrapper<Users>()
                .eq(Users::getEmail, email)
        );
        if(count!=0){
            return Result.failed(ResultCode.USER_NAME_EXISTS);
        }
        if(count1!=0){
            return Result.failed(ResultCode.USER_EMAIL_EXISTS);
        }
        Users entity = userConverter.form2Entity(userForm);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setDeleted(1);
        boolean result = this.save(entity);
        sysUserTypeService.addUserType(entity.getId(),userForm.getTypeId());
        return result ? Result.success(result) : Result.failed(ResultCode.USER_OPERATE_ERROR);
    }

    @Override
    public Result<Boolean> updateUser(UserForm userForm) {
        int id=userForm.getId();
        String name = userForm.getName();
        String phone=userForm.getPhone();
        String email=userForm.getEmail();
        long count = this.count(new LambdaQueryWrapper<Users>()
                .eq(Users::getName, name)
                .ne(Users::getId, id)
        );
        long count1 = this.count(new LambdaQueryWrapper<Users>()
                .eq(Users::getPhone, phone)
                .ne(Users::getId, id)
        );
        long count2 = this.count(new LambdaQueryWrapper<Users>()
                .eq(Users::getEmail, email)
                .ne(Users::getId, id)
        );
        if(count!=0){
            return Result.failed(ResultCode.USER_NAME_EXISTS);
        }
        if(count1!=0){
            return Result.failed(ResultCode.USER_PHONE_EXISTS);
        }
        if(count2!=0){
            return Result.failed(ResultCode.USER_EMAIL_EXISTS);
        }
        Users entity = userConverter.form2Entity(userForm);
        entity.setId(id);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        sysUserTypeService.updateUserType(id,userForm.getTypeId());
        boolean result = this.updateById(entity);
        return result ? Result.success(result) : Result.failed(ResultCode.USER_OPERATE_ERROR);
    }

    @Override
    public Result<Boolean> deleteUser(String ids) {
        String[] idArray=ids.split(",");
        if(idArray.length>1){
            for (String id : idArray) {
                Users users=this.getById(id);
                users.setDeleted(0);
                this.baseMapper.updateById(users);
            }
            return com.example.wmsspringbootproject.common.result.Result.success();
        }else{
            Users users=this.getById(ids);
            users.setDeleted(0);
            Boolean result=this.baseMapper.updateById(users)>0;
            return result ? Result.success(result) : Result.failed(ResultCode.USER_OPERATE_ERROR);
        }
    }

    @Override
    public Result<Boolean> resetPassword(int uid) {
        Users users=this.baseMapper.selectById(uid);
        if(users!=null ) {
            users.setPassword(passwordEncoder.encode("123"));
            Boolean result=this.lambdaUpdate().eq(Users::getId, uid).update(users);
            return result ? Result.success(result) : Result.failed(ResultCode.USER_OPERATE_ERROR);
        }else{
            return Result.failed(ResultCode.USER_NOT_EXIST);
        }
    }

    @Override
    public Users getRootUser() {
        return this.baseMapper.getRootUser();
    }


    @Override
    public Users getUserAuthInfo(String username) {
        Users user = this.baseMapper.getAuthorityInfo(username);
        user.setDataScope(roleService.getMaxDataScope(user.getRoles()));
        return user;
    }

    public Result LoginByName(UserForm userForm, Users users){
        if(userForm.getName().equals(users.getName())
            && userForm.getPassword().equals(users.getPassword()) ){
            return Result.success(userForm);
        }
        return Result.failed(ResultCode.USER_NOT_EXIST);
    }
    public Result LoginByEMail(UserForm userForm, Users users){
        if(userForm.getEmail().equals(users.getEmail())){
            return Result.success(userForm);
        }
        return Result.failed(ResultCode.USER_NOT_EXIST);
    }
}
