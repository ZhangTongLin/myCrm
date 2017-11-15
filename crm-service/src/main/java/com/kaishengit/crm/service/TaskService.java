package com.kaishengit.crm.service;

import com.kaishengit.crm.entity.Task;

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

    /**
     * 根据id删除对应的待办事项
     * @param id
     */
    void deleteTaskById(Integer id);

    /**
     * 根据id查询对应的待办事项
     * @param id
     * @return
     */
    Task findTaskById(Integer id);

    /**
     * 更改当前待办事项的状态
     * @param id
     */
    void updateTask(Integer id);

    /**
     * 根据销售机会的id 查询对应的待办事项
     * @param id
     * @return
     */
    List<Task> findAllTaskByRecordId(Integer id);

    /**
     * 根据顾客的id查询对应的待办事项
     * @param id
     * @return
     */
    List<Task> findAllTaskByCustomerId(Integer id);
}
