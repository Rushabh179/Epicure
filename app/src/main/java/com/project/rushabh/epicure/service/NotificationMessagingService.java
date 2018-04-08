package com.project.rushabh.epicure.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by rushabh.modi on 03/04/18.
 */

public class NotificationMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.e("aaaaaaaaaaaaaaaaa", "Sent");
    }
}
