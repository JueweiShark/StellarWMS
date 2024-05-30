package com.example.wmsspringbootproject.core.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.model.entity.Users;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SysUserDetails implements UserDetails {
    private int id;
    private String name;
    private String password;

    private Boolean enabled;

    private Collection<SimpleGrantedAuthority> authorities;

    public SysUserDetails(Users authorInfo){
        this.id=authorInfo.getId();
        Set<String> roles=new HashSet<String>();
        roles.add(Constants.RolesValue.ROLES[authorInfo.getType()]);

        Set<SimpleGrantedAuthority> authoritySet;
        if(CollectionUtil.isNotEmpty(roles)){
            authoritySet=roles
                    .stream()
                    .map(role->new SimpleGrantedAuthority("ROLE_"+role))
                    .collect(Collectors.toSet());
        }else{
            authoritySet= Collections.EMPTY_SET;
        }
        this.authorities=authoritySet;
        this.name=authorInfo.getName();
        this.password=authorInfo.getPassword();
        this.enabled= ObjectUtil.equal(authorInfo.getDeleted(),1);
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
