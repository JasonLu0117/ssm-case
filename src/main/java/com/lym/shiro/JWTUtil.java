package com.lym.shiro;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtil {

    // 过期时间为：5min * 60s = 300s，该修改到配置文件中
    private static String accessTokenExpireTime = "300";

    // JWT认证加密私钥(Base64加密)，该修改到配置文件中
    private static String encryptJWTKey = "secret";

//    @Value("${accessTokenExpireTime}")
//    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
//        JwtUtil.accessTokenExpireTime = accessTokenExpireTime;
//    }

//    @Value("${encryptJWTKey}")
//    public void setEncryptJWTKey(String encryptJWTKey) {
//        JwtUtil.encryptJWTKey = encryptJWTKey;
//    }

    /**
     * 校验token是否正确
     * @param token
     * @return boolean 是否正确
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            String secret = getClaim(token, "username") + decode(encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("JWTToken认证解密异常");
        }
    }

    /**
     * 获取Token中的信息，无需secret解密也能获取
     * @param token
     * @param claim
     * @return java.lang.String
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解密Token中的信息出现JWTDecodeException异常");
        }
    }

    /**
     * 生成签名Token
     * @param username 帐号
     * @param password 密码
     * @param roleId 角色id
     * @param currentTimeMillis 时间戳
     * @return java.lang.String 返回加密的Token
     */
    public static String sign(String username, String password, String roleId, String currentTimeMillis) {
        try {
            // 帐号加JWT私钥加密
            String secret = username + decode(encryptJWTKey);
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpireTime) * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带account帐号信息
            return JWT.create()
                    .withClaim("username", username)
                    .withClaim("secret", password)
                    .withClaim("roleId", roleId)
                    .withClaim("currentTimeMillis", currentTimeMillis)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new RuntimeException("JWTToken加密出现UnsupportedEncodingException异常");
        }
    }
    
    // base64加密
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    // base64解密
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }
    
}
