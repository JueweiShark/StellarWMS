package com.example.wmsspringbootproject.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.bo.RolePermsBO;
import com.example.wmsspringbootproject.model.entity.SysRoleMenu;
import com.example.wmsspringbootproject.model.form.RoleMenuForm;


import java.util.List;

/**
 * 角色菜单业务接口
 *
 * @author haoxr
 * @since 2.5.0
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合
     */
    List<Long> listMenuIdsByRoleId(Long roleId);
    Result<List<SysRoleMenu>> listByRoleId(Long roleId);
    Result<Boolean> updateRoleMenu(RoleMenuForm roleMenuForm);


    /**
     * 获取角色和权限的列表
     *
     * @return 角色权限的列表
     */
    List<RolePermsBO> getRolePermsList(String roleCode);
}
