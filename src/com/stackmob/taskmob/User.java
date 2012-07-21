package com.stackmob.taskmob;

import java.util.ArrayList;
import java.util.List;

import com.stackmob.sdk.model.StackMobUser;

public class User extends StackMobUser {

	protected User(String username, String password) {
		super(User.class, username, password);
	}
	
	private List<TaskList> taskLists;
	
	public List<TaskList>  getTaskLists() {
		if(taskLists == null) {
			taskLists = new ArrayList<TaskList>();
		}
		return taskLists;
	}
	
	public void setTasks(List<TaskList>  taskLists) {
		this.taskLists = taskLists;
	}

}
