package com.example.wmsspringbootproject.Service.impl;

import com.example.wmsspringbootproject.Service.AuthUserService;
import com.example.wmsspringbootproject.Utils.JwtTokenUtil;
import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    public Result Login(UserForm userForm) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userForm.getName(),userForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String accessToken = jwtTokenUtil.createToken(authentication);
        LoginResult.builder()
                .tokenType("wms")
                .accessToken(accessToken)
                .build();
        System.out.println(Result.success(accessToken).getMsg());
        return Result.success(accessToken);
    }
}
