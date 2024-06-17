package com.example.wmsspringbootproject.converter;

import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.entity.Warehouses;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.form.UserTypeForm;
import com.example.wmsspringbootproject.model.form.WareHouseForm;
import com.example.wmsspringbootproject.model.vo.UserTypeVO;
import com.example.wmsspringbootproject.model.vo.UserVO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SysRoleConverter {
    UserTypeVO entity2Vo(SysRole entity);
    SysRole form2Entity(UserTypeForm form);
}
