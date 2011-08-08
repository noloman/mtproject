package app.mtproject;

import android.content.Context;
import android.location.LocationManager;
import android.os.Looper;

public class DumpLocationLog extends Thread {
	LocationManager lm;
	LocationHelper loc;
	String latString, lngString = null;

	public DumpLocationLog(Context context, String latString, String lngString) {
		loc = new LocationHelper();
	    lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	}

	public void run() {
		Looper.prepare();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, loc);
		//LocationLoggingService.latString = loc.latString;
		//lngString = loc.lngString;
		Looper.loop();
	}
}
