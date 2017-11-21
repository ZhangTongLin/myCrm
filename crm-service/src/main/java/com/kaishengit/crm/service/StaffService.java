package com.kaishengit.crm.service;


import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Progress;
import com.kaishengit.crm.entity.Record;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.exception.VerifyException;

import java.util.List;
import java.util.Map;

/**
 * 员工的业务层
 * @author Administrator.
 */
public interface StaffService {
    /**
     * 登录验证
     * @param staffNum 手机号
     * @param password 密码
     * @return 验证成功返回登录对象，失败抛出异常
     */
    Staff verify(String staffNum, String password) throws VerifyException;

    /**
     * 根据条件查询员工列表
     * @param params 条件集合
     * @return 员工列表
     */
    List<Staff> pageForStaff(Map<String, Object> params);

    /**
     * 根据员工部门id 计算账户数量
     * @param deptId
     * @return
     */
    Long countByDeptId(Integer deptId);

    /**
     * 新加员工
     * @param userName
     * @param phoneNum
     * @param password
     * @param deptIds
     * @throws ServiceException 如果添加失败抛出异常ServiceException
     */
    void addStaff(String userName, String phoneNum, String password, Integer[] deptIds) throws ServiceException;

    /**
     * 根据id 删除员工信息
     * @param id 员工的id
     */
    void deleteStaffById(Integer id) throws ServiceException;

    /**
     * 查询当前用户所有的工作记录
     * @param staff 当前员工
     * @param pageNo
     * @return 工作记录列表
     */
    PageInfo<Record> findMyRecord(Staff staff, Integer pageNo);

    /**
     * 根据记录的id查找对应的机会记录
     * @param id 记录的id
     * @return 记录对象
     */
    Record findRecordWithCustomerById(Integer id);

    /**
     * 根据当前用户id查找对应的客户列表
     * @param id 用户id
     * @return 客户列表
     */
    List<Customer> findMyCustomer(Integer id);

    /**
     * 查询所有的进度
     * @return 进度列表
     */
    List<String> findAllProgress();

    /**
     * 给客户增加一条销售记录
     * @param record
     */
    void addRecord(Record record);

    /**
     * 根据销售机会的id查询对应的跟进记录列表
     * @param id 销售机会id
     * @return 跟进记录列表
     */
    List<Progress> findFollowRecordBySaleId(Integer id);

    /**
     * 更新销售机会的状态
     * @param id
     * @param progress
     */
    void updateRecord(Integer id, String progress);

    /**
     * 新增一条跟进记录
     * @param progress
     */
    void insertProgress(Progress progress);

    /**
     * 根据id删除对应的销售机会
     * @param id
     */
    void deleteRecordById(Integer id);

    /**
     * 查询所有的用户
     * @return
     */
    List<Staff> findAllStaff();

    /**
     *获取客户的销售统计
     * @return 客户的销售的统计集合
     */
    List<Map<String,Object>> customerChart();
}
