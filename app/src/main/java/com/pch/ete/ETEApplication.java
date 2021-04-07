package com.pch.ete;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.pch.ete.ui.video_call.rtmtutorial.ChatManager;

public class ETEApplication extends Application {

    public static final String CHANNEL_1_ID = "ete_channel1";
    public static final String CHANNEL_2_ID = "ete_channel2";

    private static ETEApplication sInstance;

    public static ETEApplication the() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();

        sInstance = this;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_NONE
            );
            channel1.setDescription("This is ETE Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is ETE Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
