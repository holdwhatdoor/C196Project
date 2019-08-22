package com.example.c196project.utilities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.c196project.R;

import java.util.Date;

public class Notifications extends BroadcastReceiver {

    public static final String TAG = ".Notifications";

    public static final String NOTIFICATION_ID = "notification_id";
    public static final String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        Log.d(TAG, "notification id: " + notificationId);
        manager.notify(notificationId, notification);
    }

    // Gets difference as a long between date parameters
    public static long getDifference(Date target, Date now) {
        long difference = 0;
        long secInMillis = 1000;
        long minInMillis = 60000;
        long hoursInMillis = 3600000;
        difference = target.getTime() - now.getTime();
        difference = difference + (hoursInMillis * 6);
        Log.d(TAG, "difference time in millis: " + difference);
        Log.d(TAG, "difference time in minutes: " + difference/minInMillis);
        Log.d(TAG, "difference time in hourse: " + difference/hoursInMillis);
        return difference;
    }


    // Notification scheduler
    public static void scheduleNotification(Context context, Notification notification, long delay, int requestCode) {
        Intent notificationIntent = new Intent(context, Notifications.class);
        notificationIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        notificationIntent.putExtra(Notifications.NOTIFICATION_ID, requestCode);
        notificationIntent.putExtra(Notifications.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, notificationIntent, 0);

        long targetDateMillis = System.currentTimeMillis()+ delay;
        Log.d(TAG, "Sched Notificatoin method, system clock + delay (): " + targetDateMillis);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetDateMillis, pendingIntent);
    }

    // Get notification method
    public static Notification getNotification(Context context, String title, String body, int notificationId){

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "com.example.c196project";
        String channelName = "Date Notification";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName,
                    importance);
            manager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        manager.notify(notificationId, builder.build());

        return builder.build();
    }


    // Cancels a notification
    public static void cancelNotification(Context context, int requestCode) {
            NotificationManager notificationManager = (NotificationManager)context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(requestCode);
    }

    // Checks if notification by id exists
    public static int getActiveNotificationId(Context context, int requestCode){
        int notificationId = 0;

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] barNotifications = notificationManager.getActiveNotifications();
        for(StatusBarNotification notification : barNotifications){
            Log.d(TAG, "Notification Id: " + notification.getId());
            if(notification.getId() == requestCode){
                notificationId = notification.getId();
            }
        }
        return notificationId;
    }

}
