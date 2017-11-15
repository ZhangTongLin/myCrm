package com.kaishengit.crm.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class DeptStaffKey implements Serializable {
    private Integer deptId;

    private Integer staffId;

    private static final long serialVersionUID = 1L;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }
}