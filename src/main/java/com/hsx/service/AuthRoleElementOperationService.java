package com.hsx.service;

import com.hsx.auth.AuthRoleElementOperation;

import java.util.List;
import java.util.Set;

public interface AuthRoleElementOperationService {
    List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(Set<Long> roleIdSet);
}
