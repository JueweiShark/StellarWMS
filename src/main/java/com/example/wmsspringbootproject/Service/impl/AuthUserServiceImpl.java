package com.example.wmsspringbootproject.Service.impl;

import com.alibaba.fastjson.JSON;
import com.example.wmsspringbootproject.Service.UserService;
import com.example.wmsspringbootproject.Utils.WmsCache.*;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.wmsspringbootproject.Service.AuthUserService;
import com.example.wmsspringbootproject.Utils.JwtTokenUtil;
import com.example.wmsspringbootproject.Utils.WmsCache;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.common.result.ResultCode;
import com.example.wmsspringbootproject.constants.Constants;
import com.example.wmsspringbootproject.converter.UserConverter;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import com.example.wmsspringbootproject.mapper.SysRoleMapper;
import com.example.wmsspringbootproject.mapper.SysUserTypeMapper;
import com.example.wmsspringbootproject.mapper.UserMapper;
import com.example.wmsspringbootproject.model.dto.LoginResult;
import com.example.wmsspringbootproject.model.entity.SysRole;
import com.example.wmsspringbootproject.model.entity.SysUserType;
import com.example.wmsspringbootproject.model.entity.Users;
import com.example.wmsspringbootproject.model.form.UserForm;
import com.example.wmsspringbootproject.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tio.websocket.common.WsResponse;

import javax.management.relation.Role;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SysUserTypeMapper userTypeMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @LogNote(description = "登录功能")
    @Override
    public Result<LoginResult> Login(UserForm userForm) {
        Users users;
        if (userForm.getEmail() != null){
            System.out.println(userForm);
            LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<Users>();
            queryWrapper.eq(Users::getEmail, userForm.getEmail());
             users = userMapper.selectOne(queryWrapper);
            if (users == null) {
                return Result.failed(ResultCode.USER_NOT_EXIST);
            }

            String email = userForm.getEmail();
            String code = userForm.getVerifyCode();
            // 权限码比对
            String rightCode = redisTemplate.opsForValue().get("EMAIL_" + email);
            System.out.println("redis上的验证码：" + rightCode);
            // 不通过
            if (!code.equals(rightCode)) {
                return Result.failed("验证码错误");
            }
            userForm = userConverter.entity2Form(users);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userForm.getName(),userForm.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SysUserDetails userDetails=(SysUserDetails)authentication.getPrincipal();

        if(WmsCache.get(Convert.toStr(userDetails.getId()))==null){
            String accessToken = jwtTokenUtil.createToken(authentication);
            WmsCache.put(accessToken,jwtTokenUtil.getUser(accessToken),Constants.TOKEN_EXPIRE);
            WmsCache.put(Convert.toStr(userDetails.getId()),accessToken,Constants.TOKEN_EXPIRE);
            List<Object> results=WmsCache.values().stream().toList();
            SysUserDetails userDetails1=(SysUserDetails) WmsCache.get(accessToken);
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

    @Override
    public Result getUserInfo(String token) {
        if (jwtTokenUtil.getUser(token)==null){
            return Result.failed("用户信息不存在,请重新登录");
        }
        if (jwtTokenUtil.getUser(token)==null){
            if (redisTemplate.opsForValue().get(token)!=null)
            return Result.success(redisTemplate.opsForValue().get(token));
        }
        System.out.println(jwtTokenUtil.getUser(token));
        LambdaQueryWrapper<SysUserType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserType::getUserId,jwtTokenUtil.getUser(token).getId());
        SysUserType userType = userTypeMapper.selectOne(queryWrapper);
        LambdaQueryWrapper<SysRole> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(SysRole::getId,userType.getRoleId());
        SysRole role = roleMapper.selectOne(queryWrapper1);
        LambdaQueryWrapper<Users> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Users::getId, jwtTokenUtil.getUser(token).getId());
        Users user = userMapper.selectOne(queryWrapper2);
        System.out.println(user);
        UserVO userVO = userConverter.entity2Vo(user);
        userVO.setRole(role.getName());

            return Result.success(userVO);
    }
}
