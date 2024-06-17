package com.example.wmsspringbootproject.Service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.wmsspringbootproject.Service.AuthUserService;
import com.example.wmsspringbootproject.Utils.JwtTokenUtil;
import com.example.wmsspringbootproject.Utils.WmsCache;
import com.example.wmsspringbootproject.common.Annotation.LogNote;
import com.example.wmsspringbootproject.common.result.Result;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tio.websocket.common.WsResponse;

import javax.management.relation.Role;


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
    private UserMapper userMapper;
    @Autowired
    private UserConverter userConverter;
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

    @Override
    public Result getUserInfo(String token) {
        if (jwtTokenUtil.getUser(token)==null){
            return Result.failed("用户信息不存在,请重新登录");
        }
        System.out.println(jwtTokenUtil.getUser(token));
//        LambdaQueryWrapper<SysUserType> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SysUserType::getUserId,jwtTokenUtil.getUser(token).getId());
//        SysUserType userType = userTypeMapper.selectOne(queryWrapper);
//        LambdaQueryWrapper<SysRole> queryWrapper1 = new LambdaQueryWrapper<>();
//        queryWrapper1.eq(SysRole::getId,userType.getRoleId());
//        SysRole role = roleMapper.selectOne(queryWrapper1);
//        UserVO user = new UserVO();
//        user.setId(jwtTokenUtil.getUser(token).getId());
//        user.setName(jwtTokenUtil.getUser(token).getName());
//        user.setAvatar(jwtTokenUtil.getUser(token).getAvatar());
////        user.setEmail(jwtTokenUtil.getUser(token).getEmail());
////        user.setPhone(jwtTokenUtil.getUser(token).getPhone());
////        user.setWeChatName(jwtTokenUtil.getUser(token).getWeChatName());
////        user.setLastLogin(jwtTokenUtil.getUser(token).getLastLogin());
//        user.setDataScope(jwtTokenUtil.getUser(token).getDataScope());
//        user.setTypeId(userType.getRoleId());
//        user.setRole(role.getName());

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
