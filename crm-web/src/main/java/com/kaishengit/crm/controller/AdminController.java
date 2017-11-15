package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Department;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.AdminService;
import com.kaishengit.crm.service.StaffService;
import com.kaishengit.result.AjaxResult;
import com.kaishengit.result.DataTablesResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private AdminService adminService;

    /**
     * 获取部门的json对象
     * @return
     */
    @GetMapping("/dept.json")
    @ResponseBody
    public List<Department> deptList() {
        return adminService.findAllDept();
    }

    @GetMapping("/staffManage")
    public String staffManage() {
        return "admin/staffManage";
    }

    /**
     *  添加新部门
     * @param deptName
     * @return
     */
    @PostMapping("/dept/new")
    @ResponseBody
    public AjaxResult addDept(String deptName) {
        try {

            adminService.saveDept(deptName);
            return AjaxResult.success();

        } catch (ServiceException ex) {
            ex.printStackTrace();
            return AjaxResult.error(ex.getMessage());
        }
    }

    /**
     * dataTable的异步请求
     * @param draw
     * @return
     */
    @GetMapping("/load.json")
    @ResponseBody
    public DataTablesResult<Staff> findStaffWithDeptByParam(Integer draw,
                                                    Integer start,
                                                    Integer length,
                                                    Integer deptId,
                                                    HttpServletRequest request) {

        String serchValue = request.getParameter("search[value]");

        Map<String,Object> params = new HashMap<>();
        params.put("start",start);
        params.put("draw",draw);
        params.put("length",length);
        params.put("deptId",deptId);
        params.put("serchValue",serchValue);

        List<Staff> staffList = staffService.pageForStaff(params);

        Long total = staffService.countByDeptId(deptId);

        return new DataTablesResult<Staff>(draw,total.intValue(),staffList);
    }

}
