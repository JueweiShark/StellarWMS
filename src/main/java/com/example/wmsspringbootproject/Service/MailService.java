package com.example.wmsspringbootproject.Service;

import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.Users;

public interface MailService {

    Result sendEmail(Users user);
}
