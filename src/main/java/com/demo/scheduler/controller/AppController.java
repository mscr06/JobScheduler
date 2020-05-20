package com.demo.scheduler.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.scheduler.service.ApplicationService;
import com.google.common.collect.ImmutableMap;


@RestController
@RequestMapping(value="/demo/scheduler")
public class AppController {
	
	@Autowired
	private ApplicationService applicationService;
	
	@GetMapping(value="/stop",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String,String> stopTask(@RequestParam String taskName,@RequestParam(required=false,defaultValue="false") Boolean stopCurrentFlag){
		this.applicationService.stopSchedule(taskName,stopCurrentFlag);
		return ImmutableMap.of("message","Task Stopped");
	}
	@GetMapping(value="/start",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String, String> startTask(@RequestParam String taskName,
			@RequestParam(required = false, defaultValue = "-1") Long initialDelayValue,
			@RequestParam(required = false, defaultValue = "-1") Long fixedDelayValue) throws NoSuchMethodException {
		this.applicationService.startSchedule(taskName, initialDelayValue, fixedDelayValue);
		return ImmutableMap.of("message","Task Started");
	}
	@GetMapping(value="/startTimer",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String, String> startTimer(@RequestParam(defaultValue = "task3") String taskName,
			@RequestParam(required = false, defaultValue = "-1") Long initialDelayValue,
			@RequestParam(required = false, defaultValue = "-1") Long fixedDelayValue) throws NoSuchMethodException {
		this.applicationService.startTimerSchedule(taskName, initialDelayValue, fixedDelayValue);
		return ImmutableMap.of("message","Task Started");
	}
	
}
