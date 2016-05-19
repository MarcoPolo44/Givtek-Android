package com.MarcoPolitakisGmailCom.GivtekAndroid9Cl;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application {

    private BeaconManager beaconManager;

    @Override
    public void onCreate() {
        super.onCreate();

        EstimoteSDK.initialize(getApplicationContext(), "givtek-android-9cl", "337d12f80f75aea3b51d3b728548b87b");

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "Givtek Region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        null, null));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                regionEntered();
            }

            @Override
            public void onExitedRegion(Region region) {
                regionExited();
            }
        });
    }

    public void regionEntered() {
        Toast.makeText(this, "onEnteredRegion", Toast.LENGTH_LONG).show();

        // Explicit intent to wrap
        Intent intent = new Intent(this, DonateActivity.class);

        // Create pending intent and wrap our intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        try {
            // Perform the operation associated with our pendingIntent
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    public void regionExited() {
        Toast.makeText(this, "onExitedRegion", Toast.LENGTH_LONG).show();

        // Explicit intent to wrap
        Intent intent = new Intent(this, MainActivity.class);

        // Create pending intent and wrap our intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        try {
            // Perform the operation associated with our pendingIntent
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
