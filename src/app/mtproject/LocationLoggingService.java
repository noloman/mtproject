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
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class LocationLoggingService extends Service {
	String latString, lngString;
	Double latitude, longitude;
	Date durationDate;
	String user_id;
	public String username;
	Context context;
	LocationManager lm;

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
		serviceHandler.postDelayed(myTask, 1000L);
		Log.d(getClass().getSimpleName(), "onStart()");
	}

	class Task implements Runnable {
		public void run() {
			try {
				myRemoteLocationServiceStub.dumpLocationLog();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			serviceHandler.postDelayed(this, 5000L);
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
		android.os.Debug.waitForDebugger();
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
		android.os.Debug.waitForDebugger();
		new DumpLocationLog(context).start();
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
}
