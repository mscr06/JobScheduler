package com.demo.scheduler.service;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyTask extends TimerTask {
	   private static final Log LOGGER = LogFactory.getLog(MyTask.class);
	   public void run() {
		      LOGGER.info("Task3 is running");
		   }
}
