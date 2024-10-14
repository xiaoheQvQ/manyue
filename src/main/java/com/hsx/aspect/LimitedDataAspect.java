package com.hsx.aspect;

import com.hsx.anno.LimitedRole;
import com.hsx.auth.UserRole;
import com.hsx.constant.AuthRoleConstant;
import com.hsx.exception.ConditionException;
import com.hsx.pojo.User;
import com.hsx.pojo.UserMoment;
import com.hsx.service.UserRoleService;
import com.hsx.utils.UserUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1) // 设置优先级
@Aspect
@Component
class LimitedDataAspect {

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.hsx.anno.LimitedData)")
    public void check(){}

    @Before("check()")
    public void before(JoinPoint joinPoint){
        Long userId = UserUtils.getCurrentUserId();
        //获取自己的等级
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        Set <String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args){
            if (arg instanceof UserMoment){
                UserMoment userMoment = (UserMoment) arg;
                String type = userMoment.getType();
                if (roleCodeSet.contains(AuthRoleConstant.ROLE_LV0) && !"0".equals(type)){
                    throw new ConditionException("参数异常");
                }
            }
        }

    }


}
