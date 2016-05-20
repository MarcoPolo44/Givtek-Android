package com.MarcoPolitakisGmailCom.GivtekAndroid9Cl;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;

/**
 * Created by Marco on 5/19/2016.
 */
public class DonateActivity extends Activity {
    TextView charityText;
    ImageView charityImage;
    Button donateButton;
    Uri charityUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_layout);
        charityText = (TextView) findViewById(R.id.charityText);
        charityImage =(ImageView) findViewById(R.id.charityImage);
        donateButton = (Button) findViewById(R.id.donateButton);

        displayCharity();
    }

    public void displayCharity() {
        MyApplication context = ((MyApplication) getApplicationContext());
        context.getBeaconKey();

        charityUri = null;

        if (context.getBeaconKey().equals("33211:2659"))
        {
            charityText.setText("Ronald McDonald House Charities®");
            charityImage.setImageResource(R.drawable.rmhcnz);
            charityUri = Uri.parse("http://nokket.com/");

            showNotification(
                    "Givtek - Donation Nearby",
                    context.getBeaconKey());
        }

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_VIEW, charityUri);
                startActivity(intent);
            }
        });
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