package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.vo.Result;

public interface AuthUserService {
    Result Login(UserForm userForm);
}
