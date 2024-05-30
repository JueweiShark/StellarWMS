package com.example.wmsspringbootproject.converter;

import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.vo.UserVO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserConverter {
    UserForm entity2Form(Users entity);
    UserVO entity2Vo(Users entity);

    Users Vo2entity(UserVO vo);

    Users form2Entity(UserForm userForm);
}
