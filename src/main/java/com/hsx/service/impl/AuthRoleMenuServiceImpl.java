package com.hsx.service.impl;

import com.hsx.auth.AuthRoleMenu;
import com.hsx.mapper.AuthRoleMenuMapper;
import com.hsx.service.AuthRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleMenuServiceImpl implements AuthRoleMenuService {

    @Autowired
    private AuthRoleMenuMapper authRoleMenuMapper;

    @Override
    public List<AuthRoleMenu> getRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuMapper.getRoleMenusByRoleIds(roleIdSet);
    }
}
