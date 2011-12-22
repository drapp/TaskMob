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

package com.stackmob.android.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;

public class EnvironmentInfoUtil {

  public static String getApplicationInfo(Context context) {
    return String.format("%s\n%s\n%s\n%s\n%s\n%s\n",
        getCountry(context), getBrandInfo(), getModelInfo(),
        getDeviceInfo(), getVersionInfo(context),
        getLocale(context));
  }

  public static String getCountry(Context context) {
    TelephonyManager mTelephonyMgr = (TelephonyManager) context
        .getSystemService(Context.TELEPHONY_SERVICE);
    return String.format("Country: %s", mTelephonyMgr
        .getNetworkCountryIso());
  }

  public static String getModelInfo() {
    return String.format("Model: %s", Build.MODEL);
  }

  public static String getBrandInfo() {
    return String.format("Brand: %s", Build.BRAND);
  }

  public static String getDeviceInfo() {
    return String.format("Device: %s", Build.DEVICE);
  }

  public static String getLocale(Context context) {
    return String.format("Locale: %s", context.getResources()
        .getConfiguration().locale.getDisplayName());
  }

  public static String getVersionInfo(Context context) {
    String version = null;

    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(
          context.getPackageName(), 0);
      version = info.versionName + " (release " + info.versionCode + ")";
    } catch (NameNotFoundException e) {
      version = "not_found";
    }

    return String.format("Version: %s", version);
  }
}