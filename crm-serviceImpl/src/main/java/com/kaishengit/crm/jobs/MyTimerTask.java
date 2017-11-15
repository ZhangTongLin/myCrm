package com.kaishengit.crm.jobs;

import java.util.TimerTask;

/**
 * @author Administrator.
 */
public class MyTimerTask extends TimerTask {
    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        System.out.println("hello,timerTask");
    }
}
