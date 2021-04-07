package com.pch.ete.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.pch.ete.base.BaseResponse;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;

import retrofit2.Response;

public class ETEBackService extends Service implements Handler.Callback, IApiCallback {

    private NotificationManagerCompat notificationManager;
    Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(this);
//        if (Build.VERSION.SDK_INT >= 26) {
//            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
////                    .setSmallIcon(android.R.drawable.star_on)
////                    .setContentTitle("Rate Us")
////                    .setContentText("If you feel fine about AwufOga, Please rate us.")
////                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//                    .build();
//
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
////            notification.flags |= Notification.DEFAULT_SOUND;
//            startForeground(1, notification);
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessage(0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 0:
                checkSRAboutToExpire();
                mHandler.sendEmptyMessageDelayed(0, 3 * 60 * 1000);
                break;
        }
        return false;
    }

    protected void checkSRAboutToExpire() {
        if (isConnectedToInternet(this))
            ApiCall.getInstance().checkSRState(AppSharedPreference.getInstance(this).getAccessToken(), AppSharedPreference.getInstance(this).getLoginData().getId(), this);
    }

    public static Boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        assert connectivity != null;
        activeNetwork = connectivity.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onSuccess(String type, Response response) {
        if (type.equals("check_sr_before_10min")) {
            Response<BaseResponse> res = response;
            if (res.isSuccessful()) {
                if (res.body().getErrorCode().equals("0")) {
//                    String result = res.body().getErrorMsg();
//                    if (result != null && !result.isEmpty()) {
//                        String titleAndContent[] = result.split("-");
//                        if (titleAndContent.length == 2) {
//                            sendNotification(titleAndContent[0], titleAndContent[1]);
//                        }
//                    }
                }
            }
        }
    }

    @Override
    public void onFailure(Object data) {

    }
}
