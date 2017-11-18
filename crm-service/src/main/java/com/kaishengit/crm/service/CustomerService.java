package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Record;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.exception.ServiceException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 客户的业务层
 * @author Administrator.
 */
public interface CustomerService {
    /**
     * 查看当前账号的客户,并进行分页
     *
     * @param pageNo 页码
     * @param id 当前账号的id
     * @return
     */
    PageInfo<Customer> findMyCustomer(Integer pageNo, Integer id);

    /**
     * 新添加客户
     * @param customer
     * @throws ServiceException 如果已存在就抛出ServiceException
     */
    void addCustomer(Customer customer) throws ServiceException;

    /**
     * 强制添加客户
     * @param customer
     */
    void addCustomerCompel(Customer customer);

    /**
     * 查找所有的客户来源
     * @return
     */
    List<String> findAllSource();

    /**
     * 查找所有的客户行业
     * @return
     */
    List<String> findAllTrade();

    /**
     * 根据id查找对应的客户
     * @param id
     * @return
     */
    Customer findCustomerById(Integer id);

    /**
     * 将客户导出csv格式的文件
     * @param staff
     * @param outputStream
     */
    void exportCsvFileToOutputStream(Staff staff, OutputStream outputStream) throws IOException;

    /**
     * 将客户导出为xls文件
     * @param staff
     * @param outputStream
     */
    void exportXlsFileToOutputStream(Staff staff, OutputStream outputStream) throws IOException;

    /**
     * 查询该客户所有的销售机会
     * @param id
     * @return
     */
    List<Record> findAllRecord(Integer id);

    /**
     * 根据id删除对应的客户
     * @param id
     * @throws ServiceException 删除不成功抛出异常
     */
    void deleteCustomerById(Integer id) throws ServiceException;

    /**
     * 更新客户信息
     * @param customer
     */
    void editCustomer(Customer customer);

}
