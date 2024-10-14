package com.hsx.utils;

import com.hsx.exception.ConditionException;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 用户工具类
 * */
public class UserUtils {


    /** 通过token 获取userId */
    public static Long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if(userId<0){
            throw new ConditionException("非法用户！");
        }
        return userId;
    }




}
