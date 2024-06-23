package com.example.wmsspringbootproject.Utils;

import cn.hutool.core.convert.Convert;
import com.example.wmsspringbootproject.constants.JwtClaimConstants;
import com.example.wmsspringbootproject.core.security.model.SysUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class JwtTokenUtil {

    /**
     * 用户调用登录接口，登录成功后获取到JWT的token；
     * 之后用户每次调用接口都在http的header中添加一个叫Authorization的头，值为JWT的token；
     * 后台程序通过对Authorization头中信息的解码及数字签名校验来获取其中的用户信息，从而实现认证和授权。
     * 相关方法说明：
     * <p>
     * getUserNameFromToken(String token)：从token中获取登录用户的信息
     * validateToken(String token, UserDetails userDetails)：判断token是否还有效
     */

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    //盐值
    private static String key ="a34e856a68f461f708757c32281bc750";


    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    public SysUserDetails getUser(String token){
        Claims claims=this.getClaimsFromToken(token);

        SysUserDetails userDetails=new SysUserDetails();
        userDetails.setName(Convert.toStr(claims.get(JwtClaimConstants.USERNAME)));
        userDetails.setId(Convert.toInt(claims.get(JwtClaimConstants.USER_ID)));
        userDetails.setDataScope(Convert.toInt(claims.get(JwtClaimConstants.DATASCOPE)));
        userDetails.setWarehouseId(Convert.toStr(claims.get(JwtClaimConstants.WAREHOUSE_ID)));
        return userDetails;
    }

    public Authentication getAuthentication(String token){
        Claims claims=this.getClaimsFromToken(token);

        SysUserDetails userDetails=getUser(token);

        Set<SimpleGrantedAuthority> authorities=Convert.toList(claims.get(JwtClaimConstants.AUTHORITIES))
                .stream().map(item->
                        new SimpleGrantedAuthority(item.toString()))
                .collect(Collectors.toSet());
        return new UsernamePasswordAuthenticationToken(userDetails,"",authorities);
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000L);
    }


    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     */
    public boolean validateToken(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        if(expiredDate==null){
            return false;
        }
        return new Date().before(expiredDate);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if(claims==null){
            return null;
        }
        return claims.getExpiration();
    }


    public String createToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        SysUserDetails userDetails = (SysUserDetails) authentication.getPrincipal();
        claims.put(JwtClaimConstants.USER_ID, userDetails.getId()); // 用户ID
        claims.put(JwtClaimConstants.USERNAME, claims.getSubject()); // 用户名
        claims.put(JwtClaimConstants.DATASCOPE,userDetails.getDataScope());//数据权限
        claims.put(JwtClaimConstants.WAREHOUSE_ID,userDetails.getWarehouseId());

        // claims 中添加角色信息
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        claims.put(JwtClaimConstants.AUTHORITIES, roles);

        Date now = new Date();
        Date expirationTime = generateExpirationDate();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256,key).compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(HttpHeaders.AUTHORIZATION);
        bearerToken="wms "+bearerToken;
        if (bearerToken != null&&bearerToken.startsWith("wms ")) {
            return bearerToken.substring(4);
        }
        return null;
    }

    public String getUsername(String tokenWithoutPrefix) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(tokenWithoutPrefix).getBody().getSubject();
    }
}

