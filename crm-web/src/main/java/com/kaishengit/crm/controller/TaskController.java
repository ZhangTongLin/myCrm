package com.kaishengit.crm.controller;

import com.kaishengit.crm.controller.exception.ForbidException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Staff;
import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 新添加一个待办事项
     * @param title
     * @param finish
     * @param remind
     * @param redirectAttributes
     * @param session
     * @param recordId
     * @param custId
     * @return
     */
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

    @GetMapping("/{id:\\d+}/delete")
    public String deleteTask(@PathVariable Integer id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        checkRole(id, session);

        taskService.deleteTaskById(id);
        redirectAttributes.addFlashAttribute("message","删除成功");

        return "redirect:/task/wait";

    }

    @GetMapping("/{id:\\d+}/done")
    public String updateTaskDone(@PathVariable Integer id,
                                 HttpSession session) {

        checkRole(id,session);
        taskService.updateTask(id);
        return "redirect:/task/wait";
    }
    /**
     * 权限验证
     * @param id
     * @param session
     */
    private void checkRole(@PathVariable Integer id, HttpSession session) {
        Task task = taskService.findTaskById(id);

        Staff staff = getCurrentAccount(session);

        if (task == null) {
            throw new NotFoundException();
        }

        if (!task.getStaffId().equals(staff.getId())) {
            throw new ForbidException();
        }
    }

}
