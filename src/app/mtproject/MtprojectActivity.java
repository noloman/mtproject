package app.mtproject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.commonsware.*;

public class MtprojectActivity extends Activity {
	/** Called when the activity is first created. */

	boolean callsCheckbox;
	boolean smsCheckbox;
	boolean locationCheckbox;
	String ListPreference, date, duration, type, bodyColumn, addressColumn, serverAddress,
			dateColumn;
	String serviceToStart;

	private ServiceConnection callsLoggingConnection;
	private ServiceConnection smsLoggingConnection;
	private ServiceConnection locationLoggingConnection;

	static final private int SHOW_PREFERENCES = Menu.FIRST;

	SharedPreferences prefs;

	int callsFrequencyUpdate, smsFrequencyUpdate, locationFrequencyUpdate;

	private boolean startedCalls, startedSms, startedLocation = false;

	public String username;

	LoggingReceiver callsReceiver, smsReceiver, locationReceiver;

	OnSharedPreferenceChangeListener listener;

	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		username = getIntent().getExtras().getString("username");

		ToggleButton callsLoggingBtn = (ToggleButton) findViewById(R.id.callsLoggingBtn);
		ToggleButton smsLoggingBtn = (ToggleButton) findViewById(R.id.smsLoggingBtn);
		ToggleButton locationLoggingBtn = (ToggleButton) findViewById(R.id.locationLogginBtn);

		updateFromPreferences();

		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
				if (key.equals("CALLS_FREQUENCY_PREF")) {
					callsFrequencyUpdate = Integer.parseInt(prefs.getString(
							PreferencesActivity.CALLS_FREQUENCY_PREF, "86400000"));

				} else if (key.equals("SMS_FREQUENCY_PREF")) {
					smsFrequencyUpdate = Integer.parseInt(prefs.getString(
							PreferencesActivity.SMS_FREQUENCY_PREF, "86400000"));

				} else if (key.equals("LOC_FREQUENCY_PREF")) {
					locationFrequencyUpdate = Integer.parseInt(prefs.getString(
							PreferencesActivity.LOCATION_FREQUENCY_PREF, "86400000"));
				}
			}
		};
		

		prefs.registerOnSharedPreferenceChangeListener(listener);

		callsLoggingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!startedCalls) {
					startCallsLogging();
				} else {
					stopCallsLogging();
				}
			}
		});

		smsLoggingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!startedSms) {
					startSmsLogging();
				} else {
					stopSmsLogging();
				}
			}
		});

		locationLoggingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!startedLocation) {
					startLocationLogging();
				} else {
					stopLocationLogging();
				}
			}
		});
	}

	public void updateFromPreferences() {
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		callsFrequencyUpdate = Integer.parseInt(prefs.getString(PreferencesActivity.CALLS_FREQUENCY_PREF, "86400000"));
		smsFrequencyUpdate = Integer.parseInt(prefs.getString(PreferencesActivity.SMS_FREQUENCY_PREF, "86400000"));
		locationFrequencyUpdate = Integer.parseInt(prefs.getString(PreferencesActivity.LOCATION_FREQUENCY_PREF, "86400000"));
	    serverAddress = prefs.getString(PreferencesActivity.SERVER_ADDRESS, "http://localhost");
	}

	private void savePreferences() {
		SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = activityPreferences.edit();
		editor.putInt(PreferencesActivity.CALLS_FREQUENCY_PREF, callsFrequencyUpdate);
		editor.putInt(PreferencesActivity.SMS_FREQUENCY_PREF, smsFrequencyUpdate);
		editor.putInt(PreferencesActivity.LOCATION_FREQUENCY_PREF, locationFrequencyUpdate);
		editor.putString(PreferencesActivity.SERVER_ADDRESS, serverAddress);
		editor.commit();
	}

	@Override
	protected void onResume() {
		IntentFilter callsFilter = new IntentFilter(LoggingReceiver.ACTION_CALLS_LOGGING);
		callsReceiver = new LoggingReceiver();
		registerReceiver(callsReceiver, callsFilter);
		
		IntentFilter smsFilter = new IntentFilter(LoggingReceiver.ACTION_SMS_LOGGING);
		smsReceiver = new LoggingReceiver();
		registerReceiver(smsReceiver, smsFilter);
		
		IntentFilter locationFilter = new IntentFilter(LoggingReceiver.ACTION_LOCATION_LOGGING);
		locationReceiver = new LoggingReceiver();
		registerReceiver(locationReceiver, locationFilter);

		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(callsReceiver);
		unregisterReceiver(smsReceiver);
		unregisterReceiver(locationReceiver);
		savePreferences();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SHOW_PREFERENCES, Menu.NONE, R.string.preferences);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SHOW_PREFERENCES: {
			Intent i = new Intent(this, PreferencesActivity.class);
			startActivityForResult(i, SHOW_PREFERENCES);
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	private void startCallsLogging() {
		Intent i = new Intent(getBaseContext(), LoggingService.class);
		i.putExtra("serviceToStart", "calls");
		i.putExtra("username", username);
		LoggingService.sendWakefulWork(getApplicationContext(), i);
		startedCalls = true;
	}

	private void stopCallsLogging() {
		if (!startedCalls) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(getBaseContext(), LoggingService.class);
			i.putExtra("serviceToStop", "calls");
			LoggingService.sendWakefulWork(getApplicationContext(), i);
			startedCalls = false;
		}
	}

	private void startSmsLogging() {
		Intent i = new Intent(getBaseContext(), LoggingService.class);
		i.putExtra("serviceToStart", "sms");
		i.putExtra("username", username);
		LoggingService.sendWakefulWork(getApplicationContext(), i);
		startedSms = true;
	}

	private void stopSmsLogging() {
		if (!startedSms) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(getBaseContext(), LoggingService.class);
			i.putExtra("serviceToStop", "sms");
			LoggingService.sendWakefulWork(getApplicationContext(), i);
			startedSms = false;
		}
	}

	private void startLocationLogging() {
		Intent i = new Intent(getBaseContext(), LoggingService.class);
		i.putExtra("serviceToStart", "location");
		i.putExtra("username", username);
		LoggingService.sendWakefulWork(getApplicationContext(), i);
		startedLocation = true;
	}

	private void stopLocationLogging() {
		if (!startedLocation) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(getBaseContext(), LoggingService.class);
			i.putExtra("serviceToStop", "location");
			LoggingService.sendWakefulWork(getApplicationContext(), i);
			startedLocation = false;
		}
	}

	// private boolean isCallsServiceRunning() {
	// ActivityManager manager = (ActivityManager)
	// getSystemService(ACTIVITY_SERVICE);
	// for (RunningServiceInfo service : manager
	// .getRunningServices(Integer.MAX_VALUE)) {
	// if ("app.mtproject.CallsLoggingService".equals(service.service
	// .getClassName())) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private boolean isSmsServiceRunning() {
	// ActivityManager manager = (ActivityManager)
	// getSystemService(ACTIVITY_SERVICE);
	// for (RunningServiceInfo service : manager
	// .getRunningServices(Integer.MAX_VALUE)) {
	// if ("app.mtproject.SmsLoggingService".equals(service.service
	// .getClassName())) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private boolean isLocationServiceRunning() {
	// ActivityManager manager = (ActivityManager)
	// getSystemService(ACTIVITY_SERVICE);
	// for (RunningServiceInfo service : manager
	// .getRunningServices(Integer.MAX_VALUE)) {
	// if ("app.mtproject.LocationLoggingService".equals(service.service
	// .getClassName())) {
	// return true;
	// }
	// }
	// return false;
	// }
}