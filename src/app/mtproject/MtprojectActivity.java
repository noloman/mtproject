package app.mtproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MtprojectActivity extends Activity {
	/** Called when the activity is first created. */

	boolean callsCheckbox;
	boolean smsCheckbox;
	boolean locationCheckbox;
	String ListPreference, editTextPreference, ringtonePreference,
			secondEditTextPreference, customPref, date, duration, type,
			bodyColumn, addressColumn, dateColumn;
	
	private CallsLoggingService callsService;
	private SmsLoggingService smsService;
	private LocationLoggingService locationService;
	private ServiceConnection callsLoggingConnection;
	private ServiceConnection smsLoggingConnection;
	private ServiceConnection locationLoggingConnection;
	public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
	public static final String KEY_PRIVATE = "KEY_PRIVATE";
	public static final String PREFS_NAME = "MyPrefsFile";

	private boolean startedCalls, startedSms, startedLocation = false;

	private RemoteSmsLoggingServiceConnection SmsLoggingConn = null;
	private RemoteCallsLoggingServiceConnection CallsLoggingConn = null;
	private RemoteLocationLoggingServiceConnection LocationLoggingConn = null;

	private IMyRemoteCallsLoggingService callsLoggingService;
	private IMyRemoteSmsLoggingService smsLoggingService;
	private IMyRemoteLocationLoggingService locationLoggingService;
	
	public String username;
	private OnCheckedChangeListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		username = getIntent().getExtras().getString("username");
		ToggleButton callsLoggingBtn = (ToggleButton) findViewById(R.id.callsLoggingBtn);
		ToggleButton smsLoggingBtn = (ToggleButton) findViewById(R.id.smsLoggingBtn);
		ToggleButton locationLoggingBtn = (ToggleButton) findViewById(R.id.locationLogginBtn);

		/*
		 * We first check that the status of the running services in case the
		 * user has changed the focus of the app and set the status of the
		 * buttons accordings to the running services.
		 */

		if (isCallsServiceRunning()) {
			callsLoggingBtn.setChecked(true);
		}
		if (isSmsServiceRunning()) {
			smsLoggingBtn.setChecked(true);
		}
		if (isLocationServiceRunning()) {
			locationLoggingBtn.setChecked(true);
		}

		// TODO: Fix the following depending the lifecycles and the bind status
		// or invokation and that stuff

		callsLoggingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!startedCalls) {
					startCallsService();
					bindCallsService();
				} else {
					releaseCallsService();
					stopCallsService();
				}
			}
		});

		smsLoggingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!startedSms) {
					startSmsService();
					bindSmsService();
				} else {
					releaseSmsService();
					stopSmsService();
				}
			}
		});
		
		locationLoggingBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!startedLocation) {
					startLocationService();
					bindLocationService();
				} else {
					releaseLocationService();
					stopLocationService();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void startCallsService() {
		if (startedCalls) {
			Toast.makeText(MtprojectActivity.this, "Service already started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
			i.putExtra("username", username);
			startService(i);
			startedCalls = true;
			updateCallsServiceStatus();
			Log.d(getClass().getSimpleName(), "startService()");
		}
	}

	private void startSmsService() {
		if (startedSms) {
			Toast.makeText(MtprojectActivity.this, "Service already started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.SmsLoggingService");
			i.putExtra("username", username);
			startService(i);
			startedSms = true;
			updateSmsServiceStatus();
			Log.d(getClass().getSimpleName(), "startService()");
		}
	}
	
	private void startLocationService() {
		if (startedLocation) {
			Toast.makeText(MtprojectActivity.this, "Service already started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.LocationLoggingService");
			i.putExtra("username", username);
			startService(i);
			startedLocation = true;
			updateLocationServiceStatus();
			Log.d(getClass().getSimpleName(), "startService()");
		}
	}

	private void stopCallsService() {
		if (!startedCalls) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
			stopService(i);
			startedCalls = false;
			updateCallsServiceStatus();
			Log.d(getClass().getSimpleName(), "stopService()");
		}
	}

	private void stopSmsService() {
		if (!startedSms) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.SmsLoggingService");
			stopService(i);
			startedSms = false;
			updateSmsServiceStatus();
			Log.d(getClass().getSimpleName(), "stopService()");
		}
	}
	
	private void stopLocationService() {
		if (!startedLocation) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.LocationLoggingService");
			stopService(i);
			startedLocation = false;
			updateLocationServiceStatus();
			Log.d(getClass().getSimpleName(), "stopService()");
		}
	}

	private void bindCallsService() {
		if (CallsLoggingConn == null) {
			CallsLoggingConn = new RemoteCallsLoggingServiceConnection();
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
			i.putExtra("username", username);
			bindService(i, CallsLoggingConn, Context.BIND_AUTO_CREATE);
			updateCallsServiceStatus();
			Log.d(getClass().getSimpleName(), "bindService()");
		} else {
			Toast.makeText(MtprojectActivity.this,
					"Cannot bind - service already bound", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void bindSmsService() {
		if (smsLoggingConnection == null) {
			smsLoggingConnection = new RemoteSmsLoggingServiceConnection();
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.SmsLoggingService");
			bindService(i, smsLoggingConnection, Context.BIND_AUTO_CREATE);
			updateSmsServiceStatus();
			Log.d(getClass().getSimpleName(), "bindService()");
		} else {
			Toast.makeText(MtprojectActivity.this,
					"Cannot bind - service already bound", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	private void bindLocationService() {
		if (LocationLoggingConn == null) {
			LocationLoggingConn = new RemoteLocationLoggingServiceConnection();
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.LocationLoggingService");
			i.putExtra("username", username);
			bindService(i, LocationLoggingConn, Context.BIND_AUTO_CREATE);
			updateLocationServiceStatus();
			Log.d(getClass().getSimpleName(), "bindService()");
		} else {
			Toast.makeText(MtprojectActivity.this,
					"Cannot bind - service already bound", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void releaseCallsService() {
		if (CallsLoggingConn != null) {
			unbindService(CallsLoggingConn);
			CallsLoggingConn = null;
			updateCallsServiceStatus();
			Log.d(getClass().getSimpleName(), "releaseService()");
		} else {
			Toast.makeText(MtprojectActivity.this,
					"Cannot unbind - service not bound", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void releaseSmsService() {
		if (SmsLoggingConn != null) {
			unbindService(SmsLoggingConn);
			SmsLoggingConn = null;
			updateSmsServiceStatus();
			Log.d(getClass().getSimpleName(), "releaseService()");
		} else {
			Toast.makeText(MtprojectActivity.this,
					"Cannot unbind - service not bound", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	private void releaseLocationService() {
		if (LocationLoggingConn != null) {
			unbindService(LocationLoggingConn);
			LocationLoggingConn = null;
			updateLocationServiceStatus();
			Log.d(getClass().getSimpleName(), "releaseService()");
		} else {
			Toast.makeText(MtprojectActivity.this,
					"Cannot unbind - service not bound", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void invokeCallsService() {
		if (CallsLoggingConn == null) {
			Toast.makeText(MtprojectActivity.this,
					"Cannot invoke - service not bound", Toast.LENGTH_SHORT)
					.show();
		} else {
			try {
				callsLoggingService.dumpCallsLog();
				TextView t = (TextView) findViewById(R.id.notApplicable);
				t.setText("It worked!");
				Log.d(getClass().getSimpleName(), "invokeService()");
			} catch (RemoteException e) {
				Log.e(getClass().getSimpleName(), "RemoteException");
			}
		}
	}

	private void invokeSmsService() {
		if (SmsLoggingConn == null) {
			Toast.makeText(MtprojectActivity.this,
					"Cannot invoke - service not bound", Toast.LENGTH_SHORT)
					.show();
		} else {
			try {
				smsLoggingService.dumpSmsLog();
				TextView t = (TextView) findViewById(R.id.notApplicable);
				t.setText("Invoked sms service");
				Log.d(getClass().getSimpleName(), "invokeService()");
			} catch (RemoteException re) {
				Log.e(getClass().getSimpleName(), "RemoteException");
			}
		}
	}
	
	private void invokeLocationService() {
		if (LocationLoggingConn == null) {
			Toast.makeText(MtprojectActivity.this,
					"Cannot invoke - service not bound", Toast.LENGTH_SHORT)
					.show();
		} else {
			try {
				locationLoggingService.dumpLocationLog();
				TextView t = (TextView) findViewById(R.id.notApplicable);
				t.setText("It worked!");
				Log.d(getClass().getSimpleName(), "invokeService()");
			} catch (RemoteException e) {
				Log.e(getClass().getSimpleName(), "RemoteException");
			}
		}
	}

	class RemoteCallsLoggingServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className,
				IBinder boundService) {
			callsLoggingService = IMyRemoteCallsLoggingService.Stub.asInterface((IBinder) boundService);
			Log.d(getClass().getSimpleName(), "onServiceConnected()");
			invokeCallsService();
		}

		public void onServiceDisconnected(ComponentName className) {
			callsLoggingService = null;
			updateCallsServiceStatus();
			Log.d(getClass().getSimpleName(), "onServiceDisconnected");
		}
	};
	
	class RemoteLocationLoggingServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className,
				IBinder boundService) {
			locationLoggingService = IMyRemoteLocationLoggingService.Stub.asInterface((IBinder) boundService);
			Log.d(getClass().getSimpleName(), "onServiceConnected()");
			invokeLocationService();
		}

		public void onServiceDisconnected(ComponentName className) {
			locationLoggingService = null;
			updateLocationServiceStatus();
			Log.d(getClass().getSimpleName(), "onServiceDisconnected");
		}
	};

	class RemoteSmsLoggingServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className,
				IBinder boundService) {
			smsLoggingService = IMyRemoteSmsLoggingService.Stub
					.asInterface((IBinder) boundService);
			Log.d(getClass().getSimpleName(), "onServiceConnected()");
			invokeSmsService();
		}

		public void onServiceDisconnected(ComponentName className) {
			smsLoggingService = null;
			updateSmsServiceStatus();
			Log.d(getClass().getSimpleName(), "onServiceDisconnected");
		}
	};

	private void updateCallsServiceStatus() {
		String bindStatus = CallsLoggingConn == null ? "unbound" : "bound";
		String startStatus = startedCalls ? "started" : "not started";
		String statusText = "Service status: " + bindStatus + "," + startStatus;
		TextView t = (TextView) findViewById(R.id.serviceStatus);
		t.setText(statusText);
	}

	private void updateSmsServiceStatus() {
		String bindStatus = SmsLoggingConn == null ? "unbound" : "bound";
		String startStatus = startedSms ? "started" : "not started";
		String statusText = "Service status: " + bindStatus + "," + startStatus;
	}
	
	private void updateLocationServiceStatus() {
		String bindStatus = LocationLoggingConn == null ? "unbound" : "bound";
		String startStatus = startedLocation ? "started" : "not started";
		String statusText = "Service status: " + bindStatus + "," + startStatus;
		TextView t = (TextView) findViewById(R.id.serviceStatus);
		t.setText(statusText);
	}

	protected void onDestroyCallsService() {
		super.onDestroy();
		releaseCallsService();
		Log.d(getClass().getSimpleName(), "onDestroy()");
	}

	protected void onDestroySmsService() {
		super.onDestroy();
		releaseSmsService();
		Log.d(getClass().getSimpleName(), "onDestroy()");
	}

	private boolean isCallsServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("app.mtproject.CallsLoggingService".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isSmsServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("app.mtproject.SmsLoggingService".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isLocationServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("app.mtproject.LocationLoggingService".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}
}