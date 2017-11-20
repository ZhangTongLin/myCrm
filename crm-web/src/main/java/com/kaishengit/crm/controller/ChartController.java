package com.kaishengit.crm.controller;

import com.kaishengit.crm.service.StaffService;
import com.kaishengit.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 统计报表的控制层
 * @author Administrator.
 */
@Controller
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    private StaffService staffService;

    @GetMapping
    public String toChart() {
        return "charts";
    }

    @GetMapping("/progress")
    @ResponseBody
    public AjaxResult forCustomerProgress() {
        List<Map<String,Object>> mapList = staffService.customerChart();
        return AjaxResult.success(mapList);
    }


}
