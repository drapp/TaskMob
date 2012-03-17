package com.stackmob.taskmob;

import java.util.ArrayList;
import java.util.List;

import com.stackmob.taskmob.R;
import com.stackmob.android.sdk.common.StackMobCommon;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobModelQuery;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskMob extends ListActivity {

	private StackMobModelQuery<TaskList> tasksQuery = new StackMobModelQuery<TaskList>(TaskList.class).expandDepthIs(1);
	private TaskListAdapter adapter;
	private Button addTaskListButton;
	private TextView addTaskListName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StackMobCommon.API_KEY = "API_KEY";
		StackMobCommon.API_SECRET = "API_SECRET";
		StackMobCommon.USER_OBJECT_NAME = "user";
		StackMobCommon.API_VERSION = 0;
		StackMobCommon.API_URL_FORMAT = "api.mob1.stackmob.com";
		StackMobCommon.PUSH_API_URL_FORMAT = "push.mob1.stackmob.com";
		StackMobCommon.init(this.getApplicationContext());
		addTaskListButton = (Button) this.findViewById(R.id.add_tasklist_button);
		addTaskListName = (TextView) this.findViewById(R.id.add_tasklist_text);
		addTaskListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addTaskList(addTaskListName.getText().toString());
			}
		});
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		    	//Toast.makeText(getApplicationContext(), "click on " + pos, 5).show();
		    	Intent i = new Intent(getApplicationContext(), TaskActivity.class);
		    	i.putExtra("task_list", adapter.getItem(pos).toJson(1));
		    	startActivity(i);
		    }
		});

		loadTasks();
	}

	private void addTaskList(String name) {
		TaskList newList = new TaskList(name);
		newList.save();
		adapter.add(newList);
		adapter.notifyDataSetChanged();
	}

	private void loadTasks() {
		tasksQuery.send(new StackMobQueryCallback<TaskList>() {

			@Override
			public void success(List<TaskList> taskLists) {
				initAdapter(taskLists);
			}

			@Override
			public void failure(StackMobException e) {
				if(e.getMessage().contains("Schema tasklist not found")) {
					// We've never added a tasklist, so StackMob has no clue what a tasklist is. Assume an empty set.
					initAdapter(new ArrayList<TaskList>());
				} else {
					Toast.makeText(getApplicationContext(), "Error loading data " + e.getMessage(), 5).show();
				}
			}
		});
	}
	
	private void initAdapter(List<TaskList> taskLists) {
		final List<TaskList> listsToAdd = taskLists;
		//StackMobCallbacks come back on a different thread
		runOnUiThread(new Runnable() {	
			@Override
			public void run() {
				adapter = new TaskListAdapter(getApplicationContext(), R.layout.tasklistrow, listsToAdd);
				setListAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		});

	}
}
