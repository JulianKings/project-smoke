package com.smoke.location;


import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.smoke.core.Environment;

public class LocationUpdater implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        Environment.updateLastKnownLocation(location);
    }
}
