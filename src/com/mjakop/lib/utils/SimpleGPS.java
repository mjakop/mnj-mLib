package com.mjakop.lib.utils;

import java.util.Hashtable;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

class SimpleGPSInternalListener implements LocationListener {
	
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	private SimpleGPS parent;
	private Location bestLocation;
	
	public SimpleGPSInternalListener(SimpleGPS parent) {
		this.parent = parent;
	}
	
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	public void onLocationChanged(Location location) {
		if (parent.listener != null && location!=null){
			if (isBetterLocation(location, bestLocation)) {
				bestLocation = location;
			}
			if (parent.listener != null && bestLocation != null){
				parent.listener.locationKnown(bestLocation);
			}
		}
	}
	
	public Location getLocation() {
		return bestLocation;
	}

	public void onProviderDisabled(String provider) {
		
	}

	public void onProviderEnabled(String provider) {
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
	
}

public class SimpleGPS{

	private Context context;
	private boolean useGPS;
	private LocationManager locationManager;
	private SimpleGPSInternalListener internalListener = new SimpleGPSInternalListener(this);
	protected SimpleGPSListener listener;
	
	public SimpleGPS(Context context, boolean useGPS, SimpleGPSListener listener) {
		this.context = context;
		this.useGPS = useGPS;
		this.listener = listener;
	}
	
	private LocationManager getLocationManager(){
		if (locationManager == null) {
			locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		}
		return locationManager;
	}
	
	public void start() {
		getLocationManager().requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, internalListener);
		Location lastKnownLocationNP = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		internalListener.onLocationChanged(lastKnownLocationNP);
		if (useGPS) {
			getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, internalListener);
			Location lastKnownLocationGP = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			internalListener.onLocationChanged(lastKnownLocationGP);
		}
	}
	
	public void stop() {
		getLocationManager().removeUpdates(internalListener);
	}
	
	public Location getLocation(){
		return internalListener.getLocation();
	}



}
