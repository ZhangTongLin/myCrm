package com.kaishengit.crm.service.impl;


import com.kaishengit.crm.entity.Department;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.example.DepartmentExample;
import com.kaishengit.crm.example.StaffExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.AdminMapper;
import com.kaishengit.crm.mapper.DepartmentMapper;
import com.kaishengit.crm.mapper.StaffMapper;
import com.kaishengit.crm.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    /**
     * 部门表中的公司的ID
     */
    private static final Integer PARENT_ID = 1;

    /**
     * 查询所有部门列表
     * @return 部门列表
     */
    @Override
    public List<Department> findAllDept() {
        return departmentMapper.selectByExample(new DepartmentExample());
    }

    /**
     * 新增部门
     *
     * @param deptName 部门名称
     * @throws ServiceException 如果失败了则抛出异常
     */
    @Override
    public void saveDept(String deptName) throws ServiceException {

        DepartmentExample departmentExample = new DepartmentExample();
        departmentExample.createCriteria().andDeptNameEqualTo(deptName);
        List<Department> departmentList =  departmentMapper.selectByExample(departmentExample);

        Department department = null;
        if (departmentList != null && !departmentList.isEmpty()) {
            department = departmentList.get(0);
        }

        if (department != null) {
            throw new ServiceException("该部门已存在");
        }

        department = new Department();
        department.setDeptName(deptName);
        department.setpId(PARENT_ID);

        departmentMapper.insertSelective(department);

        logger.info("{} 添加了新的部门 {}",new Date(),deptName);
    }

}
