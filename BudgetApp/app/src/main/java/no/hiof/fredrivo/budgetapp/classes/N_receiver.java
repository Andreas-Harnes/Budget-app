package no.hiof.fredrivo.budgetapp.classes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import no.hiof.fredrivo.budgetapp.R;
import no.hiof.fredrivo.budgetapp.overview;

/**
 * Broadcast receiver!
 */
public class N_receiver extends BroadcastReceiver {
    // kilde for servies for notifications: https://www.youtube.com/watch?v=k-tREnlQsrk og
    // https://github.com/ajitsing/AlarmManagerAndReceiver
    private static final String CHANNEL_ID = "WonderReminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, overview.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(overview.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("Saved money yet!?")
                .setContentText("Don't forget to register your posts!")
                .setTicker("Reminder!")
                .setSmallIcon(R.mipmap.ic_dollar_foreground)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // etter oreo så er det et krav om å ha en channel id
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NotificationDemo", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);
    }
}
