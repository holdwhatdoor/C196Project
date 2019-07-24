package com.example.c196project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    static int notificationID;

    String channelId = "test";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
 //       Toast.makeText("Notification", "Text", Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channelId);

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createNotificationChannel(Context context, String channelId) {

  //      if(Build.VERSION_SDK_INT>=Build.VERSION)

    }
}
