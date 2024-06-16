package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;


import java.util.List;

public interface UserService extends IService<Users> {
    List<Users> UserList(UserQuery userQuery);
    Result<Users> addUser(UserForm  userForm);
    Boolean updateUser(UserForm userForm);
    Boolean deleteUser(int id);

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link Users}
     */
    Users getUserAuthInfo(String username);

    Users getRootUser();

}
