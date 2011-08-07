package app.mtproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.util.Log;

//import app.mtproject.sai.IMyRemoteCallsLoggingService;

public class CallsLoggingService extends Service {
	String date, duration, type;
	Date durationDate;
	String user_id;
	public String username;

	private Handler serviceHandler;
	private Task myTask = new Task();

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
		serviceHandler.postDelayed(myTask, 1000L);
		Log.d(getClass().getSimpleName(), "onStart()");
	}

	class Task implements Runnable {
		public void run() {
			try {
				myRemoteCallsServiceStub.dumpCallsLog();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			serviceHandler.postDelayed(this, 5000L);
			Log.i(getClass().getSimpleName(), "Calling the dumpCallsLog");
		}
	}

	private void retrieveUserId() {
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

	private void sendData(String user_id) {
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("user_id", user_id));
		postParameters.add(new BasicNameValuePair("date", date));
		postParameters.add(new BasicNameValuePair("duration", duration));
		postParameters.add(new BasicNameValuePair("type", type));
		String response = null;
		try {
			android.os.Debug.waitForDebugger();
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
				// Get the field values
				try {
					date = create_datestring(c.getString(c.getColumnIndex(CallLog.Calls.DATE)));
				} catch (java.text.ParseException e1) {
					e1.printStackTrace();
				}
				duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION));
				type = c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
			} while (c.moveToNext());
		}
		retrieveUserId();
		sendData(user_id);
	}

	public static String create_datestring(String timestring)
			throws java.text.ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.US);
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
