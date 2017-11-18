package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.*;
import com.kaishengit.crm.example.*;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.exception.VerifyException;
import com.kaishengit.crm.mapper.*;
import com.kaishengit.crm.service.StaffService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator.
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private DeptStaffMapper deptStaffMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ProgressMapper progressMapper;
    @Value("${salt}")
    private String salt;
    @Value("#{'${record.progress}'.split(',')}")
    private List<String> progressList;

    private Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);
    private static final Integer COMPANY_ID = 1;

    /**
     * 登录验证
     * @param staffNum 手机号
     * @param password 密码
     * @return 验证成功返回登录对象，失败抛出VerifyException异常
     */
    @Override
    public Staff verify(String staffNum, String password) throws VerifyException {

        password = DigestUtils.md5Hex(password + salt);
        Staff staff=  staffMapper.verify(staffNum,password);
        if (staff != null) {
            logger.info("{} 在 {} 登录",staff.getUserName(),new Date());
            return staff;
        } else {
            throw new VerifyException("账号或者密码错误");
        }

    }

    /**
     * 根据条件查询员工列表
     *
     * @param params 条件集合
     * @return 员工列表
     */
    @Override
    public List<Staff> pageForStaff(Map<String, Object> params) {

        Integer deptId = (Integer) params.get("deptId");

        if (deptId == null || deptId.equals(COMPANY_ID)) {
            deptId = null;
        }
        params.put("deptId",deptId);

        List<Staff> staffList = staffMapper.findStaffByParamWithDept(params);

        return staffList;


    }

    /**
     * 根据员工部门id 计算账户数量
     * @param deptId
     * @return
     */
    @Override
    public Long countByDeptId(Integer deptId) {

        if (deptId == null || deptId.equals(COMPANY_ID)) {
            deptId = null;
        }

        return staffMapper.countByDeptId(deptId);
    }

    /**
     * 新加员工
     * @param userName
     * @param phoneNum
     * @param password
     * @param deptIds
     * @throws ServiceException 如果添加失败抛出异常ServiceException
     */
    @Override
    public void addStaff(String userName, Integer phoneNum, String password, Integer[] deptIds) throws ServiceException {

        Staff staff = new Staff();
        StaffExample staffExample = new StaffExample();
        staffExample.createCriteria().andPhoneNumEqualTo(phoneNum);
        List<Staff> staffList =  staffMapper.selectByExample(staffExample);

        if (staffList != null && !staffList.isEmpty()) {
            throw new ServiceException("该手机号已被注册");
        }

        password = DigestUtils.md5Hex(password + salt);

        staff.setUserName(userName);
        staff.setPassword(password);
        staff.setPhoneNum(phoneNum);
        staff.setCreateTime(new Date());
        staff.setUpdateTime(new Date());

        staffMapper.insertSelective(staff);
        int id = staff.getId();
        for ( int i = 0; i < deptIds.length; i++) {
            DeptStaffKey deptStaffKey = new DeptStaffKey();
            deptStaffKey.setStaffId(id);
            deptStaffKey.setDeptId(deptIds[i]);
            deptStaffMapper.insertSelective(deptStaffKey);
        }
        logger.info("{} 添加了新账号 {}", new Date(), userName);
    }

    /**
     * 根据id 删除员工信息
     *
     * @param id 员工的id
     */
    @Override
    public void deleteStaffById(Integer id) throws ServiceException {

        DeptStaffExample deptStaffExample = new DeptStaffExample();
        deptStaffExample.createCriteria().andStaffIdEqualTo(id);
        deptStaffMapper.deleteByExample(deptStaffExample);

        staffMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询当前用户所有的工作记录
     *
     * @param staff 当前员工
     * @param pageNo
     * @return 工作记录列表
     */
    @Override
    public PageInfo<Record> findMyRecord(Staff staff, Integer pageNo) {

        PageHelper.startPage(pageNo,10);
        List<Record> recordList = recordMapper.selectWithCustomerByStaffId(staff.getId());
        return new PageInfo<>(recordList);

    }

    /**
     * 根据记录的id查找对应的机会记录
     *
     * @param id 记录的id
     * @return 记录对象
     */
    @Override
    public Record findRecordWithCustomerById(Integer id) {
        return recordMapper.selectWithCustomerById(id);
    }

    /**
     * 根据当前用户id查找对应的客户列表
     *
     * @param id 用户id
     * @return 客户列表
     */
    @Override
    public List<Customer> findMyCustomer(Integer id) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andStaffIdEqualTo(id);
        return customerMapper.selectByExample(customerExample);
    }

    /**
     * 查询所有的进度
     *
     * @return 进度列表
     */
    @Override
    public List<String> findAllProgress() {
        return progressList;
    }

    /**
     * 给客户增加一条销售记录
     *
     * @param record
     */
    @Override
    @Transactional
    public void addRecord(Record record) {

        record.setCreateTime(new Date());
        recordMapper.insertSelective(record);

        Customer customer = customerMapper.selectByPrimaryKey(record.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

        if (StringUtils.isNotEmpty(record.getContent())) {

            Progress progress = new Progress();
            progress.setSaleId(record.getId());
            progress.setContent(record.getContent());
            progress.setCreateTime(new Date());

            progressMapper.insertSelective(progress);
        }

    }

    /**
     * 根据销售机会的id查询对应的跟进记录列表
     *
     * @param id 销售机会id
     * @return 跟进记录列表
     */
    @Override
    public List<Progress> findFollowRecordBySaleId(Integer id) {

        ProgressExample progressExample = new ProgressExample();
        progressExample.createCriteria().andSaleIdEqualTo(id);

        return progressMapper.selectByExample(progressExample);
    }

    /**
     * 更新销售机会的状态
     *
     * @param id
     * @param progress
     */
    @Override
    public void updateRecord(Integer id, String progress) {

        Record record = recordMapper.selectByPrimaryKey(id);
        record.setProgress(progress);
        recordMapper.updateByPrimaryKeySelective(record);

        Customer customer = customerMapper.selectByPrimaryKey(record.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

        Progress pro = new Progress();
        pro.setSaleId(id);
        pro.setCreateTime(new Date());
        pro.setContent("将进度修改为:["+ progress +"]");
        progressMapper.insert(pro);

    }

    /**
     * 新增一条跟进记录
     *
     * @param progress
     */
    @Override
    @Transactional
    public void insertProgress(Progress progress) {

        progress.setCreateTime(new Date());
        progressMapper.insertSelective(progress);

        Record record = recordMapper.selectByPrimaryKey(progress.getSaleId());
        Customer customer = customerMapper.selectByPrimaryKey(record.getCustId());
        customer.setLastContactTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);

    }

    /**
     * 根据id删除对应的销售机会
     *
     * @param id
     */
    @Override
    public void deleteRecordById(Integer id) {

        Record record = recordMapper.selectByPrimaryKey(id);
        //删除对应的跟进记录
        ProgressExample progressExample = new ProgressExample();
        progressExample.createCriteria().andSaleIdEqualTo(id);
        progressMapper.deleteByExample(progressExample);

        //删除销售机会
        recordMapper.deleteByPrimaryKey(id);

        //查询该客户是否有其他的销售机会
        RecordExample recordExample = new RecordExample();
        recordExample.createCriteria().andCustIdEqualTo(record.getCustId());
        List<Record> recordList = recordMapper.selectByExample(recordExample);

        Customer customer = customerMapper.selectByPrimaryKey(record.getCustId());
        if (recordList.isEmpty()) {
            customer.setLastContactTime(null);
        }
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 查询所有的用户
     *
     * @return
     */
    @Override
    public List<Staff> findAllStaff() {
        return staffMapper.selectByExample(new StaffExample());
    }
}
