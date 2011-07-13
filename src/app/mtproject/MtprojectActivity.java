package app.mtproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MtprojectActivity extends Activity {
	/** Called when the activity is first created. */
	
	boolean CheckboxPreference;
    String ListPreference;
    String editTextPreference;
    String ringtonePreference;
    String secondEditTextPreference;
    String customPref;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getPrefs();
		Button prefBtn = (Button) findViewById(R.id.prefsBtn);
		prefBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent settingsActivity = new Intent(getBaseContext(),Preferences.class);
				startActivity(settingsActivity);
			}
		});
	}

    private void getPrefs() {
            // Get the xml/preferences.xml preferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            CheckboxPreference = prefs.getBoolean("callsLogChk", true);
            CheckboxPreference = prefs.getBoolean("smsLogChk", true);
            CheckboxPreference = prefs.getBoolean("locationLogChk", false);
    }
}