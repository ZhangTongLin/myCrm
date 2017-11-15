package com.kaishengit.crm.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Administrator.
 */
public class MyQuartzJobs implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {

        //取值
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        Integer staffId = jobDataMap.getInt("staffId");
        String message = jobDataMap.getString("message");
        System.out.println("staffId++++++++++++++" + staffId + "message>>>>>>>>>>>>>>" + message);
    }
}
