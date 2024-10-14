package com.hsx.service.impl;

import com.hsx.auth.AuthRole;
import com.hsx.auth.AuthRoleElementOperation;
import com.hsx.auth.AuthRoleMenu;
import com.hsx.mapper.AuthRoleMapper;
import com.hsx.service.AuthRoleElementOperationService;
import com.hsx.service.AuthRoleMenuService;
import com.hsx.service.AuthRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleServiceImpl implements AuthRoleService {

    @Autowired
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Autowired
    private AuthRoleMenuService authRoleMenuService;

    @Autowired
    private AuthRoleMapper authRoleMapper;

    @Override
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return  authRoleElementOperationService.getRoleElementOperationsByRoleIds(roleIdSet);
    }

    @Override
    public List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> roleIdSet) {

        return authRoleMenuService.getRoleMenusByRoleIds(roleIdSet);
    }

    @Override
    public AuthRole getRoleByCode(String code) {
        return authRoleMapper.getRoleByCode(code);
    }
}
