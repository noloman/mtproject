package app.mtproject;

import java.util.ArrayList;

import android.content.Context;
import android.location.LocationManager;
import android.os.Looper;

public class DumpLocationLog extends Thread {
	LocationManager lm;
	LocationHelper loc;

	public DumpLocationLog(Context context) {
		loc = new LocationHelper();
	    lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	}

	public void run() {
		Looper.prepare();
		android.os.Debug.waitForDebugger();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, loc);
		Looper.loop();
	}
}
