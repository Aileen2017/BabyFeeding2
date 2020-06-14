package com.projects.babyfeeding2;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class UserPreferencesFragment extends PreferenceFragment
{

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

	
	

}
