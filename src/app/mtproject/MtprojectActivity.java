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
	String ListPreference, editTextPreference, ringtonePreference,
			secondEditTextPreference, customPref, date, duration, type,
			bodyColumn, addressColumn, dateColumn;
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
		setContentView(R.layout.main);
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
							Preferences.CALLS_FREQUENCY_PREF, "86400000"));

				} else if (key.equals("SMS_FREQUENCY_PREF")) {
					smsFrequencyUpdate = Integer.parseInt(prefs.getString(
							Preferences.SMS_FREQUENCY_PREF, "86400000"));

				} else if (key.equals("LOC_FREQUENCY_PREF")) {
					locationFrequencyUpdate = Integer.parseInt(prefs.getString(
							Preferences.LOCATION_FREQUENCY_PREF, "86400000"));
				}
			}
		};

		prefs.registerOnSharedPreferenceChangeListener(listener);

		/*
		 * We first check that the status of the running services in case the
		 * user has changed the focus of the app and set the status of the
		 * buttons accordings to the running services.
		 */

		// if (isCallsServiceRunning()) {
		// callsLoggingBtn.setChecked(true);
		// }
		// if (isSmsServiceRunning()) {
		// smsLoggingBtn.setChecked(true);
		// }
		// if (isLocationServiceRunning()) {
		// locationLoggingBtn.setChecked(true);
		// }

		// TODO: Fix the following depending the lifecycles and the bind status
		// or invokation and that stuff

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
		callsFrequencyUpdate = Integer.parseInt(prefs.getString(Preferences.CALLS_FREQUENCY_PREF, "86400000"));
		smsFrequencyUpdate = Integer.parseInt(prefs.getString(Preferences.SMS_FREQUENCY_PREF, "86400000"));
		locationFrequencyUpdate = Integer.parseInt(prefs.getString(Preferences.LOCATION_FREQUENCY_PREF, "86400000"));
	}

	private void savePreferences() {
		SharedPreferences activityPreferences = getPreferences(Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = activityPreferences.edit();
		editor.putInt(Preferences.CALLS_FREQUENCY_PREF, callsFrequencyUpdate);
		editor.putInt(Preferences.SMS_FREQUENCY_PREF, smsFrequencyUpdate);
		editor.putInt(Preferences.LOCATION_FREQUENCY_PREF, locationFrequencyUpdate);
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
			Intent i = new Intent(this, Preferences.class);
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
			Toast.makeText(MtprojectActivity.this, "Service not yet started", Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(getBaseContext(), LoggingService.class);
			i.putExtra("serviceToStop", "calls");
			LoggingService.sendWakefulWork(getApplicationContext(), i);
			//startService(i);
			startedCalls = false;
		}
	}

	private void startSmsLogging() {
		Intent i = new Intent(getBaseContext(), LoggingService.class);
		i.putExtra("serviceToStart", "sms");
		i.putExtra("username", username);
		LoggingService.sendWakefulWork(getApplicationContext(), i); 
		//startService(i);
		startedSms = true;
	}
	
	private void stopSmsLogging() {
		if (!startedSms) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started", Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(getBaseContext(), LoggingService.class);
			i.putExtra("serviceToStop", "sms");
			LoggingService.sendWakefulWork(getApplicationContext(), i);
			//startService(i);
			startedSms = false;
		}
	}

	private void startLocationLogging() {
		Intent i = new Intent(getBaseContext(), LoggingService.class);
		i.putExtra("serviceToStart", "location");
		i.putExtra("username", username);
		LoggingService.sendWakefulWork(getApplicationContext(), i); 
		//startService(i);
		startedLocation = true;
	}
	
	private void stopLocationLogging() {
		if (!startedLocation) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started", Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(getBaseContext(), LoggingService.class);
			i.putExtra("serviceToStop", "location");
			LoggingService.sendWakefulWork(getApplicationContext(), i);
			//startService(i);
			startedLocation = false;
		}
	}

	// private void startCallsService() {
	// if (startedCalls) {
	// Toast.makeText(MtprojectActivity.this, "Service already started",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Intent i = new Intent();
	// i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
	// i.putExtra("username", username);
	// i.putExtra("frequency", callsFrequencyUpdate);
	// startService(i);
	// startedCalls = true;
	// updateCallsServiceStatus();
	// Log.d(getClass().getSimpleName(), "startService()");
	// }
	// }
	//
	// private void startSmsService() {
	// if (startedSms) {
	// Toast.makeText(MtprojectActivity.this, "Service already started",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Intent i = new Intent();
	// i.setClassName("app.mtproject", "app.mtproject.SmsLoggingService");
	// i.putExtra("username", username);
	// i.putExtra("frequency", smsFrequencyUpdate);
	// startService(i);
	// startedSms = true;
	// updateSmsServiceStatus();
	// Log.d(getClass().getSimpleName(), "startService()");
	// }
	// }
	//
	// private void startLocationService() {
	// if (startedLocation) {
	// Toast.makeText(MtprojectActivity.this, "Service already started",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Intent i = new Intent();
	// i.setClassName("app.mtproject",
	// "app.mtproject.LocationLoggingService");
	// i.putExtra("username", username);
	// i.putExtra("frequency", locationFrequencyUpdate);
	// startService(i);
	// startedLocation = true;
	// updateLocationServiceStatus();
	// Log.d(getClass().getSimpleName(), "startService()");
	// }
	// }
	//
	// private void stopCallsService() {
	// if (!startedCalls) {
	// Toast.makeText(MtprojectActivity.this, "Service not yet started",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Intent i = new Intent();
	// i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
	// stopService(i);
	// startedCalls = false;
	// updateCallsServiceStatus();
	// Log.d(getClass().getSimpleName(), "stopService()");
	// }
	// }
	//
	// private void stopSmsService() {
	// if (!startedSms) {
	// Toast.makeText(MtprojectActivity.this, "Service not yet started",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Intent i = new Intent();
	// i.setClassName("app.mtproject", "app.mtproject.SmsLoggingService");
	// stopService(i);
	// startedSms = false;
	// updateSmsServiceStatus();
	// Log.d(getClass().getSimpleName(), "stopService()");
	// }
	// }
	//
	// private void stopLocationService() {
	// if (!startedLocation) {
	// Toast.makeText(MtprojectActivity.this, "Service not yet started",
	// Toast.LENGTH_SHORT).show();
	// } else {
	// Intent i = new Intent();
	// i.setClassName("app.mtproject",
	// "app.mtproject.LocationLoggingService");
	// stopService(i);
	// startedLocation = false;
	// updateLocationServiceStatus();
	// Log.d(getClass().getSimpleName(), "stopService()");
	// }
	// }
	//
	// private void bindCallsService() {
	// if (CallsLoggingConn == null) {
	// CallsLoggingConn = new RemoteCallsLoggingServiceConnection();
	// Intent i = new Intent();
	// i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
	// i.putExtra("username", username);
	// bindService(i, CallsLoggingConn, Context.BIND_AUTO_CREATE);
	// updateCallsServiceStatus();
	// Log.d(getClass().getSimpleName(), "bindService()");
	// } else {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot bind - service already bound", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// private void bindSmsService() {
	// if (SmsLoggingConn == null) {
	// SmsLoggingConn = new RemoteSmsLoggingServiceConnection();
	// Intent i = new Intent();
	// i.setClassName("app.mtproject", "app.mtproject.SmsLoggingService");
	// bindService(i, SmsLoggingConn, Context.BIND_AUTO_CREATE);
	// updateSmsServiceStatus();
	// Log.d(getClass().getSimpleName(), "bindService()");
	// } else {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot bind - service already bound", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// private void bindLocationService() {
	// if (LocationLoggingConn == null) {
	// LocationLoggingConn = new RemoteLocationLoggingServiceConnection();
	// Intent i = new Intent();
	// i.setClassName("app.mtproject",
	// "app.mtproject.LocationLoggingService");
	// i.putExtra("username", username);
	// bindService(i, LocationLoggingConn, Context.BIND_AUTO_CREATE);
	// updateLocationServiceStatus();
	// Log.d(getClass().getSimpleName(), "bindService()");
	// } else {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot bind - service already bound", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// private void releaseCallsService() {
	// if (CallsLoggingConn != null) {
	// unbindService(CallsLoggingConn);
	// CallsLoggingConn = null;
	// updateCallsServiceStatus();
	// Log.d(getClass().getSimpleName(), "releaseService()");
	// } else {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot unbind - service not bound", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// private void releaseSmsService() {
	// if (SmsLoggingConn != null) {
	// unbindService(SmsLoggingConn);
	// SmsLoggingConn = null;
	// updateSmsServiceStatus();
	// Log.d(getClass().getSimpleName(), "releaseService()");
	// } else {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot unbind - service not bound", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// private void releaseLocationService() {
	// if (LocationLoggingConn != null) {
	// unbindService(LocationLoggingConn);
	// LocationLoggingConn = null;
	// updateLocationServiceStatus();
	// Log.d(getClass().getSimpleName(), "releaseService()");
	// } else {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot unbind - service not bound", Toast.LENGTH_SHORT)
	// .show();
	// }
	// }
	//
	// private void invokeCallsService() {
	// if (CallsLoggingConn == null) {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot invoke - service not bound", Toast.LENGTH_SHORT)
	// .show();
	// } else {
	// try {
	// callsLoggingService.dumpCallsLog();
	// TextView t = (TextView) findViewById(R.id.notApplicable);
	// t.setText("It worked!");
	// Log.d(getClass().getSimpleName(), "invokeService()");
	// } catch (RemoteException e) {
	// Log.e(getClass().getSimpleName(), "RemoteException");
	// }
	// }
	// }
	//
	// private void invokeSmsService() {
	// if (SmsLoggingConn == null) {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot invoke - service not bound", Toast.LENGTH_SHORT)
	// .show();
	// } else {
	// try {
	// smsLoggingService.dumpSmsLog();
	// TextView t = (TextView) findViewById(R.id.notApplicable);
	// t.setText("Invoked sms service");
	// Log.d(getClass().getSimpleName(), "invokeService()");
	// } catch (RemoteException re) {
	// Log.e(getClass().getSimpleName(), "RemoteException");
	// }
	// }
	// }
	//
	// private void invokeLocationService() {
	// if (LocationLoggingConn == null) {
	// Toast.makeText(MtprojectActivity.this,
	// "Cannot invoke - service not bound", Toast.LENGTH_SHORT)
	// .show();
	// } else {
	// try {
	// locationLoggingService.dumpLocationLog();
	// TextView t = (TextView) findViewById(R.id.notApplicable);
	// t.setText("It worked!");
	// Log.d(getClass().getSimpleName(), "invokeService()");
	// } catch (RemoteException e) {
	// Log.e(getClass().getSimpleName(), "RemoteException");
	// }
	// }
	// }
	//
	// class RemoteCallsLoggingServiceConnection implements ServiceConnection {
	// public void onServiceConnected(ComponentName className,
	// IBinder boundService) {
	// callsLoggingService = IMyRemoteCallsLoggingService.Stub
	// .asInterface((IBinder) boundService);
	// Log.d(getClass().getSimpleName(), "onServiceConnected()");
	// invokeCallsService();
	// }
	//
	// public void onServiceDisconnected(ComponentName className) {
	// callsLoggingService = null;
	// updateCallsServiceStatus();
	// Log.d(getClass().getSimpleName(), "onServiceDisconnected");
	// }
	// };
	//
	// class RemoteLocationLoggingServiceConnection implements ServiceConnection
	// {
	// public void onServiceConnected(ComponentName className,
	// IBinder boundService) {
	// locationLoggingService = IMyRemoteLocationLoggingService.Stub
	// .asInterface((IBinder) boundService);
	// Log.d(getClass().getSimpleName(), "onServiceConnected()");
	// invokeLocationService();
	// }
	//
	// public void onServiceDisconnected(ComponentName className) {
	// locationLoggingService = null;
	// updateLocationServiceStatus();
	// Log.d(getClass().getSimpleName(), "onServiceDisconnected");
	// }
	// };
	//
	// class RemoteSmsLoggingServiceConnection implements ServiceConnection {
	// public void onServiceConnected(ComponentName className,
	// IBinder boundService) {
	// smsLoggingService = IMyRemoteSmsLoggingService.Stub
	// .asInterface((IBinder) boundService);
	// Log.d(getClass().getSimpleName(), "onServiceConnected()");
	// invokeSmsService();
	// }
	//
	// public void onServiceDisconnected(ComponentName className) {
	// smsLoggingService = null;
	// updateSmsServiceStatus();
	// Log.d(getClass().getSimpleName(), "onServiceDisconnected");
	// }
	// };
	//
	// private void updateCallsServiceStatus() {
	// String bindStatus = CallsLoggingConn == null ? "unbound" : "bound";
	// String startStatus = startedCalls ? "started" : "not started";
	// String statusText = "Service status: " + bindStatus + "," + startStatus;
	// TextView t = (TextView) findViewById(R.id.serviceStatus);
	// t.setText(statusText);
	// }
	//
	// private void updateSmsServiceStatus() {
	// String bindStatus = SmsLoggingConn == null ? "unbound" : "bound";
	// String startStatus = startedSms ? "started" : "not started";
	// String statusText = "Service status: " + bindStatus + "," + startStatus;
	// }
	//
	// private void updateLocationServiceStatus() {
	// String bindStatus = LocationLoggingConn == null ? "unbound" : "bound";
	// String startStatus = startedLocation ? "started" : "not started";
	// String statusText = "Service status: " + bindStatus + "," + startStatus;
	// TextView t = (TextView) findViewById(R.id.serviceStatus);
	// t.setText(statusText);
	// }
	//
	// protected void onDestroyCallsService() {
	// super.onDestroy();
	// releaseCallsService();
	// Log.d(getClass().getSimpleName(), "onDestroy()");
	// }
	//
	// protected void onDestroySmsService() {
	// super.onDestroy();
	// releaseSmsService();
	// Log.d(getClass().getSimpleName(), "onDestroy()");
	// }
	//
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