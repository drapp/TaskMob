package com.stackmob.taskmob;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

public class TaskList extends StackMobModel {

	private String name;
	private List<Task> tasks = new ArrayList<Task>();
	
	public TaskList() {
		super(TaskList.class);
	}

	public TaskList(String name) {
		this();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Task> getTasks() {
		return tasks;
	}
}
