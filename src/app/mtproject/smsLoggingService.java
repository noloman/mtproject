package app.mtproject;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

public class SmsLoggingService extends Service {
	String dateColumn, bodyColumn, addressColumn;

	private Handler serviceHandler;
	private int counter;
	private Task myTask = new Task();

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(getClass().getSimpleName(), "onBind()");
		return myRemoteSmsServiceStub;
	}

	private IMyRemoteSmsLoggingService.Stub myRemoteSmsServiceStub = new IMyRemoteSmsLoggingService.Stub() {

		@Override
		public void dumpSmsLog() throws RemoteException {
			Cursor cursor = getContentResolver().query(
					Uri.parse("content://sms/"), null, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					dateColumn = cursor.getString(cursor.getColumnIndex("date"));
					bodyColumn = cursor.getString(cursor.getColumnIndex("body"));
					addressColumn = cursor.getString(cursor.getColumnIndex("address"));
				} while (cursor.moveToNext());
			}
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
}