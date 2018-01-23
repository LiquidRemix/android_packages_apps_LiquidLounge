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

package com.liquid.liquidlounge.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.internal.logging.nano.MetricsProto; 
import com.android.settings.SettingsPreferenceFragment;

import com.liquid.liquidlounge.R;

public class AnimationSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "AnimationSettings";

    private static final String SCREEN_OFF_ANIMATION = "screen_off_animation";

    private ListPreference mScreenOffAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.animation_settings);

        final ContentResolver resolver = getActivity().getContentResolver();

        // Screen uff animation
        mScreenOffAnimation = (ListPreference) findPreference(SCREEN_OFF_ANIMATION);
        int screenOffStyle = Settings.System.getInt(resolver,
                 Settings.System.SCREEN_OFF_ANIMATION, 0);
        mScreenOffAnimation.setValue(String.valueOf(screenOffStyle));
        mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntry());
        mScreenOffAnimation.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mScreenOffAnimation) { 
            Settings.System.putInt(getContentResolver(), 
                    Settings.System.SCREEN_OFF_ANIMATION, Integer.valueOf((String) newValue)); 
            int valueIndex = mScreenOffAnimation.findIndexOfValue((String) newValue); 
            mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntries()[valueIndex]); 
            return true;
        }
        return false;
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.System.putInt(resolver,
                Settings.System.SCREEN_OFF_ANIMATION, 0);

        Animations.reset(mContext);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.LIQUID;
    }
}
