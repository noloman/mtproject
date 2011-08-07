package app.mtproject;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class LocationHelper implements LocationListener {
	public String latString, lngString;
	public Double latitude, longitude;

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			android.os.Debug.waitForDebugger();
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			latString = Double.toString(latitude);
			lngString = Double.toString(longitude);
			Log.d("Location: ", getClass().getSimpleName());
			Log.d(latString, getClass().getSimpleName());
			Log.d(lngString, getClass().getSimpleName());
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
}
