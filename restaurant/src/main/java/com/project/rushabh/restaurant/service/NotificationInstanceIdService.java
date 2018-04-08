package com.project.rushabh.restaurant.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.project.rushabh.restaurant.R;

/**
 * Created by rushabh.modi on 03/04/18.
 */

public class NotificationInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = NotificationInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(getString(R.string.token_registration_complete));
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.e(TAG, "sendRegistrationToServer: " + refreshedToken);
    }

    private void storeRegIdInPref(String refreshedToken) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.spk_notification_token), refreshedToken);
        editor.apply();
    }
}
