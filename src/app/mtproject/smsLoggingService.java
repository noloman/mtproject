package app.mtproject;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class smsLoggingService extends Service {
	private static final String TAG = smsLoggingService.class.getSimpleName();
	private final IBinder binder = new MyBinder();
	private Timer updateTimer;
	
	public class MyBinder extends Binder {
		smsLoggingService getService() {
			return smsLoggingService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	
	@Override
	public void onCreate() {
		updateTimer = new Timer("smsLoggingTimer");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//TODO: Launch a background thread to do processing.
		updateTimer.cancel();
		updateTimer = new Timer("smsLoggingTimer");
		updateTimer.scheduleAtFixedRate(doRefresh, 0, 60*60*1000);
		return Service.START_STICKY;
	}
	
	private TimerTask doRefresh = new TimerTask() {
		public void run() {
			Log.i(TAG, "Service working");
			//sendDataToServer();
			// Function to send the users logging to the MySQL DB
		}
	};
}