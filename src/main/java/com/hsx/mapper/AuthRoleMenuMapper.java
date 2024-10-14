package com.hsx.mapper;

import com.hsx.auth.AuthRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleMenuMapper {
    List<AuthRoleMenu> getRoleMenusByRoleIds(@Param("roleIdSet") Set<Long> roleIdSet);
}
