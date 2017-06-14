package com.example.shogun.astroapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceActivity {


    List<Update> updates = new ArrayList<>();

    public void addCallbackObjects(Update callback) {
        updates.add(callback);
    }

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        updates.add(MainActivity.getActivity());
        updates.add(BasicInfoFragment.getInstance());
        updates.add(OtherInfoFragment.getInstance());
        updates.add(ForecastFragment.getInstance());


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Utility.updateAstroCalculator(getApplicationContext());
        if (!updates.isEmpty()) {
            try {
                for (Update call : updates) {
                    call.callbackUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public interface Update {
        public void callbackUpdate();
    }
}
