package com.MarcoPolitakisGmailCom.GivtekAndroid9Cl;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    private static boolean activityVisible;
    private BeaconManager beaconManager;
    private String beaconKey;

    @Override
    public void onCreate() {
        super.onCreate();

        EstimoteSDK.initialize(getApplicationContext(), "givtek-android-9cl", "337d12f80f75aea3b51d3b728548b87b");

        beaconManager = new BeaconManager(getApplicationContext());
        beaconKey = null;

        // Set values for scanning and waiting time.
        beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(5), TimeUnit.SECONDS.toMillis(5));

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
                if (!list.isEmpty()) {
                    Beacon beacon = list.get(0);
                    beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
                    regionEntered();
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                beaconKey = null;
                regionExited();
            }
        });
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public String getBeaconKey() {
        return beaconKey;
    }

    public void regionEntered() {

        if (isActivityVisible())
        {
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
        else
        {
            showNotification("Givtek - Donation Nearby", beaconKey);
        }
    }

    public void regionExited() {

        if (isActivityVisible())
        {
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

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, DonateActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
