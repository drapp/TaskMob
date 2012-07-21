package com.stackmob.taskmob;

import com.stackmob.sdk.exception.StackMobException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class TaskActivity extends ListActivity {
	private TaskList taskList;
	private Button addTaskButton;
	private TaskAdapter adapter;
	private int index;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks);
		taskList = new TaskList();
		try {
			taskList.fillFromJson(getIntent().getExtras().getString(TaskMob.TASKLIST_KEY));
		} catch (StackMobException e) {
			Toast.makeText(getApplicationContext(), "Error deserializing " + e.getMessage(), Toast.LENGTH_LONG);
		}
		index = getIntent().getIntExtra(TaskMob.TASKLIST_INDEX, 0);
		
		adapter = new TaskAdapter(getApplicationContext(), R.layout.taskrow, taskList.getTasks());
		setListAdapter(adapter);

		addTaskButton = (Button) this.findViewById(R.id.add_task_button);
		addTaskButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		    	startActivityForResult(new Intent(getApplicationContext(), AddTaskActivity.class),0);
			}
		});
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		    	Task t = adapter.getItem(pos);
		    	CheckBox cb = (CheckBox) v.findViewById(R.id.task_done_checkbox);
		    	cb.setChecked(!cb.isChecked());
		    	t.setDone(cb.isChecked());
		    	t.save();
		    }
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			Task t = new Task();
			t.fillFromJson(data.getStringExtra("new_task"));
			taskList.getTasks().add(t);
			taskList.saveWithDepth(1);
			adapter.notifyDataSetChanged();
		} catch (StackMobException e) {
		}
	}
	
	@Override
	public void onBackPressed() {
		Intent i = getIntent();
		i.putExtra(TaskMob.TASKLIST_INDEX, index);
		i.putExtra(TaskMob.TASKLIST_RETURN_KEY, taskList.toJson());
		setResult(RESULT_OK, i);
		super.onBackPressed();
	}
}