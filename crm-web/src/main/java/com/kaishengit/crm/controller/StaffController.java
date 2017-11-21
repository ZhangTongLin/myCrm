package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.controller.exception.ForbidException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.*;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.StaffService;
import com.kaishengit.crm.service.TaskService;
import com.kaishengit.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Administrator.
 */
@Controller
@RequestMapping("/staff")
public class StaffController extends BaseController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private TaskService taskService;

    @GetMapping("/set")
    public String staffSet() {
        return "staff/set";
    }


    /**
     * 新增员工信息
     * @param userName
     * @param phoneNum
     * @param password
     * @param deptId
     * @return
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult addStaff(String userName,String phoneNum,
                               String password,Integer[] deptId) {

        try {
            System.out.println(deptId);
            staffService.addStaff(userName,phoneNum,password,deptId);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return AjaxResult.error(ex.getMessage());
        }
    }

    /**
     * 根据ID删除员工信息
     * @param id
     * @return
     */
    @PostMapping("/{id:\\d+}/delete")
    @ResponseBody
    public AjaxResult deleteStaff(@PathVariable Integer id) {

        try {

            staffService.deleteStaffById(id);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            return AjaxResult.error(ex.getMessage());
        }
    }

    /**
     * 获取当前用户的所有工作记录
     * @param session 用于获取当前用户
     * @param model 用于向前端传值
     * @return 跳转路径
     */
    @GetMapping("/my/record")
    public String myRecode(HttpSession session,
                           Model model,
                           @RequestParam(required = false,defaultValue = "1",name = "p") Integer pageNo) {

        Staff staff = getCurrentAccount(session);
        PageInfo<Record> pageInfo = staffService.findMyRecord(staff,pageNo);

        model.addAttribute("pageInfo",pageInfo);

        return "staff/record/my";
    }

    /**
     * 跳转到新增机会页面
     * @return 跳转路径
     */
    @GetMapping("/my/record/new")
    public String myRecordNew(HttpSession session, Model model) {

        Staff staff = getCurrentAccount(session);

        model.addAttribute("progressList",staffService.findAllProgress());
        model.addAttribute("customerList",staffService.findMyCustomer(staff.getId()));

        return "staff/record/new";
    }

    /**
     * 显示机会的详情页
     * @param id
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/my/record/{id:\\d+}")
    public String myRecordShow(@PathVariable Integer id,
                               Model model,
                               HttpSession session) {

        Record record = checkRole(id, session);

        //跟进记录列表
        List<Progress> followRecord = staffService.findFollowRecordBySaleId(id);

        //待办事项列表
        List<Task> taskList = taskService.findAllTaskByRecordId(id);

        model.addAttribute("taskList",taskList);
        model.addAttribute("followRecord",followRecord);
        model.addAttribute("progressList",staffService.findAllProgress());
        model.addAttribute("record",record);

        return "/staff/record/show";
    }

    /**
     * 验证权限
     * @param id
     * @param session
     * @return
     */
    private Record checkRole(@PathVariable Integer id,
                             HttpSession session) {
        Record record = staffService.findRecordWithCustomerById(id);

        if (record == null) {
            throw new NotFoundException("该记录不存在");
        }

        Staff staff = getCurrentAccount(session);
        if (!record.getStaffId().equals(staff.getId())) {
            throw new ForbidException("您没有权限查看该记录");
        }
        return record;
    }

    /**
     * 更新机会的状态
     * @param id
     * @param session
     * @param progress
     * @return
     */
    @PostMapping("/my/record/{id:\\d+}/progress/update")
    public String updateProgress(@PathVariable Integer id,
                                               HttpSession session,
                                               String progress) {
        checkRole(id, session);
        staffService.updateRecord(id,progress);

        return "redirect:/staff/my//record/"+ id;
    }

    @PostMapping("/my/record/progress/update")
    public String updateMyRecordFollowProgress(Progress progress,
                                               HttpSession session) {
        checkRole(progress.getSaleId(),session);
        staffService.insertProgress(progress);
        return "redirect:/staff/my/record/"+ progress.getSaleId();
    }

    /**
     * 新增销售机会
     * @return
     */
    @PostMapping("/my/record/new")
    public String addRecord(Record record,
                            RedirectAttributes redirectAttributes) {

        staffService.addRecord(record);
        redirectAttributes.addFlashAttribute("message","新增成功");

        return "redirect:/staff/my/record";
    }

    /**
     * 删除对应的销售机会
     * @param id
     * @return
     */
    @GetMapping("/my/record/{id:\\d+}/delete")
    public String deleteRecord(@PathVariable Integer id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        checkRole(id, session);
        staffService.deleteRecordById(id);
        redirectAttributes.addFlashAttribute("message","删除成功");

        return "redirect:/staff/my/record/";

    }
}
