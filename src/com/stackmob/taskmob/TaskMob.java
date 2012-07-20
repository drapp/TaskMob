package com.stackmob.taskmob;

import java.util.ArrayList;
import java.util.List;

import com.stackmob.taskmob.R;
import com.stackmob.android.sdk.common.StackMobCommon;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobModelQuery;
import com.stackmob.sdk.model.StackMobUser;

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
	protected static final String TASKLIST_KEY = "task_list";
	protected static final String TASKLIST_RETURN_KEY = "modified_task_list";
	protected static final String TASKLIST_INDEX = "task_list_index";
	protected static final String LOGGED_IN_USER = "logged_in_user";

	private StackMobModelQuery<TaskList> tasksQuery = new StackMobModelQuery<TaskList>(TaskList.class).expandDepthIs(1);
	private TaskListAdapter adapter;
	private Button addTaskListButton;
	private TextView addTaskListName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		StackMobCommon.OAUTH_VERSION = StackMob.OAuthVersion.Two;
		  StackMobCommon.API_KEY = "5c29caee-71f9-4c64-9cf8-fb10a11841f3";
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
		    	Intent i = new Intent(getApplicationContext(), TaskActivity.class);
		    	startActivityForResult(i, 1);
		    }
		});
		
		if(StackMob.getStackMob().isLoggedIn()) {
			User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
				@Override
				public void success(List<User> list) {
					if(list.size() > 0) {
						loadTasks(list.get(0));
					} else {
						doLogin();
					}
				}

				@Override
				public void failure(StackMobException e) {
					doLogin();
				}
			});
		} else {
			doLogin();
		}
	}
	
	private void doLogin() {
    	Intent i = new Intent(getApplicationContext(), LoginActivity.class);
    	startActivityForResult(i, 0);
	}

	private void addTaskList(String name) {
		TaskList newList = new TaskList(name);
		newList.save();
		adapter.add(newList);
		adapter.notifyDataSetChanged();
	}

	private void loadTasks(final User user) {
		user.fetchWithDepth(2, new StackMobCallback() {
			
			@Override
			public void success(String arg0) {
				initAdapter(user.getTaskLists());
			}
			
			@Override
			public void failure(StackMobException e) {
				Toast.makeText(getApplicationContext(), "Error loading data " + e.getMessage(), 5).show();
				
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if(requestCode == 0) {
				if(data.getExtras().containsKey(TASKLIST_INDEX) && data.getExtras().containsKey(TASKLIST_RETURN_KEY)) {
					adapter.getItem(data.getIntExtra(TASKLIST_INDEX, 0)).fillFromJson(data.getStringExtra(TASKLIST_RETURN_KEY));
					adapter.notifyDataSetChanged();
				}
			} else if(requestCode == 1) {
				if(data.getExtras().containsKey(LOGGED_IN_USER)) {
					User user = new User("","");
					user.fillFromJson(data.getStringExtra(LOGGED_IN_USER));
					loadTasks(user);
				}
			}
		} catch (StackMobException e) {
		}
	}
}
