package com.kaishengit.crm.service.impl;

import com.kaishengit.crm.entity.Task;
import com.kaishengit.crm.example.TaskExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.TaskMapper;
import com.kaishengit.crm.service.TaskService;
import com.kaishengit.crm.jobs.SendMassageJob;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    private Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * 添加一个新的待办事项
     * @param params
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
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
            throw new ServiceException(e.getMessage());
        }

        taskMapper.insertSelective(task);

        logger.info("添加了新的任务 {}",task.getTitle());

        //添加新的调度任务
        String remind = (String)params.get("remind");
        addNewScheduler(remind, task);

    }

    /**
     * 添加新的调度任务
     * @param remind 提醒时间
     * @param task
     */
    private void addNewScheduler(String remind, Task task) {
        if (StringUtils.isNotEmpty(remind)) {

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.putAsString("staffId",task.getStaffId());
            jobDataMap.put("message",task.getTitle());

            JobDetail jobDetail = JobBuilder
                    .newJob(SendMassageJob.class)
                    .withIdentity("taskId"+task.getId(),"sendMessage")
                    .setJobData(jobDataMap)
                    .build();

            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            DateTime dateTime = dateTimeFormatter.parseDateTime(remind);

            StringBuilder cronExpression = new StringBuilder("0")
                    .append(" ")
                    .append(dateTime.getMinuteOfHour())
                    .append(" ")
                    .append(dateTime.getHourOfDay())
                    .append(" ")
                    .append(dateTime.getDayOfMonth())
                    .append(" ")
                    .append(dateTime.getMonthOfYear())
                    .append(" ? ")
                    .append(dateTime.getYear());

            ScheduleBuilder scheduleBuilder = CronScheduleBuilder
                    .cronSchedule(cronExpression.toString());
            Trigger cronTrigger = TriggerBuilder.newTrigger()
                    .withSchedule(scheduleBuilder).build();

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            try {
                scheduler.scheduleJob(jobDetail,cronTrigger);
                scheduler.start();
            } catch (SchedulerException e) {
                throw new ServiceException("添加定时任务异常");
            }

        }
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
        taskExample.setOrderByClause("finish_time desc");
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 根据id删除对应的待办事项
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteTaskById(Integer id) {

        Task task = findTaskById(id);
        deleteTimedTask(task);
        taskMapper.deleteByPrimaryKey(id);

    }

    /**
     * 删除定时任务
     * @param task
     */
    private void deleteTimedTask(Task task) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if (task.getRemindTime() != null && !task.getRemindTime().equals("")) {
            try {
                scheduler.deleteJob(new JobKey("taskId" +task.getId(),"sendMessage"));
            } catch (SchedulerException e) {
                throw new ServiceException("删除定时任务异常");
            }
        }
    }

    /**
     * 根据id查询对应的待办事项
     *
     * @param id
     * @return
     */
    @Override
    public Task findTaskById(Integer id) {
        return taskMapper.selectByPrimaryKey(id);
    }

    /**
     * 更改当前待办事项的状态
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor =RuntimeException.class )
    public void updateTask(Integer id) {
        Task task = findTaskById(id);
        task.setDone(DONE);
        deleteTimedTask(task);
        taskMapper.updateByPrimaryKeySelective(task);
    }

    /**
     * 根据销售机会的id 查询对应的待办事项
     *
     * @param id
     * @return
     */
    @Override
    public List<Task> findAllTaskByRecordId(Integer id) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andRecordIdEqualTo(id);
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 根据顾客的id查询对应的待办事项
     *
     * @param id
     * @return
     */
    @Override
    public List<Task> findAllTaskByCustomerId(Integer id) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andCustIdEqualTo(id);
        return taskMapper.selectByExample(taskExample);
    }

    /**
     * 更新待办事项
     *
     * @param params
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void editTask(Map<String, Object> params) {

        Integer taskId = (Integer) params.get("taskId");
        Task task = findTaskById(taskId);
        //删除原有的定时任务
        deleteTimedTask(task);

        //更新数据库，并添加新的定时任务
        try {
            SimpleDateFormat finishSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date finishTime = finishSimpleDateFormat.parse((String) params.get("finish"));

            task.setTitle((String) params.get("title"));
            task.setFinishTime(finishTime);
            if (params.get("remind") != null) {

                SimpleDateFormat remindSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date remindTime = null;

                remindTime = remindSimpleDateFormat.parse((String) params.get("remind"));

                task.setRemindTime(remindTime);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskMapper.updateByPrimaryKeySelective(task);

        //添加新的调度任务
        String remind = (String) params.get("remind");

        addNewScheduler(remind,task);
    }

    /**
     * 根据客户id删除对应的待办事项
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteTaskByCustomerId(Integer id) {

        List<Task> taskList = findAllTaskByCustomerId(id);

        if (!taskList.isEmpty()) {
            for (Task task : taskList) {
                deleteTimedTask(task);
            }
        }
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andCustIdEqualTo(id);
        taskMapper.deleteByExample(taskExample);
    }

    /**
     * 根据销售机会id删除对应的待办事项
     *
     * @param id
     */
    @Override
    public void deleteTaskByRecordId(Integer id) {
        TaskExample taskExample = new TaskExample();
        taskExample.createCriteria().andRecordIdEqualTo(id);
        List<Task> taskList = taskMapper.selectByExample(taskExample);

        if (taskList != null && !taskList.isEmpty()) {
            for (Task task : taskList) {
                deleteTimedTask(task);
                taskMapper.deleteByPrimaryKey(task.getId());
            }
        }

    }
}
