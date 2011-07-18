package app.mtproject;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class callsLoggingService extends Service {
	private final IBinder binder = new MyBinder();
	private Timer updateTimer;
	
	public class MyBinder extends Binder {
		callsLoggingService getService() {
			return callsLoggingService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		updateTimer = new Timer("callsLoggingTimer");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO: Launch a background thread to do processing.
		updateTimer.cancel();
		updateTimer = new Timer("callsLoggingTimer");
		updateTimer.scheduleAtFixedRate(doRefresh, 0, 60 * 60 * 1000);
		return Service.START_STICKY;
	}

	private TimerTask doRefresh = new TimerTask() {
		public void run() {
			// sendDataToServer();
			// Function to send the users logging to the MySQL DB
		}
	};
}
