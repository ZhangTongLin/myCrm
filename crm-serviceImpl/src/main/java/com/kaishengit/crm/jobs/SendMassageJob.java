package com.kaishengit.crm.jobs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

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

        try {
            ApplicationContext applicationContext = (ApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContext");

            JmsTemplate jmsTemplate = (JmsTemplate) applicationContext.getBean("jmsTemplate");

            jmsTemplate.send("weixinMessage-queue", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    String json = "{\"staffId\" : " + staffId + ", \"message\" : \"hello,weixin from jms\"}";
                    TextMessage textMessage = session.createTextMessage(json);
                    return textMessage;
                }
            });

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
