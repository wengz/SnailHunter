package pers.wengzc.snailhunterrt.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import pers.wengzc.snailhunterrt.CanaryDisplayActivity;
import pers.wengzc.snailhunterrt.R;

/**
 * @author wengzc
 */
public class Notifier {

    private static final int NOTIFICATION_CHANNEL_ID = 13178;

    public static void notifyNewSnail (Context context){
        Intent intent = new Intent(context, CanaryDisplayActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String contentTitle = "捕捉到违反约束的耗时函数";
        String contentText = "点击查看详情";

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建
            NotificationChannel channel = new NotificationChannel(String.valueOf(NOTIFICATION_CHANNEL_ID), "通知", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(ContextCompat.getColor(context, R.color.color_75c17d));
            channel.setShowBadge(true);
            channel.setDescription(contentTitle);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, String.valueOf(NOTIFICATION_CHANNEL_ID));
        mBuilder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.snail_hunter_icon))
                .setOnlyAlertOnce(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.snail_hunter_notification)
                .setAutoCancel(true);
        mBuilder.setContentIntent(pendingIntent);
        manager.notify(NOTIFICATION_CHANNEL_ID, mBuilder.build());
    }

}
