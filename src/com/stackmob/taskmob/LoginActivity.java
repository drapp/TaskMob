package com.stackmob.taskmob;

import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobSession;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	TextView username;
	TextView password;
	Button login;
	Button createUser;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
 		username = (TextView) findViewById(R.id.username);
		password = (TextView) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	login(getUser());
            }
        });
		createUser = (Button) findViewById(R.id.createuser);
		createUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	createUser(getUser());
            }
        });
	}
	
	public User getUser() {
		return new User(username.getText().toString(), password.getText().toString());
	}
	
	public void login(final StackMobUser user) {
		user.login(new StackMobCallback() {
			
			@Override
			public void success(String arg0) {
				StackMobSession session = StackMob.getStackMob().getSession();
				Intent i = getIntent();
				i.putExtra(TaskMob.LOGGED_IN_USER, user.toJson());
				setResult(RESULT_OK, i);
				finish();	
			}
			
			@Override
			public void failure(StackMobException arg0) {
				Toast.makeText(getApplicationContext(), arg0.getMessage(), 10).show();
				
			}
		});
	}
	
	public void createUser(final User user) {
		user.save(new StackMobCallback() {
			
			@Override
			public void success(String arg0) {
				login(user);	
			}
			
			@Override
			public void failure(StackMobException arg0) {
				Toast.makeText(getApplicationContext(), arg0.getMessage(), 10).show();
			}
		});
	}
}
