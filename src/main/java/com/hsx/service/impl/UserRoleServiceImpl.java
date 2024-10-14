package com.hsx.service.impl;

import com.hsx.auth.UserRole;
import com.hsx.mapper.UserRoleMapper;
import com.hsx.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> getUserRoleByUserId(Long userId) {

        return userRoleMapper.getUserRoleByUserId(userId);

    }

    @Override
    public void addUserRole(UserRole userRole) {
        userRoleMapper.addUserRole(userRole);
    }
}
