package com.hsx.service;

import com.hsx.auth.AuthRole;
import com.hsx.auth.AuthRoleElementOperation;
import com.hsx.auth.AuthRoleMenu;

import java.util.List;
import java.util.Set;

public interface AuthRoleService {
    List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet);

    List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> roleIdSet);

    AuthRole getRoleByCode(String roleLv0);
}
