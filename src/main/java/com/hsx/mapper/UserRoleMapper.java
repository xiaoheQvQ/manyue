package com.hsx.mapper;

import com.hsx.auth.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    List<UserRole> getUserRoleByUserId(Long userId);

    void addUserRole(UserRole userRole);
}
