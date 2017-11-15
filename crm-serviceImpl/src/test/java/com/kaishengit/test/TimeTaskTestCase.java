package com.kaishengit.test;


import com.kaishengit.crm.jobs.MyTimerTask;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;

/**
 * Quartz框架的任务
 * @author Administrator.
 */
public class TimeTaskTestCase {

    @Test
    public void timerTaskTest() throws IOException {

        Timer timer = new Timer();
        //当前任务每2秒执行一次，不延迟
       // timer.schedule(new MyTimerTask(),0,2000);
        //当前任务延迟2s，只执行一次
        //timer.schedule(new MyTimerTask(),2000);
        //从指定的时间开始每隔2s做一次
        timer.schedule(new MyTimerTask(),new Date(),2000);
        System.in.read();

    }

}
