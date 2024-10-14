package com.hsx.service;

import com.hsx.auth.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> getUserRoleByUserId(Long userId);

    void addUserRole(UserRole userRole);
}
