package fun.qxfly.common.utils;

import fun.qxfly.common.enums.ExceptionEnum;
import fun.qxfly.common.exception.excep.JwtException;
import fun.qxfly.common.exception.excep.UserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;

@Slf4j
public class JwtUtils {
    private static final String SignKey = System.getProperty("JwtSignKey");
    private static final String accessTokenTimeout = System.getProperty("AccessTokenTimeout");
    private static final String refreshTokenTimeout = System.getProperty("RefreshTokenTimeout");

    /**
     * 创建访问和刷新令牌
     *
     * @param uid      用户id
     * @param username 用户名
     * @param role     角色
     * @param timeout  过期时间
     * @return
     */
    public static HashMap<String, String> createAccessAndRefreshToken(int uid, String username, String role, Long timeout) {
        HashMap<String, String> map = new HashMap<>();
        String accessToken = createAccessToken(uid, username, role, timeout);
        String refreshToken = createRefreshToken(uid, username, role, timeout);
        map.put("token", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }

    /**
     * 生成访问token
     *
     * @param uid      uid
     * @param username 用户名
     * @param timeout  过期时间
     * @return token
     */
    public static String createAccessToken(int uid, String username, String role, Long timeout) {
        if (timeout == null) {
            timeout = Long.parseLong(accessTokenTimeout);
        }
        return Jwts.builder()
                .claim("uid", uid)
                .claim("username", username)
                .claim("role", role)
                .claim("type", "accessToken")
                .setIssuedAt(new Date()) //设置jwt生成时间
                .signWith(SignatureAlgorithm.HS256, SignKey)
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .compact();
    }

    /**
     * 生成刷新token
     *
     * @param uid      uid
     * @param username 用户名
     * @param timeout  过期时间
     * @return token
     */
    public static String createRefreshToken(int uid, String username, String role, Long timeout) {
        if (timeout == null) {
            timeout = Long.parseLong(refreshTokenTimeout);
        }
        return Jwts.builder()
                .claim("uid", uid)
                .claim("username", username)
                .claim("role", role)
                .claim("type", "refreshToken")
                .setIssuedAt(new Date()) //设置jwt生成时间
                .signWith(SignatureAlgorithm.HS256, SignKey)
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .compact();
    }

    /**
     * 更新token
     *
     * @param refreshToken 老token
     * @return 新的token
     */
    public static HashMap<String, String> refreshToken(String refreshToken) {
        if (!getTokenType(refreshToken).equals("refreshToken"))
            throw new UserException(ExceptionEnum.USER_LOGIN_EXPIRED);
        String accessToken = createAccessToken(getUid(refreshToken), getUsername(refreshToken), getRole(refreshToken), null);
        String refreshToken1 = createRefreshToken(getUid(refreshToken), getUsername(refreshToken), getRole(refreshToken), null);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken1);
        return tokenMap;
    }


    /**
     * 解析token
     *
     * @param token token
     * @return Claims
     */
    public static Claims parseJWT(String token) {
        return parseJWT(token, null);
    }

    private static Claims parseJWT(String token, String type) {
        if (token == null || token.isEmpty() || token.isBlank()) {
            throw new RuntimeException();
        }
        try {
            return Jwts.parser()
                    .setSigningKey(SignKey)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            if (type != null && type.equals("refreshToken")) {
                throw new UserException(ExceptionEnum.USER_LOGIN_EXPIRED);
            }
            throw new JwtException(ExceptionEnum.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new JwtException(ExceptionEnum.TOKEN_ERROR);
        }
    }

    /**
     * 获取token中的uid
     *
     * @param token token
     * @return uid
     */
    public static Integer getUid(String token) {
        return parseJWT(token).get("uid", Integer.class);
    }

    /**
     * 获取token中的用户名
     *
     * @param token token
     * @return 用户名
     */
    public static String getUsername(String token) {
        return parseJWT(token).get("username", String.class);
    }

    /**
     * 获取token中的用户角色
     *
     * @param token token
     * @return 用户名
     */
    public static String getRole(String token) {
        return parseJWT(token).get("role", String.class);
    }

    /**
     * 获取token类型
     *
     * @param token token
     * @return token类型
     */
    public static String getTokenType(String token) {
        return parseJWT(token).get("type", String.class);
    }
}
