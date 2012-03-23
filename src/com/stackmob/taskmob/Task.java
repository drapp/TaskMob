package com.stackmob.taskmob;

import java.util.Date;


import com.stackmob.sdk.model.StackMobModel;

public class Task extends StackMobModel {
	
	private String name;
	private Date dueDate;
	private int priority = 0;
	private boolean done = false;
	
	public Task() {
		super(Task.class);
	}
	
	public Task(String name, String details, TaskList list) {
		this();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean getDone() {
		return done;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}
