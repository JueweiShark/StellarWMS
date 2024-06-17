package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;

public interface AuthUserService {
    Result<LoginResult> Login(UserForm userForm);

    Result getUserInfo(String token);
}
