package com.podrzizivot.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String NOTIFICATION_CHANNEL_ID = "com.podrzizivot.project";
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "PodrziZivot";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setSound(null)
                    .setContentText(remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "text_za_notifikaciju")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentTitle(getString(R.string.app_name))
                    .setColor(Color.rgb(92, 45, 151))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
                notificationManager.notify(0, builder.build());
            }
        } else {
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
//            notificationBuilder.setContentTitle(getString(R.string.app_name));
//            notificationBuilder.setContentText(remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "text_za_notifikaciju");
//            notificationBuilder.setAutoCancel(true);
//            notificationBuilder.setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
//            notificationBuilder.setColor(this.getResources().getColor(R.color.colorPrimary));
//            notificationBuilder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notification = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "text_za_notifikaciju")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setContentIntent(pendingIntent)
                    .setSound(null)
                    .build();
            if (notificationManager != null) {
                notificationManager.notify(0, notification);
            }
//        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_round));
        }
    }
}
