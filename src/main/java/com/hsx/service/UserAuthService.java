package com.hsx.service;

import com.hsx.auth.UserAuthorities;

public interface UserAuthService {
    UserAuthorities getUserAuthorities(Long userId);

    void addUserDefaultRole(Long id);
}
