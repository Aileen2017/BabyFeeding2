package com.projects.babyfeeding2;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;


import android.view.Menu;
import android.view.MenuItem;

public class UserPreferencesActivity extends Activity implements OnSharedPreferenceChangeListener 
{
	UserPreferencesFragment upf;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) 
	{
	        super.onCreate(savedInstanceState);
	     // setTheme(R.style.AppTheme);
	
	        setContentView(R.layout.activity_user_preferences);	
	       //PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	       
	        upf= new UserPreferencesFragment();
	       getFragmentManager().beginTransaction()
	                .replace(R.id.userPrefsll, upf)
	                .commit();
	    
	       
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_preferences, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		if (key.equals("Volumn_Unit")) {
            Preference unitPref = upf.findPreference(key);
            // Set summary to be the user-description for the selected value
            if(sharedPreferences.getString("Volumn_Unit", "").equalsIgnoreCase("1"))
            	unitPref.setSummary(R.string.volumn_unit_preferencesSummary_Milliliter);
            if(sharedPreferences.getString("Volumn_Unit", "").equalsIgnoreCase("2"))
            	unitPref.setSummary(R.string.volumn_unit_preferencesSummary_Ounce);
           
        }
		if (key.equals("Time_Type")) {
            Preference timePref = upf.findPreference(key);
            if(sharedPreferences.getString("Time_Type", "").equalsIgnoreCase("1"))
            	timePref.setSummary(R.string.time_type__preferencesSummary_12hours);
            if(sharedPreferences.getString("Time_Type", "").equalsIgnoreCase("2"))
            	timePref.setSummary(R.string.time_type__preferencesSummary_24hours);
        }

	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    upf.getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	    Preference unitPref = upf.findPreference("Volumn_Unit");
       
        Preference timePref = upf.findPreference("Time_Type");
        
        if(sharedPreferences.getString("Volumn_Unit", "").equalsIgnoreCase("1"))
        	unitPref.setSummary(R.string.volumn_unit_preferencesSummary_Milliliter);
        if(sharedPreferences.getString("Volumn_Unit", "").equalsIgnoreCase("2"))
        	unitPref.setSummary(R.string.volumn_unit_preferencesSummary_Ounce);
        if(sharedPreferences.getString("Time_Type", "").equalsIgnoreCase("1"))
        	timePref.setSummary(R.string.time_type__preferencesSummary_12hours);
        if(sharedPreferences.getString("Time_Type", "").equalsIgnoreCase("2"))
        	timePref.setSummary(R.string.time_type__preferencesSummary_24hours);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    upf.getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
}
