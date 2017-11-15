package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.example.TaskExample;
import com.kaishengit.crm.mapper.TaskMapper;
import com.kaishengit.crm.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务的业务层
 * @author Administrator.
 */
@Service
public class  TaskServiceImpl implements TaskService {

    /**
     * 如果为1就是已经做了
     */
    private static final Byte DONE = 1;
    /**
     * 如果为0就是待办
     */
    private static final Byte NOTDONE = 0;

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 添加一个新的待办事项
     * @param params
     */
    @Override
    public void addTask(Map<String, Object> params){


        Task task = new Task();

        try {
            SimpleDateFormat finishSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date finishTime = finishSimpleDateFormat.parse((String) params.get("finish"));

            task.setTitle((String) params.get("title"));
            task.setCreateTime(new Date());
            task.setDone(NOTDONE);
            task.setFinishTime(finishTime);
            task.setStaffId((Integer) params.get("staffId"));

            if (params.get("remind") != null) {

                SimpleDateFormat remindSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date remindTime = remindSimpleDateFormat.parse((String) params.get("remind"));
                task.setRemindTime(remindTime);
            }

            if (params.get("custId") != null) {
                task.setCustId((Integer) params.get("custId"));
            }

            if (params.get("recordId") != null) {
                task.setRecordId((Integer) params.get("recordId"));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskMapper.insertSelective(task);

    }

    /**
     * 查找当前用户的所有待办事项
     *
     * @param id 当前用户的id
     * @return
     */
    @Override
    public List<Task> findAllTaskByStaffId(Integer id) {

        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andStaffIdEqualTo(id);

        return taskMapper.selectByExample(taskExample);
    }
}
