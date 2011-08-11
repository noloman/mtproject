package app.mtproject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {
	public static final String CALLS_FREQUENCY_PREF = "CALLS_FREQUENCY_PREF";
	public static final String SMS_FREQUENCY_PREF = "SMS_FREQUENCY_PREF";
	public static final String LOCATION_FREQUENCY_PREF = "LOCATION_FREQUENCY_PREF";
	
	SharedPreferences prefs;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}