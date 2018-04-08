package com.project.rushabh.restaurant.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.rushabh.restaurant.activity.MainActivity;

/**
 * Created by rushabh.modi on 03/04/18.
 */

public class NotificationMessagingService extends FirebaseMessagingService {

    private NotificationCompat.Builder pushNotificationBuilder;
    private NotificationManagerCompat pushNotificationManager;
    private PendingIntent pushPendingIntent;
    private Intent intent;
    private int uniqueID = 99;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("aaaaaaaaaaaaaaaaaa", "message received");

        intent = new Intent(this, MainActivity.class);
        pushPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        assert remoteMessage.getNotification() != null;

        pushNotificationBuilder = new NotificationCompat.Builder(this, "com.project.rushabh.restaurant.service");
        pushNotificationBuilder.setAutoCancel(true)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setTicker("This is the ticker")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                .setContentTitle(remoteMessage.getFrom())
                .setContentText(remoteMessage.getData().get("message"))
                .addAction(android.R.drawable.arrow_up_float, "Open", pushPendingIntent)
                .setContentIntent(pushPendingIntent);

        //Issue notification
        pushNotificationManager = NotificationManagerCompat.from(this);
        pushNotificationManager.notify(uniqueID, pushNotificationBuilder.build());
    }
}
