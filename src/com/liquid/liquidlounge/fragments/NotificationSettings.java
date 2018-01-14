/*
 * Copyright (C) 2017 The Nitrogen Project
 * Copyright (C) 2017 The Liquid Remix Project
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

package com.liquid.liquidlounge.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;

import android.provider.Settings;
import com.liquid.liquidlounge.preferences.Utils;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.R;

public class NotificationSettings extends SettingsPreferenceFragment
                        implements OnPreferenceChangeListener {
							
    private static final String INCALL_VIB_OPTIONS = "incall_vib_options";
	private static final String PREF_LESS_NOTIFICATION_SOUNDS = "less_notification_sounds";
	
    private Preference mChargingLeds;
	private ListPreference mAnnoyingNotifications;
    private ListPreference mNoisyNotification;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.notifications_settings);

        PreferenceScreen prefScreen = getPreferenceScreen();

        PreferenceCategory incallVibCategory = (PreferenceCategory) findPreference(INCALL_VIB_OPTIONS);
        if (!Utils.isVoiceCapable(getActivity())) {
            prefScreen.removePreference(incallVibCategory);
        }

        mChargingLeds = (Preference) findPreference("charging_light");
        if (mChargingLeds != null
                && !getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveBatteryLed)) {
            prefScreen.removePreference(mChargingLeds);
        }

		mAnnoyingNotifications = (ListPreference) findPreference(PREF_LESS_NOTIFICATION_SOUNDS);
        int notificationThreshold = Settings.System.getInt(getContentResolver(),
                Settings.System.MUTE_ANNOYING_NOTIFICATIONS_THRESHOLD, 0);
        mAnnoyingNotifications.setValue(Integer.toString(notificationThreshold));
        int valueIndex = mAnnoyingNotifications.findIndexOfValue(String.valueOf(notificationThreshold));
        if (valueIndex > 0) {
            mAnnoyingNotifications.setSummary(mAnnoyingNotifications.getEntries()[valueIndex]);
        }
        mAnnoyingNotifications.setOnPreferenceChangeListener(this);

        mNoisyNotification = (ListPreference) findPreference("notification_sound_vib_screen_on");
        mNoisyNotification.setOnPreferenceChangeListener(this);
        int mode = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.NOTIFICATION_SOUND_VIB_SCREEN_ON,
                1, UserHandle.USER_CURRENT);
        mNoisyNotification.setValue(String.valueOf(mode));
        mNoisyNotification.setSummary(mNoisyNotification.getEntry());
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mAnnoyingNotifications) {
            String notificationThreshold = (String) newValue;
            int notificationThresholdValue = Integer.parseInt(notificationThreshold);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.MUTE_ANNOYING_NOTIFICATIONS_THRESHOLD, notificationThresholdValue);
            int notificationThresholdIndex = mAnnoyingNotifications
                    .findIndexOfValue(notificationThreshold);
            mAnnoyingNotifications
                    .setSummary(mAnnoyingNotifications.getEntries()[notificationThresholdIndex]);
            return true;
        } else if (preference.equals(mNoisyNotification)) {
            int mode = Integer.parseInt(((String) newValue).toString());
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.NOTIFICATION_SOUND_VIB_SCREEN_ON, mode, UserHandle.USER_CURRENT);
            int index = mNoisyNotification.findIndexOfValue((String) newValue);
            mNoisyNotification.setSummary(
                    mNoisyNotification.getEntries()[index]);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.LIQUID;
    }
}
