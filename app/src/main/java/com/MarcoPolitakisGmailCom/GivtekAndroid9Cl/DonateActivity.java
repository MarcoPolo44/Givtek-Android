package com.MarcoPolitakisGmailCom.GivtekAndroid9Cl;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Marco on 5/19/2016.
 */
public class DonateActivity extends Activity {
    TextView charityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_layout);

        charityText = (TextView) findViewById(R.id.charity_id);
        displayCharity();
    }

    public void displayCharity() {
        MyApplication context = ((MyApplication) getApplicationContext());
        context.getBeaconKey();

        if (context.getBeaconKey().equals("33211:2659"))
        {
            charityText.setText("Ronald McDonald House Charities®");

            showNotification(
                    "Givtek - Donation Nearby",
                    context.getBeaconKey());
        }
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
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