package com.example.c196project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Notifications extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        int notificationId =

 /**       private void createNotificationChannels(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel startChannel = new NotificationChannel(
                        COURSE_START_CH,
                        "Channel 1",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                startChannel.setDescription("This is channel 1");

                NotificationChannel endChannel = new NotificationChannel(
                        COURSE_END_CH,
                        "Channel 2",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                endChannel.setDescription("This is channel 2");

                NotificationChannel dueChannel = new NotificationChannel(
                        ASSESS_DUE_CH,
                        "Channel 3",
                        NotificationManager.IMPORTANCE_DEFAULT
                );


                /**     NotificationManager manager = getSystemService(NotificationManager.class);
                 manager.createNotificationChannel(startChannel);
                 manager.createNotificationChannel(endChannel);
                 manager.createNotificationChannel(dueChannel);
                 */
            }
        }


/**        private boolean isChannelBlocked(String channelId){
            NotificationManager manager = getSystemService(NotificationManager.class);
            NotificationChannel channel = manager.getNotificationChannel(channelId);

        }
    }
}*/
