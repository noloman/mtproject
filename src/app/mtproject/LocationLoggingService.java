package app.mtproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationLoggingService extends Service {
	static String latString;
	static String lngString;
	Double latitude, longitude;
	Date durationDate;
	String user_id;
	public String username;
	Context context;
	LocationManager lm;
	
	long frequency;
	SharedPreferences prefs;
	OnSharedPreferenceChangeListener listener;

	private Handler serviceHandler;
	private Task myTask = new Task();
	
	@Override
	public IBinder onBind(Intent i) {
		Log.d(getClass().getSimpleName(), "onBind()");
		username = i.getStringExtra("username");
		return myRemoteLocationServiceStub;
	}

	private IMyRemoteLocationLoggingService.Stub myRemoteLocationServiceStub = new IMyRemoteLocationLoggingService.Stub() {
		public void dumpLocationLog() throws RemoteException {
			LocationLoggingService.this.dumpLocationLog();
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		context = getBaseContext();
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
				myRemoteLocationServiceStub.dumpLocationLog();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			serviceHandler.postDelayed(this, frequency);
			Log.i(getClass().getSimpleName(), "Calling the dumpLocationLog");
		}
	}

	public void retrieveUserId() {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("username", username));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://10.0.2.2/science/getUserId.php", postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (!res.equals("0")) {
				Log.d(getClass().getSimpleName(),
						"Successfully retrieved user_id");
				user_id = res;
			} else {
				Log.d(getClass().getSimpleName(), "Error retrieving user_id");
			}
		} catch (Exception e) {
		}
	}

	protected void sendData(String user_id) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("user_id", user_id));
		postParameters.add(new BasicNameValuePair("latitude", latString));
		postParameters.add(new BasicNameValuePair("longitude", lngString));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost("http://10.0.2.2/science/sendLocationData.php",
					postParameters);
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
	
	private void dumpLocationLog() {
		new DumpLocationLog(context, latString, lngString).start();
		Log.d(latString, getClass().getSimpleName());
		Log.d(lngString, getClass().getSimpleName());
        retrieveUserId();
        sendData(user_id);
	}

	public static String create_datestring(String timestring)
			throws java.text.ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		Date dt = null;
		Calendar c = Calendar.getInstance();
		try {
			dt = sdf.parse("2011-03-01 17:55:15");
			c.setTime(dt);
			System.out.println(c.getTimeInMillis());
			System.out.println(dt.toString());
		} catch (ParseException e) {
			System.err.println("There's an error in the Date!");
		}
		return dt.toString();
	}
	
	
	private Long toLong (int hours) {
		Long frequency = Long.valueOf((((hours*60)*60)*1000));
		return frequency;
	}
}
