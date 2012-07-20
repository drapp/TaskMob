package com.stackmob.android;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.callback.StackMobNoopCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.push.StackMobPushToken.TokenType;

public class GCMIntentService extends GCMBaseIntentService {
	
	private String regId;

	public GCMIntentService() {
		super(AndroidStarterActivity.SENDER_ID);
	}

	@Override
	protected void onError(Context ctx, String errorId) {
		Toast.makeText(ctx, "Got a error " + errorId, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onMessage(Context ctx, Intent arg1) {
		Toast.makeText(ctx, "Got a message!", Toast.LENGTH_LONG).show();
		
	}

	@Override
	protected void onRegistered(Context ctx, String regId) {
		this.regId = regId;
		PushRegistrationIDHolder holder = new PushRegistrationIDHolder(ctx);
		holder.setID(regId);
		
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		StackMob.getStackMob().removePushToken(regId, TokenType.Android, new StackMobNoopCallback());
	}

}
