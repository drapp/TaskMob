package com.stackmob.android.sdk.common;

import com.stackmob.sdk.api.StackMobLogger;
import android.util.Log;


public class StackMobAndroidLogger extends StackMobLogger {
	
	private static String TAG = "StackMob";
	
	private boolean enableLogging = false;
	
    public void setLogging(boolean logging) {
        enableLogging = logging;
    }
	
    public void logDebug(String format, Object... args) {
        if(enableLogging) Log.d(TAG, String.format(format, args));
    }

    public void logInfo(String format, Object... args) {
        if(enableLogging) Log.i(TAG, String.format(format, args));
    }

    public void logWarning(String format, Object... args) {
        if(enableLogging) Log.w(TAG, String.format(format, args));
    }

    public void logError(String format, Object... args) {
        if(enableLogging) Log.e(TAG, String.format(format, args));
    }
    

}
