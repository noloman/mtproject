package app.mtproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.util.Log;


public class CallsLoggingService extends Service {
	String date;
	String duration, type;
	Date durationDate;
	String user_id;
	public String username;

	private Handler serviceHandler;
	private Task myTask = new Task();
	
	Long frequency;
	SharedPreferences prefs;
	OnSharedPreferenceChangeListener listener;

	@Override
	public IBinder onBind(Intent i) {
		Log.d(getClass().getSimpleName(), "onBind()");
		username = i.getStringExtra("username");
		return myRemoteCallsServiceStub;
	}

	private IMyRemoteCallsLoggingService.Stub myRemoteCallsServiceStub = new IMyRemoteCallsLoggingService.Stub() {
		public void dumpCallsLog() throws RemoteException {
			CallsLoggingService.this.dumpCallsLog();
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(getClass().getSimpleName(), "onCreate()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		serviceHandler.removeCallbacks(myTask);
		serviceHandler = null;
		Log.d(getClass().getSimpleName(), "onDestroy()");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		username = intent.getStringExtra("username");
		super.onStart(intent, startId);
		serviceHandler = new Handler();
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		frequency = toLong(Integer.parseInt(prefs.getString(Preferences.SMS_FREQUENCY_PREF, "0")));
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				if (key.equals("SMS_FREQUENCY_PREF")) {
					frequency = toLong(Integer.parseInt(prefs.getString(Preferences.SMS_FREQUENCY_PREF, "0")));
				}
			}
		};
		prefs.registerOnSharedPreferenceChangeListener(listener);
		serviceHandler.postDelayed(myTask, frequency);
		Log.d(getClass().getSimpleName(), "onStart()");
	}

	class Task implements Runnable {
		public void run() {
			try {
				myRemoteCallsServiceStub.dumpCallsLog();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			serviceHandler.postDelayed(this, frequency);
			Log.i(getClass().getSimpleName(), "Calling the dumpCallsLog");
		}
	}

	private void retrieveUserId() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("username", username));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost("http://10.0.2.2/science/getUserId.php", postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (res.equals("1")) {
				Log.d(getClass().getSimpleName(),"Successfully retrieved user_id");
				user_id = res;
			} else {
				Log.d(getClass().getSimpleName(), "Error retrieving user_id");
			}
		} catch (Exception e) {
		}
	}

	private void sendData(String user_id) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("user_id", user_id));
		postParameters.add(new BasicNameValuePair("date", date));
		Log.d(date, "Inserted date");
		postParameters.add(new BasicNameValuePair("duration", duration));
		postParameters.add(new BasicNameValuePair("type", type));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://10.0.2.2/science/sendCallsData.php", postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (res.equals("1")) {
				Log.d(getClass().getSimpleName(), "Insertado en DB!");
			} else {
				Log.d(getClass().getSimpleName(), "Error insertando en la DB");
			}
		} catch (Exception e) {
		}
	}

	private void dumpCallsLog() {
		ContentResolver cr = getContentResolver();
		String columns[] = new String[] { CallLog.Calls.DATE,
				CallLog.Calls.DURATION, CallLog.Calls.TYPE };
		Uri mContacts = CallLog.Calls.CONTENT_URI;
		Cursor c = cr.query(mContacts, columns, // Which columns to return
				null, // WHERE clause; which rows to return(all rows)
				null, // WHERE clause selection arguments (none)
				CallLog.Calls.DEFAULT_SORT_ORDER // Order-by clause
				// (ascending by name)

				);
		if (c.moveToFirst()) {
			do {
				android.os.Debug.waitForDebugger();
				//date = c.getString(c.getColumnIndex(CallLog.Calls.DATE));
				android.os.Debug.waitForDebugger();
				try {
					date = create_datestring(c.getLong(c.getColumnIndex(CallLog.Calls.DATE)));
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
				duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
				type = c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
			} while (c.moveToNext());
		}
		retrieveUserId();
		sendData(user_id);
	}

	public static String create_datestring(Long date) throws java.text.ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		Date finalDate = new Date(date);
		return formatter.format(finalDate);
	}
	
	
	private Long toLong (int hours) {
		Long frequency = Long.valueOf((((hours*60)*60)*1000));
		return frequency;
	}
}
