package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

/*
* 管理多条件查询接受类
* */
public class SaleChanceQuery extends BaseQuery {
    private String customerName;//客户名
    private String createMan;//创建人
    private String state;//分配状态

    public SaleChanceQuery() {
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
