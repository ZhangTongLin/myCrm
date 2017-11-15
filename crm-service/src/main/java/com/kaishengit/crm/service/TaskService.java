package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Task;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 任务的业务层
 * @author Administrator.
 */
public interface TaskService {
    /**
     * 添加一个新的待办事项
     * @param params
     */
    void addTask(Map<String, Object> params);

    /**
     * 查找当前用户的所有待办事项
     * @param id
     * @return
     */
    List<Task> findAllTaskByStaffId(Integer id);
}
