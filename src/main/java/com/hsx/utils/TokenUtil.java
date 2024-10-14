package com.hsx.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hsx.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;

public class TokenUtil {

    public static final String ISSUER = "签发者";

    public static String generateToken(Long userId) throws Exception{
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MINUTE,800);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(instance.getTime())
                .sign(algorithm);
    }

    //    验证用户令牌
    public static Long verifyToken(String token){
        try{
            Algorithm algorithm= Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            // 解密
            DecodedJWT jwt = verifier.verify(token);
            String userId = jwt.getKeyId();
            return Long.valueOf(userId);
        }catch (TokenExpiredException e){
            throw new ConditionException("555","token过期！");
        }catch (Exception e){
            throw new ConditionException("非法用户token！");
        }
    }

    public static String generateRefreshToken(Long userId) throws Exception {
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.DAY_OF_MONTH,7);  // 7天
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(instance.getTime())
                .sign(algorithm);
    }

}
