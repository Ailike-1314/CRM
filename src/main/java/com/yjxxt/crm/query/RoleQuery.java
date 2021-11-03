package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

public class RoleQuery extends BaseQuery {
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public RoleQuery() {
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
