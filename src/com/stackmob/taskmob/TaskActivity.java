package com.stackmob.taskmob;

import com.stackmob.sdk.exception.StackMobException;

import android.app.ListActivity;
import android.content.Intent;
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
		    	startActivityForResult(new Intent(getApplicationContext(), AddTaskActivity.class),0);
			}
		});
		Toast.makeText(getApplicationContext(), "got taskList " + taskList.getName(), 5).show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			Task t = new Task();
			t.fillFromJson(data.getStringExtra("new_task"));
			taskList.getTasks().add(t);
			taskList.saveWithDepth(1);
			adapter.add(t);
			adapter.notifyDataSetChanged();
		} catch (StackMobException e) {
		}
	}
}
