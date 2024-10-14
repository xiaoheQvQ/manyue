package com.hsx.service;

import com.hsx.auth.AuthRoleMenu;

import java.util.List;
import java.util.Set;

public interface AuthRoleMenuService {
    List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> roleIdSet);
}
