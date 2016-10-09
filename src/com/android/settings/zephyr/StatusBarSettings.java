package com.android.settings.zephyr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import net.margaritov.preference.colorpicker.ColorPickerPreference;
import com.android.internal.logging.MetricsProto.MetricsEvent;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String KEY_HEADS_UP_SETTINGS = "heads_up_settings";
    private static final String KEY_ZEPHYR_LOGO_COLOR = "status_bar_zephyr_logo_color";
    private static final String KEY_ZEPHYR_LOGO_STYLE = "status_bar_zephyr_logo_style";


    private PreferenceScreen mHeadsUp;
    private ColorPickerPreference mZephyrLogoColor;
    private ListPreference mZephyrLogoStyle;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.status_bar_settings);
        PreferenceScreen prefScreen = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();
	Context context = getActivity();

        mHeadsUp = (PreferenceScreen) findPreference(KEY_HEADS_UP_SETTINGS);
	mZephyrLogoStyle = (ListPreference) findPreference(KEY_ZEPHYR_LOGO_STYLE);
            int zephyrLogoStyle = Settings.System.getIntForUser(resolver,
                    Settings.System.STATUS_BAR_ZEPHYR_LOGO_STYLE, 0,
                    UserHandle.USER_CURRENT);
            mZephyrLogoStyle.setValue(String.valueOf(zephyrLogoStyle));
            mZephyrLogoStyle.setSummary(mZephyrLogoStyle.getEntry());
            mZephyrLogoStyle.setOnPreferenceChangeListener(this);

            // Zephyr logo color
            mZephyrLogoColor =
                (ColorPickerPreference) prefScreen.findPreference(KEY_ZEPHYR_LOGO_COLOR);
            mZephyrLogoColor.setOnPreferenceChangeListener(this);
            int intColor = Settings.System.getInt(resolver,
                    Settings.System.STATUS_BAR_ZEPHYR_LOGO_COLOR, 0xffffffff);
            String hexColor = String.format("#%08x", (0xffffffff & intColor));
            mZephyrLogoColor.setSummary(hexColor);
            mZephyrLogoColor.setNewPreviewColor(intColor);

    }

    private boolean getUserHeadsUpState() {
         return Settings.System.getInt(getContentResolver(),
                Settings.System.HEADS_UP_USER_ENABLED,
                Settings.System.HEADS_UP_USER_ON) != 0;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	ContentResolver resolver = getActivity().getContentResolver();
	if (preference == mZephyrLogoColor) {
                String hex = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                preference.setSummary(hex);
                int intHex = ColorPickerPreference.convertToColorInt(hex);
                Settings.System.putInt(resolver,
                        Settings.System.STATUS_BAR_ZEPHYR_LOGO_COLOR, intHex);
                return true;
            } else if (preference == mZephyrLogoStyle) {
                int zephyrLogoStyle = Integer.valueOf((String) newValue);
                int index = mZephyrLogoStyle.findIndexOfValue((String) newValue);
                Settings.System.putIntForUser(
                        resolver, Settings.System.STATUS_BAR_ZEPHYR_LOGO_STYLE, zephyrLogoStyle,
                        UserHandle.USER_CURRENT);
                mZephyrLogoStyle.setSummary(
                        mZephyrLogoStyle.getEntries()[index]);
                return true;
            }
        return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.THE_VORTEX;
    }

    @Override
    public void onResume() {
        super.onResume();

        mHeadsUp.setSummary(getUserHeadsUpState()
                ? R.string.summary_heads_up_enabled : R.string.summary_heads_up_disabled);
    }
}
