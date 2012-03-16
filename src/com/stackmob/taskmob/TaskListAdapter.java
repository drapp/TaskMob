package com.stackmob.taskmob;

import java.util.List;

import com.stackmob.taskmob.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskListAdapter extends ArrayAdapter<TaskList> {

	public TaskListAdapter(Context context, int textViewResourceId, List<TaskList> taskLists) {
		super(context, textViewResourceId, taskLists);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.tasklistrow, null);
		}
		TaskList taskList = getItem(position);
		if (taskList != null) {
			((TextView) v.findViewById(R.id.tasklist_name)).setText(taskList.getName());
			((TextView) v.findViewById(R.id.task_count)).setText("(" + taskList.getTasks().size() + ")");
		}
		return v;
	}
}