package com.example.wmsspringbootproject.core.security.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.model.entity.Users;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SysUserDetails implements UserDetails {

    private int id;

    private String name;

    private String password;

    private Boolean enabled;

    private Integer dataScope;

    private Collection<SimpleGrantedAuthority> authorities;

    private String warehouseId;

    private String avatar;

    public SysUserDetails(Users authorInfo){
        this.id=authorInfo.getId();
        Set<String> roles=authorInfo.getRoles();

        Set<SimpleGrantedAuthority> authoritySet;

        if(CollectionUtil.isNotEmpty(roles)){
            authoritySet=roles
                    .stream()
                    .map(role->new SimpleGrantedAuthority("role_"+role))
                    .collect(Collectors.toSet());
        }else{
            authoritySet=Collections.emptySet();
        }
        this.authorities=authoritySet;
        this.name=authorInfo.getName();
        this.password=authorInfo.getPassword();
        this.dataScope=authorInfo.getDataScope();
        this.warehouseId=authorInfo.getWarehouseId();
        this.enabled= ObjectUtil.equal(authorInfo.getDeleted(),1);
        this.avatar=authorInfo.getAvatar();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
