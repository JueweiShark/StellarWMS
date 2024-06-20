package com.example.wmsspringbootproject.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wmsspringbootproject.Service.SysRoleMenuService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.mapper.SysRoleMenuMapper;
import com.example.wmsspringbootproject.model.bo.RolePermsBO;
import com.example.wmsspringbootproject.model.entity.SysRoleMenu;
import com.example.wmsspringbootproject.model.entity.TransactionProduct;
import com.example.wmsspringbootproject.model.form.RoleMenuForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色菜单业务实现
 *
 * @author haoxr
 * @since 2.5.0
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    /**
     * 获取角色拥有的菜单ID集合
     *
     * @param roleId 角色ID
     * @return 菜单ID集合
     */
    @Override
    public List<Long> listMenuIdsByRoleId(Long roleId) {
        return this.baseMapper.listMenuIdsByRoleId(roleId);
    }

    @Override
    public Result<List<SysRoleMenu>> listByRoleId(Long roleId) {
        List<SysRoleMenu> sysRoleMenus=this.lambdaQuery().eq(SysRoleMenu::getRoleId,roleId).list();
        return Result.success(sysRoleMenus);
    }

    @Override
    public Result<Boolean> updateRoleMenu(RoleMenuForm roleMenuForm) {
        List<Long> menuIdsFromSql=listMenuIdsByRoleId(roleMenuForm.getRoleId());
        List<Long> menusIds=roleMenuForm.getMenuIds();
        List<Long> menusIdsNotExistInSql = menusIds.stream()
                .filter(menuId -> !menuIdsFromSql.contains(menuId))
                .toList();
        List<Long> menuIdsFromSqlNotExistInMenus = menuIdsFromSql.stream()
                .filter(menuId -> !menusIds.contains(menuId))
                .toList();
        System.out.println("menusIds中存在但menuIdsFromSql不存在的数据：" + menusIdsNotExistInSql);
        System.out.println("menusIds中不存在但menuIdsFromSql存在的数据：" + menuIdsFromSqlNotExistInMenus);
        if(menuIdsFromSqlNotExistInMenus.size()>0){
            for (Long menuId : menuIdsFromSqlNotExistInMenus) {
                System.out.println("后端存在的 menuId: " + menuId);
                this.lambdaUpdate().eq(SysRoleMenu::getMenuId,menuId)
                        .eq(SysRoleMenu::getRoleId,roleMenuForm.getRoleId())
                        .remove();
            }
        }
        if(menusIdsNotExistInSql.size()>0){
            for (Long menuId : menusIdsNotExistInSql) {
                System.out.println("前端存在的 menuId: " + menuId);
                SysRoleMenu sysRoleMenu=new SysRoleMenu();
                sysRoleMenu.setRoleId(roleMenuForm.getRoleId());
                sysRoleMenu.setMenuId(menuId);
                this.baseMapper.insert(sysRoleMenu);
            }
        }
        return Result.success();
    }

    /**
     * 获取权限角色列表
     *
     * @return 权限角色列表
     */
    @Override
    public List<RolePermsBO> getRolePermsList(String roleCode) {
        List<RolePermsBO> rolePerms= this.baseMapper.getRolePermsList(roleCode);
        return rolePerms;
    }

}
