package com.example.madproject;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TaskStartReciever extends BroadcastReceiver {
    MediaPlayer mp;
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context,AllTasks.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context,1,i,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "TaskNotification")
                .setSmallIcon(R.mipmap.launch_icon)
                .setContentTitle("Task Update!!!")
                .setContentText("A New Task Just Started..Check It Out")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi);

        mp = MediaPlayer.create(context, Settings.System.DEFAULT_NOTIFICATION_URI);
        mp.start();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(102, builder.build());

        }
    }
}
