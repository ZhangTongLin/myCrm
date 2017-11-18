package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Record;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.example.CustomerExample;
import com.kaishengit.crm.example.RecordExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.mapper.ProgressMapper;
import com.kaishengit.crm.mapper.RecordMapper;
import com.kaishengit.crm.service.CustomerService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * 客户的业务层
 * @author Administrator.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ProgressMapper progressMapper;

    //springEL表达式
    @Value("#{'${customer.trade}'.split(',')}")
    private List<String> customerTrades;

    @Value("#{'${customer.source}'.split(',')}")
    private List<String> customerSources;

    private Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    /**
     * 查看当前账号的客户
     *
     *
     * @param pageNo 页码
     * @param id 当前账号的id
     * @return 顾客的分页
     */
    @Override
    public PageInfo<Customer> findMyCustomer(Integer pageNo, Integer id) {

        PageHelper.startPage(pageNo,5);
        List<Customer> customerList = findAllCustomerByStaffId(id);

        return new PageInfo<>(customerList);

    }

    /**
     * 新添加客户
     *
     * @param customer 顾客对象
     * @throws ServiceException 如果已存在就抛出ServiceException
     */
    @Override
    public void addCustomer(Customer customer) throws ServiceException {

        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria()
                .andCustNameEqualTo(customer.getCustName())
                .andPhoneNumEqualTo(customer.getPhoneNum()).andSexEqualTo(customer.getSex());

        List<Customer> customerList = customerMapper.selectByExample(customerExample);

        if (customerList != null && !customerList.isEmpty()) {
            throw new ServiceException("该客户已存在");
        }

        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        customer.setLastContactTime(new Date());

        customerMapper.insertSelective(customer);
        logger.info("在 {} 时候 添加了新客户 {}",new Date(), customer.getCustName());
    }

    /**
     * 强制添加客户
     *
     * @param customer 客户对象
     */
    @Override
    public void addCustomerCompel(Customer customer) {

        customer.setCreateTime(new Date());
        customer.setUpdateTime(new Date());
        customer.setLastContactTime(new Date());

        customerMapper.insertSelective(customer);
        logger.info("在 {} 时候 强制添加了新客户 {}",new Date(), customer.getCustName());
    }

    /**
     * 查找所有的客户来源
     *
     * @return 客户来源列表
     */
    @Override
    public List<String> findAllSource() {
        return customerSources;
    }

    /**
     * 查找所有的客户行业
     *
     * @return 客户行业列表
     */
    @Override
    public List<String> findAllTrade() {
        return customerTrades;
    }

    /**
     * 根据id查找对应的客户
     *
     * @param id 客户id
     * @return 客户对象
     */
    @Override
    public Customer findCustomerById(Integer id) {
        return customerMapper.selectByPrimaryKey(id);
    }

    /**
     * 导出csv格式的文件
     *
     * @param staff 员工对象
     * @param outputStream 输出流
     */
    @Override
    public void exportCsvFileToOutputStream(Staff staff, OutputStream outputStream) throws IOException {

        List<Customer> customerList = findAllCustomerByStaffId(staff.getId());

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("顾客姓名")
                .append(",")
                .append("手机号")
                .append(",")
                .append("职位")
                .append(",")
                .append("来源");

        for (Customer customer : customerList) {
            stringBuilder.append(customer.getCustName())
                    .append(",")
                    .append(customer.getPhoneNum())
                    .append(",")
                    .append(customer.getJobTitle())
                    .append(",")
                    .append(customer.getSource());
        }

        IOUtils.write(stringBuilder.toString(),outputStream,"UTF-8");
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 将客户导出为xls文件
     *
     * @param staff 员工对象
     * @param outputStream 输出流
     */
    @Override
    public void exportXlsFileToOutputStream(Staff staff, OutputStream outputStream) throws IOException {

        List<Customer> customerList = findAllCustomerByStaffId(staff.getId());

        //创建工作表
        Workbook workbook = new HSSFWorkbook();
        //创建sheet
        Sheet sheet = workbook.createSheet("我的客户");
        //创建行
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(0);//创建单元格
        cell.setCellValue("姓名");
        row.createCell(1).setCellValue("手机号");
        row.createCell(2).setCellValue("职位");
        row.createCell(3).setCellValue("来源");

        for (int i = 0;i < customerList.size();i++) {
            Customer customer = customerList.get(i);
            Row customerRow = sheet.createRow(i + 1);
            customerRow.createCell(0).setCellValue(customer.getCustName());
            customerRow.createCell(1).setCellValue(customer.getPhoneNum());
            customerRow.createCell(2).setCellValue(customer.getJobTitle());
            customerRow.createCell(3).setCellValue(customer.getSource());

        }

        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 查询该客户所有的销售机会
     *
     * @param id
     * @return
     */
    @Override
    public List<Record> findAllRecord(Integer id) {

        RecordExample recordExample = new RecordExample();
        recordExample.createCriteria().andCustIdEqualTo(id);
        return recordMapper.selectByExample(recordExample);

    }

    /**
     * 根据id删除对应的客户
     *
     * @param id
     * @throws ServiceException 删除不成功抛出异常
     */
    @Override
    public void deleteCustomerById(Integer id) throws ServiceException {
        //TODO 检查是否有关联文件
        //TODO 检查是否有待办事项

        RecordExample recordExample = new RecordExample();
        recordExample.createCriteria().andCustIdEqualTo(id);
        List<Record> recordList = recordMapper.selectByExample(recordExample);
        if (!recordList.isEmpty()) {
            throw new ServiceException("该客户有销售机会，无法删除");
        }
        customerMapper.deleteByPrimaryKey(id);
    }

    /**
     * 更新客户信息
     *
     * @param customer
     */
    @Override
    public void editCustomer(Customer customer) {
        customer.setUpdateTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 获取当前员工的所有客户
     * 并进行倒序排列（最新添加的客户再最上面）
     * @param staffId 员工id
     */
    private List<Customer> findAllCustomerByStaffId(Integer staffId) {

        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andStaffIdEqualTo(staffId);
        customerExample.setOrderByClause("id desc");

        return customerMapper.selectByExample(customerExample);
    }
}
