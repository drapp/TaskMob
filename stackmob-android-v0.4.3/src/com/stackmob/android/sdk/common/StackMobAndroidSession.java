package com.stackmob.android.sdk.common;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.stackmob.sdk.api.StackMobSession;

public class StackMobAndroidSession extends StackMobSession {
	
	private static final String SERVER_TIME_KEY = "servertimediff";
	private SharedPreferences.Editor editor;
	private Date nextSaveTime = new Date();
	private static long SAVE_INTERVAL = 10 * 60 * 1000;

	public StackMobAndroidSession(Context context, StackMobSession session) {
		super(session);
		SharedPreferences prefs = context.getSharedPreferences("stackmob.servertimediff", 0);
		editor = prefs.edit();
		super.saveServerTimeDiff(prefs.getLong(SERVER_TIME_KEY, 0));
	}
	
	@Override
    protected void saveServerTimeDiff(long serverTimeDiff) {
		super.saveServerTimeDiff(serverTimeDiff);
		//We don't want to write to disk with every request
		if(nextSaveTime.before(new Date())) {
			editor.putLong(SERVER_TIME_KEY, serverTimeDiff);
			editor.commit();
			nextSaveTime.setTime(new Date().getTime() + SAVE_INTERVAL);
		}
    }
}
