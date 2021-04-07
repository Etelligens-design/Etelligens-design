package com.pch.ete.helper;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pch.ete.R;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.ui.notification.NotificationActivity;
import com.pch.ete.ui.splash.SplashActivity;

import java.util.List;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // private AppSharedPreferences preferences ;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String CHANNEL_ID = "my_channel_01";
        NotificationChannel mChanal = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChanal = new NotificationChannel(CHANNEL_ID, "Cyclops", NotificationManager.IMPORTANCE_HIGH);
        }
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.eyelogo)
                .setSmallIcon(R.drawable.eyelogo)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentIntent(getPaddingIntent())
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChanal);
        }
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("new_notification"));
    }
    
    private PendingIntent getPaddingIntent() {
        if(isForeground()){
            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                    new Intent(getApplicationContext(), NotificationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), PendingIntent.FLAG_ONE_SHOT);
            return intent;
        }else {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            AppSharedPreference.getInstance(getApplicationContext()).setFromNotification(true);
            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                    new Intent(getApplicationContext(), SplashActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK), PendingIntent.FLAG_ONE_SHOT);
            return intent;
        }
    }

    public boolean isForeground() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals(getApplication().getPackageName());
    }

    @Override
    public void onNewToken(String token) {
        Log.d("Token: ", "Refreshed token: " + token);

    }
}
