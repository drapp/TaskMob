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

package com.stackmob.android.sdk.activity;

import com.stackmob.sdk.exception.StackMobException;
import static com.stackmob.android.sdk.common.StackMobCommon.*;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class TwitterActivity extends Activity {
	private static final String TAG = TwitterActivity.class.getCanonicalName();
	
	private static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	private static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	
	private static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	private static final String OAUTH_CALLBACK_HOST = "callback";
	private static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
	
	public static final int RESULT_ERROR = 0;
	public static final int RESULT_OK = 1;
	
	private CommonsHttpOAuthConsumer consumer;
	private CommonsHttpOAuthProvider provider;
	
	private void setTwitterTokens(String token, String secret, Throwable failure) {
		if(TwitterCallback != null) {
			if(failure != null) {
				TwitterCallback.failure(new StackMobException(failure.getMessage()));
			}
			
			else {
				TwitterCallback.success(token, secret);
			}
		}
	}
	
	private class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {
		@Override
		protected Void doInBackground(Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			
			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);
			}
			catch (Exception e) {
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
				setTwitterTokens(null, null, e);
				finish();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setTwitterTokens(consumer.getToken(), consumer.getTokenSecret(), null);
			finish();
		}
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  
		  try {
			  consumer = new CommonsHttpOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
			  provider = new CommonsHttpOAuthProvider(REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);
		  }
		  catch (Exception e) {
			  Log.e(TAG, "Error creating consumer / provider", e);
			  setTwitterTokens(null, null, e);
			  finish();
			  return;
		  }
		  
		  Log.i(TAG, "Starting task to retrieve request token.");
		  new OAuthRequestTokenTask().execute();
	  }
	  
	  private class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void> {
		  @Override
		  protected Void doInBackground(Void... params) {
			  try {
				  Log.i(TAG, "Retrieving request token from Google servers");
				  final String url = provider.retrieveRequestToken(consumer, OAUTH_CALLBACK_URL);
				  Log.i(TAG, "Popping a browser with the authorize URL : " + url);
				  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
				  .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						  | Intent.FLAG_ACTIVITY_NO_HISTORY
						  | Intent.FLAG_FROM_BACKGROUND);
				  startActivity(intent);
			  }
			  catch (Exception e) {
				  setTwitterTokens(null, null, e);
			  }
			  finish();
			  return null;
		  }
	  }
	  	  
	  @Override
	  public void onNewIntent(Intent intent) {
		  super.onNewIntent(intent);
		  final Uri uri = intent.getData();
		  Log.d(TAG, "Calling onNewIntent with scheme: " + uri.getScheme());
		  
		  if (uri != null && uri.getScheme().equals(OAUTH_CALLBACK_SCHEME)) {
			  Log.i(TAG, "Callback received : " + uri);
			  Log.i(TAG, "Retrieving Access Token");
			  new RetrieveAccessTokenTask().execute(uri);
		  }
	  }
}
