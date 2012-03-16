package com.stackmob.taskmob;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

public class Task extends StackMobModel {
	
	private String name;
	private String details;
	private Date dueDate;
	private int priority = 0;
	private boolean done = false;
	
	public Task() {
		super(Task.class);
	}
	
	public Task(String name, String details, TaskList list) {
		this();
	}
}
