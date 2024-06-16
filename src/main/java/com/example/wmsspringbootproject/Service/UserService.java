package com.example.wmsspringbootproject.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.model.vo.UserVO;

import java.util.List;

public interface UserService {
    Result<IPage<UserVO>> UserList(UserQuery userQuery);
    Result<Boolean> addUser(UserForm  userForm);
    Result<Boolean> updateUser(UserForm userForm);
    Result<Boolean> deleteUser(String ids);
    Result<Boolean> resetPassword(int uid);

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link Users}
     */
    Users getUserAuthInfo(String username);
    Users getRootUser();
}
