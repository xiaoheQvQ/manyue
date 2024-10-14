package com.hsx.auth;

import lombok.Data;

import java.util.List;

@Data
public class UserAuthorities {

    /**
     * 页面元素权限
     * */
    List<AuthRoleElementOperation> roleElementOperationList;

    /**
     * 菜单权限
     * */
    List<AuthRoleMenu> roleMenuList;

}
