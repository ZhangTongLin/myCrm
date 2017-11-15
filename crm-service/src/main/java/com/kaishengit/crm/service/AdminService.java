package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Admin;
import com.kaishengit.crm.entity.Department;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.exception.VerifyException;

import java.util.List;

/**
 *
 * @author Administrator.
 */
public interface AdminService {

    /**
     * 查询所有部门列表
     * @return
     */
    List<Department> findAllDept();

    /**
     * 新增部门
     * @param deptName 部门名称
     * @throws ServiceException 如果失败了则抛出异常
     */
    void saveDept(String deptName) throws ServiceException;
}
