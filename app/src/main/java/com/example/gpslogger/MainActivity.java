package com.example.gpslogger;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.CellSignalStrength;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private TextView tvLatitude, tvLongitude, tvAltitude, tvSignalStrength, tvBatteryLevel;
    private LocationManager locationManager;
    private TelephonyManager telephonyManager;
    private File logFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvAltitude = findViewById(R.id.tvAltitude);
        tvSignalStrength = findViewById(R.id.tvSignalStrength);
        tvBatteryLevel = findViewById(R.id.tvBatteryLevel);

        Button btnChangeLanguage = findViewById(R.id.btnChangeLanguage);
        Button btnSaveToCSV = findViewById(R.id.btnSaveToCSV);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        logFile = new File(getExternalFilesDir(null), "LogTracking.csv");

        btnChangeLanguage.setOnClickListener(v -> changeLanguage());
        btnSaveToCSV.setOnClickListener(v -> {
            saveToCSV();
            showFileLocation(); // Afficher l'emplacement du fichier après l'enregistrement
        });


        // Vérifiez les permissions
        if (checkPermissions()) {
            requestLocationUpdates();
        } else {
            requestPermissions();
        }
    }

    private void requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "Permissions not granted for location access", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Permissions denied. App cannot function properly.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeLanguage() {
        Locale current = getResources().getConfiguration().locale;
        String newLanguage = current.getLanguage().equals("en") ? "fr" : current.getLanguage().equals("fr") ? "ar" : "en";
        Locale locale = new Locale(newLanguage);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Restart activity to apply changes
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void saveToCSV() {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.append(String.format(Locale.getDefault(), "%s,%s,%s,%s,%s\n",
                    tvLatitude.getText(),
                    tvLongitude.getText(),
                    tvAltitude.getText(),
                    tvSignalStrength.getText(),
                    tvBatteryLevel.getText()));
            Toast.makeText(this, "Data saved to CSV", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            int signalStrength = getSignalStrength();
            int batteryLevel = getBatteryLevel();

            tvLatitude.setText(getString(R.string.latitude)+ ": " + latitude);
            tvLongitude.setText(getString(R.string.longitude) + ": " + longitude);
            tvAltitude.setText(getString(R.string.altitude) + ": " + altitude);
            tvSignalStrength.setText(getString(R.string.signal_strength) + ": " + signalStrength);
            tvBatteryLevel.setText(getString(R.string.battery_level) + ": " + batteryLevel);
        }
    };

    private int getSignalStrength() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
        if (cellInfos != null && !cellInfos.isEmpty()) {
            CellSignalStrength cellSignalStrength = cellInfos.get(0).getCellSignalStrength();
            return cellSignalStrength.getDbm();
        }
        return 0;
    }

    private int getBatteryLevel() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, intentFilter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            return (int) ((level / (float) scale) * 100);
        }
        return -1;
    }

    private void showFileLocation() {
   /
        File logFileLocation = new File(getExternalFilesDir(null), "LogTracking.csv");


        Toast.makeText(this, "File saved at: " + logFileLocation.getAbsolutePath(), Toast.LENGTH_LONG).show();

        // Ou si vous voulez l'afficher dans un TextView, vous pouvez faire ceci :
        TextView tvFileLocation = findViewById(R.id.tvFileLocation); // Assurez-vous d'avoir un TextView dans votre layout
        tvFileLocation.setText("File saved at: " + logFileLocation.getAbsolutePath());
    }

}
