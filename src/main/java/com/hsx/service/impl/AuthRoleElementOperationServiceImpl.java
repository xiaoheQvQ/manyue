package com.hsx.service.impl;

import com.hsx.auth.AuthRoleElementOperation;
import com.hsx.mapper.AuthRoleElementOperationMapper;
import com.hsx.service.AuthRoleElementOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthRoleElementOperationServiceImpl implements AuthRoleElementOperationService {


    @Autowired
    private AuthRoleElementOperationMapper authRoleElementOperationMapper;

    @Override
    public List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet) {
        return authRoleElementOperationMapper.getRoleElementOperationsByRoleIds(roleIdSet);
    }
}
