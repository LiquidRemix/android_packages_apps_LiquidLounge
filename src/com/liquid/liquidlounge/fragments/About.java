/*
 * Copyright (C) 2018 LiquidRemix Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liquid.liquidlounge.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import com.android.settings.R;

import java.util.List;
import java.util.ArrayList;

public class About extends SettingsPreferenceFragment implements Indexable {

    public static final String TAG = "About";

    private String KEY_LIQUID_SOURCE = "liquid_source";
    private String KEY_LIQUID_TELEGRAM = "liquid_telegram";
    private String KEY_LIQUID_SHARE = "liquid_share";
    private String KEY_LIQUID_GOOLE_PLUS = "liquid_google_plus";

    private Preference mSourceUrl;
    private Preference mTelegramUrl;
    private Preference mShare;
    private Preference mGoogleUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.liquid_settings_about);

        mSourceUrl = findPreference(KEY_LIQUID_SOURCE);
        mTelegramUrl = findPreference(KEY_LIQUID_TELEGRAM);
        mShare = findPreference(KEY_LIQUID_SHARE);
        mGoogleUrl = findPreference(KEY_LIQUID_GOOLE_PLUS);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mSourceUrl) {
            launchUrl("https://github.com/LiquidRemix");
        } else if (preference == mTelegramUrl) {
            launchUrl("https://t.me/LiquidRemixChat");
        } else if (preference == mShare) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, String.format(
                    getActivity().getString(R.string.share_message), Build.MODEL));
            startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_chooser_title)));
        } else if (preference == mGoogleUrl) {
            launchUrl("https://plus.google.com/communities/115990749843435030575");
        }

        return super.onPreferenceTreeClick(preference);
    }

    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(intent);
    }
    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.LIQUID;
    }

    /**
     * For search
     */
    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.liquid_settings_about;
                    result.add(sir);

                    return result;
                }
            };
}
