package com.kaishengit.crm.controller;

import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事项管理器
 * @author Administrator.
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Autowired
    private TaskService taskService;

    /**
     * 待办事项 列表
     * @return
     */
    @GetMapping("/wait")
    public String waitTask(Model model,
                           HttpSession session) {

        Staff staff = getCurrentAccount(session);
        List<Task> taskList = taskService.findAllTaskByStaffId(staff.getId());
        model.addAttribute("taskList",taskList);
        return "/staff/task/wait";
    }

    /**
     * 新增任务页面
     * @return
     */
    @GetMapping("/new")
    public String addTask() {
        return "/staff/task/new";
    }

    @PostMapping("/new")
    public String addTask(String title,
                          String finish,
                          @RequestParam(required = false) String remind,
                          RedirectAttributes redirectAttributes,
                          HttpSession session,
                          @RequestParam(required = false) Integer recordId,
                          @RequestParam(required = false) Integer custId){

        Map<String,Object> params = new HashMap<>();
        Staff staff = getCurrentAccount(session);

        params.put("title",title);
        params.put("finish",finish);
        params.put("staffId",staff.getId());
        params.put("recordId",recordId);
        params.put("custId",custId);
        params.put("remind",remind);

        taskService.addTask(params);

        redirectAttributes.addFlashAttribute("message","添加成功");
        return "redirect:/task/wait";
    }

}
