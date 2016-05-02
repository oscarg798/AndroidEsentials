package com.rm.androidesentials.model.providers;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.rm.androidesentials.model.interfaces.LocationProviderUtils;
import com.rm.androidesentials.utils.ErrorCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscargallon on 4/12/16.
 */
public class UserLocationProvider extends Service implements LocationListener {

    private LocationManager locationManager;

    private boolean canGetLocation = false;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    private static final long MIN_TIME_BW_UPDATES = 0;

    private Location userLocation;

    private List<LocationProviderUtils.onSubscribeforLocationUpdates> onSubscribeforLocationUpdatesList;

    private boolean isGettingLocationUpdates = false;

    public static UserLocationProvider UserLocationProvider;

    private Context context;

    private UserLocationProvider(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) this.context
                .getSystemService(LOCATION_SERVICE);
        onSubscribeforLocationUpdatesList = new ArrayList<>();

    }

    public static UserLocationProvider getLocationProvider(Context context) {
        if (UserLocationProvider == null) {
            UserLocationProvider = new UserLocationProvider(context);
        }
        return UserLocationProvider;
    }


    public void getUserLocaton(String provider, LocationProviderUtils.onGetLocation onGetLocation
            , LocationProviderUtils.onSubscribeforLocationUpdates
                                       onSubscribeforLocationUpdates) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        if (locationManager == null) {
            onGetLocation.errorGettingLocation(ErrorCodes.LOCATION_MANAGER_UNAVALAIBLE);
        }


        getLocationUpdates(onSubscribeforLocationUpdates);


        switch (provider) {
            case LocationManager.GPS_PROVIDER: {
                if (!isGPSEnabled()) {
                    onGetLocation
                            .errorGettingLocation(ErrorCodes.GPS_PROVIDER_DISABLE);
                    isGettingLocationUpdates = true;
                    return;
                }

                break;
            }
            case LocationManager.NETWORK_PROVIDER: {
                if (!isNetworkEnabled()) {
                    onGetLocation.errorGettingLocation(ErrorCodes.NETWORK_PROVIDER_DISABLE);
                    return;
                }
                break;
            }
        }

        locationManager.requestLocationUpdates(
                provider, 0, 0, this);

        userLocation = locationManager.getLastKnownLocation(provider);


        if (userLocation != null) {
            onGetLocation.locationGot(userLocation);
        }

    }


    private void getLocationUpdates(LocationProviderUtils.onSubscribeforLocationUpdates onSubscribeforLocationUpdates) {
        if (this.onSubscribeforLocationUpdatesList == null) {
            this.onSubscribeforLocationUpdatesList = new ArrayList<>();
        }
        this.onSubscribeforLocationUpdatesList.add(onSubscribeforLocationUpdates);


    }

    private void unSuscribeLocationUpdates() {
        this.onSubscribeforLocationUpdatesList = null;
        if (this.locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(this);
        }
    }


    private boolean isGPSEnabled() {
        return locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isNetworkEnabled() {
        return locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (onSubscribeforLocationUpdatesList != null) {
            for (LocationProviderUtils.onSubscribeforLocationUpdates on : onSubscribeforLocationUpdatesList) {
                on.locationUpdateGot(location);
            }
        }
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                locationManager.removeUpdates(this);
                onSubscribeforLocationUpdatesList = null;
                unSuscribeLocationUpdates();
                userLocation = location;
                locationManager = null;
                isGettingLocationUpdates = false;
            }
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isGettingLocationUpdates() {
        return isGettingLocationUpdates;
    }

    public void setGettingLocationUpdates(boolean gettingLocationUpdates) {
        isGettingLocationUpdates = gettingLocationUpdates;
    }
}
