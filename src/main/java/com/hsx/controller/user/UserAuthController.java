package com.hsx.controller.user;

import com.hsx.auth.UserAuthorities;
import com.hsx.pojo.JsonResponse;
import com.hsx.service.UserAuthService;

import com.hsx.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthController {


    @Autowired
    private UserAuthService userAuthService;

    //获取用户权限信息
    @GetMapping("/user-authorities")
    public JsonResponse<UserAuthorities> getUserAuthorities(){
        Long userId = UserUtils.getCurrentUserId();
        UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
