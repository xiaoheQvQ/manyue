package com.hsx.mapper;

import com.hsx.auth.AuthRole;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthRoleMapper {
    AuthRole getRoleByCode(String code);
}
