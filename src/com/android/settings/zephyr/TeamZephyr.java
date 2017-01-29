/*
 * Copyright (C) 2016 The ZephyrOS Project
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

package com.android.settings.zephyr;

import android.os.Bundle;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import android.preference.PreferenceGroup;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.MetricsProto.MetricsEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TeamZephyr extends SettingsPreferenceFragment {

    public TeamZephyr() {
        // empty fragment constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.team_zephyr, container, false);

        Random rng = new Random();

        ViewGroup crewGroup = (ViewGroup) root.findViewById(R.id.devs);

	// remove all developers from the view randomize them, add em back
        int N = crewGroup.getChildCount();
        while (N > 0) {
            View removed = crewGroup.getChildAt(rng.nextInt(N));
            crewGroup.removeView(removed);
            crewGroup.addView(removed);
            N -= 1;
        }
        return root;
    }


    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.ABOUT_ZEPHYR;
    }

}
