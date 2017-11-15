package com.kaishengit.test;

import com.kaishengit.crm.jobs.MyQuartzJobs;
import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

/**
 * @author Administrator.
 */
public class QuartzTriggerTest {

    @Test
    public void simpleTrigger() throws SchedulerException, IOException {

//        JobDetail jobDetail = JobBuilder.newJob(MyQuartzJobs.class).build();
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
//        //设置执行间隔
//        scheduleBuilder.withIntervalInSeconds(3);
//        scheduleBuilder.repeatForever();
//        Trigger simpleTrigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();
//
//        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
//        scheduler.scheduleJob(jobDetail,simpleTrigger);
//        scheduler.start();

        //设置jobDetail中 的参数
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("customerId",200);

        //定义job
        JobDetail jobDetail = JobBuilder
                .newJob(MyQuartzJobs.class)
                .setJobData(jobDataMap)
                .build();
        //定义trigger
        ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ? ");
        Trigger cronTrigger = TriggerBuilder.newTrigger().withSchedule(scheduleBuilder).build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(jobDetail,cronTrigger);
        scheduler.start();

        System.in.read();

    }
}
