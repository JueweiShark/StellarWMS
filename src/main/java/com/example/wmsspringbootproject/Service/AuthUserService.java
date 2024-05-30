package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.form.UserForm;

public interface AuthUserService {
    LoginResult Login(UserForm userForm);
}
