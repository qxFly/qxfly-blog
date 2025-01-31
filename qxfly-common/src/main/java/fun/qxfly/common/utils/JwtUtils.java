package fun.qxfly.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtils {
    private static final String SignKey = System.getProperty("JwtSignKey");
    private static final String defaultTimeout = System.getProperty("JwtTimeout");

    /**
     * 生成token
     *
     * @param uid      uid
     * @param username 用户名
     * @param timeout  过期时间
     * @return token
     */
    public static String createToken(int uid, String username, Long timeout) {
        if (timeout == null) {
            timeout = Long.parseLong(defaultTimeout);
        }
        return Jwts.builder()
                .claim("uid", uid)
                .claim("username", username)
                .setIssuedAt(new Date()) //设置jwt生成时间
                .signWith(SignatureAlgorithm.HS256, SignKey)
                .setExpiration(new Date(System.currentTimeMillis() + timeout))
                .compact();
    }

    /**
     * 更新token
     * @param token 老token
     * @return 新的token
     */
    public static String updateToken(String token) {
        Claims claims = parseJWT(token);
        if (claims == null) return null;
        return createToken(claims.get("uid", Integer.class), claims.get("username", String.class), null);
    }

    /**
     * 解析token
     *
     * @param token token
     * @return Claims
     */
    public static Claims parseJWT(String token) {
        if (token == null || token.isEmpty() || token.isBlank()) {
            return null;
        }
        try {
            return Jwts.parser()
                    .setSigningKey(SignKey)
                    .parseClaimsJws(token)
                    .getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
