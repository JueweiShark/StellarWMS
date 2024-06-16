package com.example.wmsspringbootproject.Service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysRoleService;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.converter.UserConverter;
import com.example.wmsspringbootproject.mapper.UserMapper;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
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


    @Override
    public List<Users> UserList(UserQuery userQuery) {
        String keyword = userQuery.getKeyword();
        String type = userQuery.getType();
        String nick_name=userQuery.getNick_name();
        String email=userQuery.getEmail();
        String phone=userQuery.getPhone();
        List<Users> usersList = this.list(
                new LambdaQueryWrapper<Users>()
                        .like(StrUtil.isNotBlank(keyword), Users::getName, keyword)
                        .like(nick_name != null, Users::getNickName, nick_name)
                        .eq(email != null, Users::getEmail, email)
                        .eq(phone != null, Users::getPhone, phone)
                        .select(
                                Users::getId,
                                Users::getName,
                                Users::getNickName,
                                Users::getPassword,
                                Users::getAvatar,
                                Users::getEmail,
                                Users::getPhone,
                                Users::getWeChatName,
                                Users::getCreateTime,
                                Users::getLastLogin,
                                Users::getDeleted
                        )
        );
        if (CollectionUtil.isEmpty(usersList)) {
            return Collections.EMPTY_LIST;
        }
        return usersList;
    }

    @Override
    public Result<Users> addUser(UserForm userForm) {
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
            return Result.result("407","该用户名已存在",null);
        }
        if(count1!=0){
            return Result.result("408","该邮箱号已存在",null);
        }
        Users entity = userConverter.form2Entity(userForm);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        boolean result = this.save(entity);
        if(!result){
            return Result.result("401","操作失败",null);
        }
        return Result.success(entity);
    }

    @Override
    public Boolean updateUser(UserForm userForm) {
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
        Assert.isTrue(count == 0, "该用户名已存在");
        Assert.isTrue(count1 == 0, "该手机号已存在");
        Assert.isTrue(count2 == 0, "该邮箱号已存在");
        Users entity = userConverter.form2Entity(userForm);
        entity.setId(id);
        boolean result = this.updateById(entity);
        Assert.isTrue(result, "用户修改失败");
        return result;
    }

    @Override
    public Boolean deleteUser(int id) {
        Users users1 =this.baseMapper.selectOne(new LambdaQueryWrapper<Users>()
                .eq(id>0, Users::getId,id)
        );
        boolean result=false;
        if(users1 !=null){
            users1.setDeleted(1);
            result=this.updateById(users1);
        }
        Assert.isTrue(result, "用户删除失败");
        return result;
    }

    @Override
    public Users getUserAuthInfo(String username) {
        Users user = this.baseMapper.getAuthorityInfo(username);
        user.setDataScope(roleService.getMaxDataScope(user.getRoles()));
        return user;
    }

    @Override
    public Users getRootUser() {
        return this.baseMapper.getRootUser();
    }


    public Result<UserForm> LoginByName(UserForm userForm, Users users){
        if(userForm.getName().equals(users.getName())
            && userForm.getPassword().equals(users.getPassword()) ){
            return Result.success(userForm);
        }
        return Result.result("404","找不到该用户",userForm);
    }
    public Result<UserForm> LoginByEMail(UserForm userForm, Users users){
        if(userForm.getEmail().equals(users.getEmail())){
            return Result.success(userForm);
        }
        return Result.result("405","找不到该用户",userForm);
    }
}
