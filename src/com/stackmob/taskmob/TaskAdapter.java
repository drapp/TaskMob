package com.stackmob.taskmob;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
	
	public TaskAdapter(Context context, int textViewResourceId, List<Task> tasks) {
		super(context, textViewResourceId, tasks);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.taskrow, null);
		}
		Task task = getItem(position);
		if (task != null) {
			((CheckBox) v.findViewById(R.id.task_done_checkbox)).setChecked(task.getDone());
			((TextView) v.findViewById(R.id.task_name)).setText(task.getName());
			((TextView) v.findViewById(R.id.task_priority)).setText(task.getPriority() == 1 ? "(!)" : "");
		}
		return v;
	}
}
