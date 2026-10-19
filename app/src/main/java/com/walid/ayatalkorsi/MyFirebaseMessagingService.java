package com.walid.ayatalkorsi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final int DAILY_NOTIFICATION_CODE = 1525;
    private static final String CHANNEL_ID = "Remote_notification_fire_base_new";
    private static final String RANDOM_KEY = "OpenName";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = null;
        String body = null;

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        if (title == null || title.isEmpty()) {
            title = remoteMessage.getData().get("title");
        }
        if (body == null || body.isEmpty()) {
            body = remoteMessage.getData().get("body");
        }

        if (title == null) {
            title = getString(R.string.app_name);
        }
        if (body == null) {
            body = "";
        }

        Class<?> targetActivity = MainActivity.class;
        String name = remoteMessage.getData().get(RANDOM_KEY);
        if (name != null && !name.isEmpty()) {
            try {
                Class<?> cls = Class.forName(name);
                if (android.app.Activity.class.isAssignableFrom(cls)) {
                    targetActivity = cls;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        sendNotification(title, body, targetActivity);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "إشعارات التطبيق";
            String description = "القناة الخاصة بإشعارات تطبيق آية الكرسي";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private PendingIntent getPendingIntent(Intent intent) {
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        return PendingIntent.getActivity(getApplicationContext(), DAILY_NOTIFICATION_CODE, intent, flags);
    }

    private void sendNotification(String title, String messageBody, Class<?> targetClass) {
        Intent intent = new Intent(getApplicationContext(), targetClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = getPendingIntent(intent);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
        }
    }
}
