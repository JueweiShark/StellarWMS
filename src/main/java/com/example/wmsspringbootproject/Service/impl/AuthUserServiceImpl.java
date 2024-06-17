package com.example.wmsspringbootproject.Service.impl;

import cn.hutool.core.convert.Convert;
import com.example.wmsspringbootproject.Service.AuthUserService;
import com.example.wmsspringbootproject.Utils.JwtTokenUtil;
import com.example.wmsspringbootproject.Utils.WmsCache;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.form.UserForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tio.websocket.common.WsResponse;


@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    @LogNote(description = "登录功能")
    @Override
    public Result<LoginResult> Login(UserForm userForm) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userForm.getName(),userForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SysUserDetails userDetails=(SysUserDetails)authentication.getPrincipal();

        if(WmsCache.get(Convert.toStr(userDetails.getId()))==null){
            String accessToken = jwtTokenUtil.createToken(authentication);
            WmsCache.put(accessToken,jwtTokenUtil.getUser(accessToken),Constants.TOKEN_EXPIRE);
            WmsCache.put(Convert.toStr(userDetails.getId()),accessToken,Constants.TOKEN_EXPIRE);
            return Result.success(LoginResult.builder()
                    .tokenType("wms")
                    .accessToken(accessToken)
                    .expires(Long.parseLong(String.valueOf(jwtTokenUtil.getExpiredDateFromToken(accessToken).getTime())))
                    .build());
        }else{
            String token=(String) WmsCache.get(Convert.toStr(userDetails.getId()));
            return Result.success(LoginResult.builder()
                    .tokenType("wms")
                    .accessToken(token)
                    .expires(Long.parseLong(String.valueOf(jwtTokenUtil.getExpiredDateFromToken(token).getTime())))
                    .build());
        }
    }
}
