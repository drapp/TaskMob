package com.stackmob.taskmob;

import com.stackmob.sdk.exception.StackMobException;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class TaskActivity extends ListActivity {
	private TaskList taskList;
	private Button addTaskButton;
	private TaskAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks);
		taskList = new TaskList();
		try {
			taskList.fillFromJson(getIntent().getExtras().getString("task_list"));
		} catch (StackMobException e) {
			Toast.makeText(getApplicationContext(), "Error deserializing " + e.getMessage(), 5);
		}
		
		adapter = new TaskAdapter(getApplicationContext(), R.layout.tasklistrow, taskList.getTasks());
		setListAdapter(adapter);

		addTaskButton = (Button) this.findViewById(R.id.add_task_button);
		addTaskButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
		});
		Toast.makeText(getApplicationContext(), "got taskList " + taskList.getName(), 5).show();
	}
}
