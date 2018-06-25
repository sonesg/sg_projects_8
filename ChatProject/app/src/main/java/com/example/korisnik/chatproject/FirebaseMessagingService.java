package com.example.korisnik.chatproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Korisnik on 1/5/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String check_title = "New Friend Request";
        String notification_title = remoteMessage.getNotification().getTitle();
        if (check_title.equals(notification_title)) {
            String notification_text = remoteMessage.getNotification().getBody();

            String clickAction = remoteMessage.getNotification().getClickAction();

            String from_user_id = remoteMessage.getData().get("user_id");

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.img_avatar)
                            .setContentTitle(notification_title)
                            .setContentText(notification_text)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setAutoCancel(true);

            Intent intentResult = new Intent(clickAction);
            intentResult.putExtra("user_id", from_user_id);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intentResult,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            mBuilder.setContentIntent(pendingIntent);

            int notificationID = (int) System.currentTimeMillis();

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notificationID, mBuilder.build());
        }
    }
}
