package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.model.vo.Result;
import com.example.wmsspringbootproject.model.vo.UserVO;

import java.util.List;

public interface UserService {
    List<Users> UserList(UserQuery userQuery);
    Result addUser(UserForm  userForm);
    Boolean updateUser(UserForm userForm);
    Boolean deleteUser(int id);

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link Users}
     */
    Users getUserAuthInfo(String username);

}
