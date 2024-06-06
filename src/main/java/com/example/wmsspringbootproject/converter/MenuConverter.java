package com.example.wmsspringbootproject.converter;


import com.example.wmsspringbootproject.model.entity.SysMenu;
import com.example.wmsspringbootproject.model.form.MenuForm;
import com.example.wmsspringbootproject.model.vo.MenuVO;
import org.mapstruct.Mapper;

/**
 * 菜单对象转换器
 *
 * @author haoxr
 * @since 2022/7/29
 */
@Mapper(componentModel = "spring")
public interface MenuConverter {

    MenuVO entity2Vo(SysMenu entity);

    MenuForm entity2Form(SysMenu entity);

    SysMenu form2Entity(MenuForm menuForm);

}