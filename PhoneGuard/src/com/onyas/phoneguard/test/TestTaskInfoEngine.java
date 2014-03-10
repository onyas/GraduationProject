package com.onyas.phoneguard.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.onyas.phoneguard.domain.TaskInfo;
import com.onyas.phoneguard.engine.TaskInfoEngine;

public class TestTaskInfoEngine extends AndroidTestCase {

	
	public void testGetAll(){
		TaskInfoEngine engine = new TaskInfoEngine(getContext());
		List<TaskInfo> infos = engine.getAllTasks();
		System.out.println(infos.size());
		for(TaskInfo info :infos){
			System.out.println(info.getAppname());
		}
	}
	
}
