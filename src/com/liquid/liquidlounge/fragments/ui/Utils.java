/*
 * Copyright (C) 2018 The Liquid Remix Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liquid.liquidlounge.fragments.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;

import static android.provider.Settings.Secure.DOZE_ENABLED;

public final class Utils {

    private static final String TAG = "DozeUtils";
    private static final boolean DEBUG = false;
    private static final String DOZE_INTENT = "com.android.systemui.doze.pulse";

    public static final String CATEG_PROX_SENSOR = "proximity_sensor";

    public static void startService(Context context) {
        if (DEBUG) Log.d(TAG, "Starting service");
        context.startServiceAsUser(new Intent(context, DozeService.class),
                UserHandle.CURRENT);
    }

    public static void stopService(Context context) {
        if (DEBUG) Log.d(TAG, "Stopping service");
        context.stopServiceAsUser(new Intent(context, DozeService.class),
                UserHandle.CURRENT);
    }

    public static boolean getProxCheckBeforePulse(Context context) {
        try {
            Context con = context.createPackageContext("com.android.systemui", 0);
            int id = con.getResources().getIdentifier("doze_proximity_check_before_pulse",
                    "bool", "com.android.systemui");
            return con.getResources().getBoolean(id);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isDozeEnabled(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(),
                DOZE_ENABLED, context.getResources().getBoolean(
                com.android.internal.R.bool.config_doze_enabled_by_default) ? 1 : 0,
                UserHandle.USER_CURRENT) != 0;
    }

    public static boolean aodEnabled(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(),
                Settings.Secure.DOZE_ALWAYS_ON, 0, UserHandle.USER_CURRENT) == 1;
    }

    public static boolean aodAvailable(Context context) {
        return context.getResources().getBoolean(
                com.android.internal.R.bool.config_dozeAlwaysOnDisplayAvailable);
    }

    public static void enableService(boolean enable, Context context) {
        if (enable) {
            startService(context);
        } else {
            stopService(context);
        }
    }

    public static void launchDozePulse(Context context) {
        if (DEBUG) Log.d(TAG, "Launch doze pulse");
        context.sendBroadcastAsUser(new Intent(DOZE_INTENT),
                new UserHandle(UserHandle.USER_CURRENT));
    }

    public static boolean tiltEnabled(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(),
                Settings.System.DOZE_TRIGGER_TILT, 0, UserHandle.USER_CURRENT) == 1;
    }

    public static boolean pickUpEnabled(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(),
                Settings.System.DOZE_TRIGGER_PICKUP, 0, UserHandle.USER_CURRENT) == 1;
    }

    public static boolean handwaveGestureEnabled(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(),
                Settings.System.DOZE_TRIGGER_HANDWAVE, 0, UserHandle.USER_CURRENT) == 1;
    }

    public static boolean pocketGestureEnabled(Context context) {
        return Settings.System.getIntForUser(context.getContentResolver(),
                Settings.System.DOZE_TRIGGER_POCKET, 0, UserHandle.USER_CURRENT) == 1;
    }

    public static boolean sensorsEnabled(Context context) {
        return tiltEnabled(context) || pickUpEnabled(context) || handwaveGestureEnabled(context)
                || pocketGestureEnabled(context);
    }
}
