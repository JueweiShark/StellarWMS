package com.example.wmsspringbootproject.core.security.service;

import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.converter.UserConverter;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import com.example.wmsspringbootproject.model.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserDetailsService implements UserDetailsService {
    private final UserConverter userConverter;
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users=userService.getUserAuthInfo(username);
        if(users==null){
            throw new UsernameNotFoundException(username);
        }
        return new SysUserDetails(users);
    }
}
