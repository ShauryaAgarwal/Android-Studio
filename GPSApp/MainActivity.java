package com.example.gpsproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView latitudeTextView, longitudeTextView, addressTextView, distanceTextView, timeTextView;
    long elapsedTimeStart = System.currentTimeMillis();
    float locationManagerMinDistance, distanceTotalMeters;
    double locationLatitude, locationLongitude, distanceTotalMiles;
    Geocoder geocoder;
    List<Address> addressList;
    Location currentLocation = null;
    Location prevLocation = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = findViewById(R.id.id_latitudeTextView);
        longitudeTextView = findViewById(R.id.id_longitudeTextView);
        addressTextView = findViewById(R.id.id_addressTextView);
        distanceTextView = findViewById(R.id.id_distanceTextView);
        timeTextView = findViewById(R.id.id_timeTextView);

        locationManagerMinDistance = 10;
        distanceTotalMeters = 0;

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }
        else
            Log.d("TAG", "Permissions already granted");

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, locationManagerMinDistance, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationLatitude = location.getLatitude();
        locationLongitude = location.getLongitude();
        latitudeTextView.setText("Location Latitude: " + Double.toString(locationLatitude));
        longitudeTextView.setText("Location Longitude: " + Double.toString(locationLongitude));
        geocoder = new Geocoder(this, Locale.US);
        try {
            addressList = geocoder.getFromLocation(locationLatitude, locationLongitude, 1);
            String address = addressList.get(0).getAddressLine(0);
            addressTextView.setText("Location Address: " + address);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG_1", e.toString());
        }
        prevLocation = currentLocation;
        currentLocation = location;
        if(prevLocation != null)
        {
            distanceTotalMeters += currentLocation.distanceTo(prevLocation);
            distanceTotalMiles = Math.round((distanceTotalMeters/1609.34)*100.0)/100.0;
            distanceTextView.setText("Total Distance Travelled: " + String.valueOf(distanceTotalMiles) + " miles");
        }
        double elapsedTime = (System.currentTimeMillis() - elapsedTimeStart)/1000.0;
        timeTextView.setText("Time Spent Travelling: " + String.valueOf(elapsedTime) + " seconds");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}