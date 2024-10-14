package com.hsx.aspect;

import com.hsx.anno.LimitedRole;
import com.hsx.auth.UserRole;
import com.hsx.exception.ConditionException;
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
public class LimitedRoleAspect {

    @Autowired
    private UserRoleService userRoleService;

    @Pointcut("@annotation(com.hsx.anno.LimitedRole)")
    public void check(){}

    @Before("check() && @annotation(limitedRole)")
    public void before(JoinPoint joinPoint, LimitedRole limitedRole){
        Long userId = UserUtils.getCurrentUserId();
        //获取自己的等级
        List<UserRole> userRoleList = userRoleService.getUserRoleByUserId(userId);
        System.out.println(userRoleList.size());
        String [] limitedRoleCodeList = limitedRole.limitedRoleCodeList();
        Set <String> limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set <String> roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        roleCodeSet.retainAll(limitedRoleCodeSet); //roleCodeSet里面的就是交集
        if (roleCodeSet.size()>0){
            throw new ConditionException("权限不足");
        }

    }


}
