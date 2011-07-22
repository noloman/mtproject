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
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.mtproject.sai.*;

public class MtprojectActivity extends Activity implements OnSharedPreferenceChangeListener {
	/** Called when the activity is first created. */

	boolean callsCheckbox;
	boolean smsCheckbox;
	boolean locationCheckbox;
	String ListPreference, editTextPreference, ringtonePreference,
			secondEditTextPreference, customPref, date, duration, type,
			bodyColumn, addressColumn, dateColumn;
	private CallsLoggingService callsService;
	private SmsLoggingService smsService;
	private ServiceConnection callsLoggingConnection;
	private ServiceConnection smsLoggingConnection;
	public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
	public static final String KEY_PRIVATE = "KEY_PRIVATE";
	private SharedPreferences prefsPrivate;
	public static final String PREFS_NAME = "MyPrefsFile";

	private boolean started = false;

	private RemoteSmsLoggingServiceConnection SmsLoggingConn = null;
	private RemoteCallsLoggingServiceConnection CallsLoggingConn = null;

	private IMyRemoteCallsLoggingService callsLoggingService;
	private IMyRemoteSmsLoggingService smsLoggingService;
	public String username;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		username = getIntent().getExtras().getString("username");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // register preference change listener
        prefs.registerOnSharedPreferenceChangeListener(this);
		setContentView(R.layout.main);
		retrievePreferences();

		Button prefBtn = (Button) findViewById(R.id.prefsBtn);
//		Button start = (Button)findViewById(R.id.startButton);
//        Button stop = (Button)findViewById(R.id.stopButton);
//        Button bind = (Button)findViewById(R.id.bindButton);
//        Button release = (Button)findViewById(R.id.releaseButton);
//        Button invoke = (Button)findViewById(R.id.invokeButton);
//        
//        start.setOnClickListener(new OnClickListener() {
//        	public void onClick(View v){
//        		startCallsService();
//        	}
//        });
//        
//        stop.setOnClickListener(new OnClickListener() {
//        	public void onClick(View v){
//        		stopCallsService();
//        	}
//        });       
//        
//        bind.setOnClickListener(new OnClickListener() {
//        	public void onClick(View v){
//        		bindCallsService();
//        	}
//        });  
//        
//        release.setOnClickListener(new OnClickListener() {
//        	public void onClick(View v){
//        		releaseCallsService();
//        	}
//        });          
//        
//        invoke.setOnClickListener(new OnClickListener() {
//        	public void onClick(View v){
//        		invokeCallsService();
//        	}
//        });          

		prefBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Explicit intent to call the preferences
				Intent preferencesActivity = new Intent(getBaseContext(),Preferences.class);
				startActivity(preferencesActivity);
			}
		});
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("callsLogChk")) {
			if (callsCheckbox)
			{
				startCallsService();
				bindCallsService();
			}
			else {
				releaseCallsService();
				stopCallsService();
			}
		}
	}

	private void retrievePreferences() {
		// Get the xml/preferences.xml preferences
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		callsCheckbox = prefs.getBoolean("callsLogChk", false);
		smsCheckbox = prefs.getBoolean("smsLogChk", false);
		locationCheckbox = prefs.getBoolean("locationLogChk", false);
		
		prefs.registerOnSharedPreferenceChangeListener(this);

//		if (callsCheckbox) {
//			startCallsService();
//			bindCallsService();
//		}
//		if (smsCheckbox) {
//
//		}
	}

	public void startCallsService() {
		if (started) {
			Toast.makeText(MtprojectActivity.this, "Service already started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
			i.putExtra("username", username);
			startService(i);
			started = true;
			updateCallsServiceStatus();
			Log.d(getClass().getSimpleName(), "startService()");
		}
	}

	private void startSmsService() {
		if (started) {
			Toast.makeText(MtprojectActivity.this, "Service already started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.smsLoggingService");
			startService(i);
			started = true;
			updateSmsServiceStatus();
			Log.d(getClass().getSimpleName(), "startService()");
		}
	}

	private void stopCallsService() {
		if (!started) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.CallsLoggingService");
			stopService(i);
			started = false;
			updateCallsServiceStatus();
			Log.d(getClass().getSimpleName(), "stopService()");
		}
	}

	private void stopSmsService() {
		if (!started) {
			Toast.makeText(MtprojectActivity.this, "Service not yet started",
					Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.smsLoggingService");
			stopService(i);
			started = false;
			updateSmsServiceStatus();
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
					"Cannot bind - service already bound", Toast.LENGTH_SHORT).show();
		}
	}

	private void bindSmsService() {
		if (smsLoggingConnection == null) {
			smsLoggingConnection = new RemoteSmsLoggingServiceConnection();
			Intent i = new Intent();
			i.setClassName("app.mtproject", "app.mtproject.smsLoggingService");
			bindService(i, smsLoggingConnection, Context.BIND_AUTO_CREATE);
			updateCallsServiceStatus();
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

	class RemoteSmsLoggingServiceConnection implements ServiceConnection {
		public void onServiceConnected(ComponentName className,
				IBinder boundService) {
			smsLoggingService = IMyRemoteSmsLoggingService.Stub
					.asInterface((IBinder) boundService);
			Log.d(getClass().getSimpleName(), "onServiceConnected()");
		}

		public void onServiceDisconnected(ComponentName className) {
			smsLoggingService = null;
			updateSmsServiceStatus();
			Log.d(getClass().getSimpleName(), "onServiceDisconnected");
		}
	};

	private void updateCallsServiceStatus() {
		String bindStatus = CallsLoggingConn == null ? "unbound" : "bound";
		String startStatus = started ? "started" : "not started";
		String statusText = "Service status: " + bindStatus + "," + startStatus;
		TextView t = (TextView) findViewById(R.id.serviceStatus);
		t.setText(statusText);
	}

	private void updateSmsServiceStatus() {
		String bindStatus = SmsLoggingConn == null ? "unbound" : "bound";
		String startStatus = started ? "started" : "not started";
		String statusText = "Service status: " + bindStatus + "," + startStatus;
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
}