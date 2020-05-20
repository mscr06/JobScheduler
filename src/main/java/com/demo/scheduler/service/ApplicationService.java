package com.demo.scheduler.service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;


@Service
public class ApplicationService {

	private static final Log LOGGER = LogFactory.getLog(ApplicationService.class);

	@Autowired
	private ScheduledAnnotationBeanPostProcessor postProcessor;

	@Autowired
	private ScheduledThreadPoolExecutor scheduler;
	   
	private static Map<String,ScheduledFuture<?>> jobsMap = new HashMap<>();
    
	@Value("${application_scheduler_initial_delay}")
	private  String initialDelay;
	
	@Value("${application_scheduler_fixed_delay}")
	private  String fixedDelay;
	

	@Scheduled(initialDelayString="${application_scheduler_initial_delay}",fixedDelayString="${application_scheduler_initial_delay}")
	private void task1() throws InterruptedException {

		String processName=Thread.currentThread().getName()+"-task1";
		LOGGER.info(processName+" Started At:" + LocalDateTime.now());
		Thread.sleep(5000);
		LOGGER.info(processName+" Completed At:" + LocalDateTime.now());

	}
	
	@Scheduled(initialDelayString="${application_scheduler_initial_delay}",fixedDelayString="${application_scheduler_initial_delay}")
	private void task2() throws InterruptedException {
		
		String processName=Thread.currentThread().getName()+"-task2";
		LOGGER.info(processName+" Started At:" + LocalDateTime.now());
		Thread.sleep(10000);
		LOGGER.info(processName+" Completed At:" + LocalDateTime.now());

	}
	
	public void stopSchedule(String methodName, boolean stopCurrentFlag) {
		try {
			ScheduledFuture<?> future = ApplicationService.jobsMap.get(methodName);
			if (future != null) {
				LOGGER.info("Stop Current Flag:" + Boolean.valueOf(stopCurrentFlag));
				future.cancel(stopCurrentFlag);
				LOGGER.info("Canceled " + methodName);

			}

			for (ScheduledTask task : this.postProcessor.getScheduledTasks()) {
				ScheduledMethodRunnable methodRunnable = (ScheduledMethodRunnable) task.getTask().getRunnable();
				if (methodRunnable.getMethod().getName().equalsIgnoreCase(methodName)) {
					this.scheduler.remove(methodRunnable);
					if (future == null)
						task.cancel();// Warning this will Interrupt the Current Running Task Too
					ApplicationService.jobsMap.put(methodName, null);
					break;
				}

			}

		} catch (Exception e) {
			LOGGER.error("Error Stopping "+methodName);
		}

	}
	
	
	public void startSchedule(String methodName,long initialDelayValue,long fixedDelayValue) throws NoSuchMethodException {
		
		initialDelayValue=(initialDelayValue<=0)?new Long(this.initialDelay):initialDelayValue;
		fixedDelayValue=(fixedDelayValue<=0)?new Long(this.fixedDelay):fixedDelayValue;
		
		for (ScheduledTask task : this.postProcessor.getScheduledTasks()) {
			 ScheduledMethodRunnable methodRunnable = (ScheduledMethodRunnable) task.getTask().getRunnable();
			if (methodRunnable.getMethod().getName().equalsIgnoreCase(methodName)) {
				LOGGER.info("Resume "+methodName);
				ScheduledFuture<?> future=this.scheduler.scheduleWithFixedDelay(methodRunnable, initialDelayValue,fixedDelayValue,TimeUnit.MILLISECONDS);
				ApplicationService.jobsMap.put(methodName,future);
				break;
			}

		}
		LOGGER.info("Started "+methodName+" with "+ initialDelayValue +" Initial Delay & "+ fixedDelayValue +" Fixed Delay");
	}
	
	public void startTimerSchedule(String task,long initialDelayValue,long fixedDelayValue) throws NoSuchMethodException {
		
		initialDelayValue=(initialDelayValue<=0)?new Long(this.initialDelay):initialDelayValue;
		fixedDelayValue=(fixedDelayValue<=0)?new Long(this.fixedDelay):fixedDelayValue;
		Timer timer = new Timer(); // creating timer
	    TimerTask timerTask = new MyTask(); // creating timer task
	    timer.schedule(timerTask, initialDelayValue, fixedDelayValue); // scheduling the task
		LOGGER.info("Started "+task+" with timer "+ initialDelayValue +" Initial Delay & "+ fixedDelayValue +" Fixed Delay");
		try {
			Thread.sleep(fixedDelayValue);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		LOGGER.info("Task3 is cancelled:"+timerTask.cancel());
	}
	
}
