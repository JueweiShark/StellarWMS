package com.example.wmsspringbootproject.Utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {

    /**
     * 获取当前登录人信息
     *
     * @return
     */
    public static SysUserDetails getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof SysUserDetails) {
                return (SysUserDetails) authentication.getPrincipal();
            }
        }
        return null;
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static Long getUserId() {
        Long userId = Convert.toLong(getUser().getId());
        return userId;
    }

    /**
     * 获取用户仓库ID
     *
     * @return
     */
    public static String getWarehouseId() {
            String warehouseId = Objects.requireNonNull(getUser()).getWarehouseId();
        return warehouseId;
    }

    /**
     * 获取数据权限范围
     *
     * @return DataScope
     */
    public static Integer getDataScope() {
        Integer dataScope = Convert.toInt(getUser().getDataScope());
        return dataScope;
    }


    /**
     * 获取用户角色集合
     *
     * @return
     */
    public static Set<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (CollectionUtil.isNotEmpty(authorities)) {
                Set<String> roles = authorities.stream().filter(item -> item.getAuthority().startsWith("role_"))
                        .map(item -> StrUtil.removePrefix(item.getAuthority(), "role_"))
                        .collect(Collectors.toSet());
                return roles;
            }
        }
        return Collections.EMPTY_SET;
    }

    /**
     * 获取用户权限集合
     *
     * @return
     */
    public static Set<String> getPerms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (CollectionUtil.isNotEmpty(authorities)) {
                Set<String> perms = authorities.stream().filter(item -> !item.getAuthority().startsWith("role_"))
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toSet());
                return perms;
            }
        }
        return Collections.EMPTY_SET;
    }

    /**
     * 是否超级管理员
     * <p>
     * 超级管理员忽视任何权限判断
     *
     * @return
     */
    public static boolean isRoot() {
        Set<String> roles = getRoles();
        if (roles.contains(Constants.RoleType.ROOT.getType())) {
            return true;
        }
        return false;
    }

    public static boolean isAdmin(){
        Set<String> roles = getRoles();
        if (roles.contains(Constants.RoleType.ADMIN.getType())) {
            return true;
        }
        return false;
    }


    /**
     * 是否拥有权限判断
     * <p>
     * 适用业务判断(接口权限判断适用Spring Security 自带注解 PreAuthorize 判断即可 )
     *
     * @return
     */
    public static boolean hasPerm(String perm) {

        if (isRoot()) {
            return true;
        }

        Set<String> perms = getPerms();

        boolean hasPerm = perms.stream().anyMatch(item -> PatternMatchUtils.simpleMatch(perm, item));
        return hasPerm;
    }

    public static Result checkResponse(Result result){
        ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes!=null){
            HttpServletResponse response=requestAttributes.getResponse();
            if(response!=null){
                int status=response.getStatus();
                if(status==HttpServletResponse.SC_UNAUTHORIZED){
                    return Result.failed("没有数据查看权限");
                }
            }
        }
        return result;
    }

}
