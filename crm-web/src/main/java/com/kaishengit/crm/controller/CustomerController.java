package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.controller.exception.ForbidException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.entity.Record;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.CustomerService;
import com.kaishengit.crm.service.StaffService;
import com.kaishengit.crm.service.TaskService;
import com.kaishengit.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator.
 */
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private StaffService staffService;

    /**
     * 查找我的客户
     * @param model
     * @param pageNo
     * @param session
     * @return
     */
    @GetMapping("/my")
    public String myCustomerList(Model model,
                                 @RequestParam(name = "p",required = false,defaultValue = "1") Integer pageNo,
                                 HttpSession session) {

        Staff staff = getCurrentAccount(session);
        PageInfo<Customer> pageInfo = customerService.findMyCustomer(pageNo,staff.getId());
        model.addAttribute("pageInfo",pageInfo);

        return "customer/my";
    }

    /**
     * 跳转公海客户页面
     * @param pageNo
     * @param model
     * @return
     */
    @GetMapping("/public")
    public String publicCustomerList(@RequestParam(defaultValue = "1",name = "p",required = false) Integer pageNo,
                                     Model model) {
        PageInfo<Customer> pageInfo = staffService.findAllPublicCustomer(pageNo);
        model.addAttribute("pageInfo",pageInfo);
        return "customer/public";
    }

    /**
     * 将客户放入公海
     * @param id
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/public/{id:\\d+}")
    public String addPublicCustomer(@PathVariable Integer id,
                                    RedirectAttributes redirectAttributes) {


        deleteTaskAndRecordByCustomerId(id);

        //放入公海
        Customer customer = customerService.addPublicCustomer(id);

        redirectAttributes.addFlashAttribute("message","以成功将" + customer.getCustName() + "放入公海");
        return "redirect:/customer/my";
    }

    /**
     * 显示公海客户详情
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/public/show/{id:\\d+}")
    public String showPublicCustomer(@PathVariable Integer id,
                                     Model model) {

        Customer customer = customerService.findCustomerById(id);
        model.addAttribute("customer",customer);
        return "customer/publicShow";
    }

    /**
     * 将公海客户添加至我的客户
     * @param id
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/public/{id:\\d+}/my")
    public String addPublicCustomerToMy(@PathVariable Integer id,
                                        RedirectAttributes redirectAttributes) {


        Staff staff = getCurrentStaff();
        customerService.customerToMy(id,staff);

        redirectAttributes.addFlashAttribute("message","恭喜您收入一个新客户");
        return "redirect:/customer/my";
    }

    /**
     * 根据顾客id删除对应的待办事项和销售机会
     * @param id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void deleteTaskAndRecordByCustomerId(@PathVariable Integer id) {

        //删除待办，根据客户id
        taskService.deleteTaskByCustomerId(id);

        List<Record> recordList = customerService.findAllRecord(id);

        for (Record record : recordList) {
            //删除待办事项，根据销售机会id
            taskService.deleteTaskByRecordId(record.getId());
            //删除销售机会
            staffService.deleteRecordById(record.getId());
        }
    }

    /**
     * 添加客户页面
     * @return
     */
    @GetMapping("/my/new")
    public String addCustomer(Model model) {

        model.addAttribute("sources", customerService.findAllSource());
        model.addAttribute("trades",customerService.findAllTrade());

        return "customer/new";
    }

    /**
     * 新增客户
     * @return
     */
    @PostMapping("/my/new")
    @ResponseBody
    public AjaxResult addCustomer(Customer customer) {

        try {
            customerService.addCustomer(customer);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return AjaxResult.error(ex.getMessage());
        }
    }

    /**
     * 强制添加客户
     * @return
     */
    @PostMapping("/my/new/compel")
    @ResponseBody
    public AjaxResult addCustomerCompel(Customer customer) {
        customerService.addCustomerCompel(customer);
        return AjaxResult.success();
    }

    /**
     *查看客户详情
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/my/show/{id:\\d+}")
    public String showCustomer(@PathVariable Integer id,
                               HttpSession session,
                               Model model) {

        Customer customer = checkRole(id, session);

        //销售机会列表
        List<Record> recordList = customerService.findAllRecord(customer.getId());

        //待办事项列表
        List<Task> taskList = taskService.findAllTaskByCustomerId(id);

        //所有用户的列表 用于转交他人
        List<Staff> staffList = staffService.findAllStaff();

        model.addAttribute("progressList",staffService.findAllProgress());
        model.addAttribute("staffList",staffList);
        model.addAttribute("taskList",taskList);
        model.addAttribute("recordList",recordList);
        model.addAttribute("customer",customer);

        return "customer/show";
    }

    /**
     * 验证当前登录用户的权限
     * @param id
     * @param session
     * @return
     */
    private Customer checkRole(@PathVariable Integer id, HttpSession session) {
        Customer customer = customerService.findCustomerById(id);

        if (customer == null) {
            throw new NotFoundException("该客户不存在");
        }

        Staff staff = getCurrentAccount(session);
        if (!customer.getStaffId().equals(staff.getId())) {
            throw new ForbidException("您没有权限查看该客户");
        }
        return customer;
    }

    /**
     * 导出csv格式的文件
     * @param response
     * @param session
     * @throws IOException
     */
    @GetMapping("/my/export.csv")
    public void exportCsv(HttpServletResponse response,
                          HttpSession session) throws IOException {


        Staff staff = getCurrentAccount(session);

        response.setContentType("text/csv;charset=GBK");
        String fileName = new String("我的客户.cvs".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");

        OutputStream outputStream = response.getOutputStream();

        customerService.exportCsvFileToOutputStream(staff,outputStream);

    }

    /**
     * 导出为xls文件
     * @param response
     * @param session
     */
    @GetMapping("/my/export.xls")
    public void exportXls(HttpServletResponse response,
                          HttpSession session) throws IOException {

        Staff staff = getCurrentAccount(session);

        response.setContentType("application/vnd.ms-excel");
        String fileName = new String("我的客户.xls".getBytes("UTF-8"),"ISO8859-1");
        response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");

        OutputStream outputStream = response.getOutputStream();

        customerService.exportXlsFileToOutputStream(staff,outputStream);

    }

    /**
     * 删除对应的客户
     * @param id
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/my/{id:\\d+}/delete")
    public String deleteCustomer(@PathVariable Integer id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        checkRole(id, session);
        try {
            customerService.deleteCustomerById(id);
            redirectAttributes.addFlashAttribute("message","删除成功");
        } catch (ServiceException se) {
            redirectAttributes.addFlashAttribute("message",se.getMessage());
        }

        return "redirect:/customer/my";

    }

    /**
     * 跳转到更新客户页面
     * @param id
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/my/{id:\\d+}/edit")
    public String editMyCustomer(@PathVariable Integer id,
                                 HttpSession session,
                                 Model model) {

        checkRole(id, session);
        Customer customer = customerService.findCustomerById(id);
        model.addAttribute("customer",customer);
        model.addAttribute("sources", customerService.findAllSource());
        model.addAttribute("trades",customerService.findAllTrade());

        return "customer/edit";

    }

    /**
     * 更新客户信息
     * @param customer
     * @return
     */
    @PostMapping("/my/edit")
    public String editMyCustomer(Customer customer) {

        customerService.editCustomer(customer);
        return "redirect:/customer/my/show/"+ customer.getId();
    }

    /**
     * 转交他人
     * @param customerId
     * @param toStaffId
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/my/{customerId:\\d+}/tran/{toStaffId:\\d+}")
    public String tranCustomer(@PathVariable Integer customerId,
                               @PathVariable Integer toStaffId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Customer customer = checkRole(customerId,session);
        Staff staff = staffService.findStaffById(toStaffId);
        if (staff != null) {
            customerService.tranCustomerToStaff(toStaffId,customer);
        } else {
            throw new NotFoundException("该用户不存在");
        }

        redirectAttributes.addFlashAttribute("message","转交成功");
        return "redirect:/customer/my";

    }
}
