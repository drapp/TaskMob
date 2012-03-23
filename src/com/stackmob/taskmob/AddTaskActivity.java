package com.stackmob.taskmob;

import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class AddTaskActivity extends Activity{

	private static final int DATE_DIALOG = 0;
	
	TextView taskName;
	TextView dueDateDisplay;
	Button pickDueDate;
	Button addTaskButton;
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
    	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    		task.setDueDate(new Date(year - 1900, monthOfYear, dayOfMonth));
    		AddTaskActivity.this.dueDateDisplay.setText("Date: " + task.getDueDate().toString());
    	}
    };

	String dueDateLabel;
	Task task;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtask);
		taskName = (TextView) findViewById(R.id.new_task_name);
		pickDueDate = (Button) findViewById(R.id.change_date);
		pickDueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	showDialog(DATE_DIALOG);
            }
        });

		dueDateDisplay = (TextView) findViewById(R.id.date_display);
		dueDateLabel = dueDateDisplay.getText().toString();
		addTaskButton = (Button) findViewById(R.id.add_task_button2);
		addTaskButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				task.setName(taskName.getText().toString());
				Intent i = getIntent();
				i.putExtra("new_task", task.toJson());
				setResult(RESULT_OK, i);
				finish();
			}
		});
	    Spinner spinner = (Spinner) findViewById(R.id.spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.priorities, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				task.setPriority(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
	    	
	    });
		task = new Task();
    }
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG:
	    	Date dateToDisplay = task.getDueDate() == null ? new Date() : task.getDueDate();
	        return new DatePickerDialog(this, dateSetListener, dateToDisplay.getYear() + 1900, dateToDisplay.getMonth(), dateToDisplay.getDate());
	    }
	    return null;
	}


}
