/**
 * Copyright 2011 StackMob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stackmob.android;

import android.content.Context;
import android.content.SharedPreferences;

public class PushRegistrationIDHolder {
	public static class NoStoredRegistrationIDException extends Exception {
		private static final long serialVersionUID = -4109182687918155379L;
		public NoStoredRegistrationIDException(String s) {
			super(s);
		}
	}
	private final String TAG = PushRegistrationIDHolder.class.getCanonicalName();
	private final String KEY = "registrationID";
	
	private Context context;
	private SharedPreferences prefs;
	public PushRegistrationIDHolder(Context ctx) {
		this.context = ctx;
		this.prefs = this.context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
	}
	
	public Boolean hasID() {
		return this.prefs.contains(KEY);
	}
	
	public void setID(String id) {
		SharedPreferences.Editor editor = this.prefs.edit();
		editor.putString(KEY, id);
		editor.commit();
	}
	
	public String getID() throws NoStoredRegistrationIDException {
		String id = this.prefs.getString(KEY, null);
		if(id == null) {
			throw new NoStoredRegistrationIDException("no registration ID was stored in shared prefs");
		}
		return id;
	}

}
