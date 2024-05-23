package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.query.UserQuery;
import com.example.wmsspringbootproject.model.vo.Result;

import java.util.List;

public interface UserService {
    List<Users> UserList(UserQuery userQuery);
    Result Login(UserForm userForm);
    Result addUser(UserForm  userForm);
    Boolean updateUser(UserForm userForm);
    Boolean deleteUser(int id);

}
