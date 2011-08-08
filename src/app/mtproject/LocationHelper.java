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
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			LocationLoggingService.latString = Double.toString(latitude);
			LocationLoggingService.lngString = Double.toString(longitude);
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
