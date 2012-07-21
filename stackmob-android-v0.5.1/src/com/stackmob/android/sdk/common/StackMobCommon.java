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

package com.stackmob.android.sdk.common;

import android.content.Context;

import com.stackmob.android.sdk.callback.*;
import com.stackmob.sdk.callback.StackMobRedirectedCallback;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobRequest;
import com.stackmob.sdk.api.StackMob.OAuthVersion;

import java.util.Map;

public class StackMobCommon {
	public static OAuthVersion OAUTH_VERSION = OAuthVersion.One;
	public static String API_KEY = "YOUR_API_KEY_HERE";
	public static String API_SECRET = "YOUR_API_SECRET_HERE";
	public static String USER_OBJECT_NAME = "user";
	public static Integer API_VERSION = 0;
	
	public static String API_URL_FORMAT = "api.mob1.stackmob.com";
	public static String PUSH_API_URL_FORMAT = "push.mob1.stackmob.com";
	
	public static String TWITTER_CONSUMER_KEY = "YOUR_TWITTER_CONSUMER_KEY_HERE";
	public static String TWITTER_CONSUMER_SECRET = "YOUR_TWITTER_CONSUMER_SECRET_HERE";
	
	public static String FACEBOOK_APP_ID = "YOUR_FACEBOOK_APP_ID_HERE";
	
	public static boolean LOGGING_ENABLED = false;
	
	public static StackMobTwitterCallback TwitterCallback = null;
	public static StackMobFacebookCallback FacebookCallback = null;

	private static boolean initialized = false;
	
	private static StackMobRedirectedCallback redirectedCallback = new StackMobRedirectedCallback() {
		@Override public void redirected(String originalURL, Map<String, String> redirectHeaders, String redirectBody, String newURL) {
			//do nothing for now
		}
	};
	
	// Init using the constants above
	public static void init(Context c) {
		init(c, OAUTH_VERSION, API_KEY, API_SECRET, USER_OBJECT_NAME, API_VERSION, API_URL_FORMAT, PUSH_API_URL_FORMAT);
	}
	
	// Init with minimal information and the most basic defaults
	public static void init(Context c, String publicKey, int apiVersion) {
		init(c, OAuthVersion.Two, publicKey, "None", USER_OBJECT_NAME, apiVersion, API_URL_FORMAT, PUSH_API_URL_FORMAT);
	}
	
	// Init with minimal information for oauth1 and the most basic defaults
	public static void init(Context c, String publicKey, String privateKey, int apiVersion) {
		init(c, OAuthVersion.Two, publicKey, privateKey, USER_OBJECT_NAME, apiVersion, API_URL_FORMAT, PUSH_API_URL_FORMAT);
	}
	
	// Init specifying all options
	public static void init(Context c, OAuthVersion version, String publicKey, String privateKey, String userObjectName, int apiVersion, String apiUrlFormat, String pushUrlFormat) {
		StackMob.setStackMob(new StackMob(version, publicKey, privateKey, userObjectName, apiVersion, apiUrlFormat, pushUrlFormat, redirectedCallback));
		StackMob.getStackMob().setSession(new StackMobAndroidSession(c, StackMob.getStackMob().getSession()));
		StackMob.setUserAgentName("Android");
		StackMob.setLogger(new StackMobAndroidLogger());
		StackMob.getLogger().setLogging(LOGGING_ENABLED);
		StackMobRequest.setCookieStore(new StackMobAndroidCookieStore(c));
		initialized = true;
	}

	public static StackMob getStackMobInstance() {
		if(!initialized) throw new IllegalStateException("Make sure to call StackMobCommon.init(Contect c) in onCreate");
		return StackMob.getStackMob();
	}
}
