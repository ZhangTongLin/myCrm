package com.kaishengit.crm.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时发送提醒的任务
 * @author Administrator.
 */
public class SendMassageJob implements Job {

    Logger logger = LoggerFactory.getLogger(SendMassageJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String message = (String) jobDataMap.get("message");
        Integer staffId = jobDataMap.getInt("staffId");

       logger.info("发送 {} 给 {}",message,staffId);
    }
}
