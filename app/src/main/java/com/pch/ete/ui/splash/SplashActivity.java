package com.pch.ete.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.LoginActivity;
import com.pch.ete.ui.login.model.LoginDataRes;
import com.pch.ete.ui.main.MainEndUserActivity;
import com.pch.ete.ui.main.MainVendorActivity;

import java.io.File;

import retrofit2.Response;


public class SplashActivity extends BaseActivity implements Handler.Callback, IApiCallback<LoginDataRes> {

    Handler mHandler;
    boolean isOver2Sec = false, isFail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        getWindow().setBackgroundDrawableResource(R.color.splash_back);

        setContentView(R.layout.activity_splash);
        bindView(this);
        mHandler = new Handler(this);

        getNotificationInstanceId();

        if(!getExternalFilesDir(Environment.DIRECTORY_PICTURES).exists()){
            getExternalFilesDir(Environment.DIRECTORY_PICTURES).mkdir();
        }
        File[] oldImageFiles = getExternalFilesDir(Environment.DIRECTORY_PICTURES).listFiles();
        if (oldImageFiles != null || oldImageFiles.length > 0) {
            for (File tmp : oldImageFiles) {
                tmp.delete();
            }
        }

        if(!getExternalFilesDir(Environment.DIRECTORY_DCIM).exists()){
            getExternalFilesDir(Environment.DIRECTORY_DCIM).mkdir();
        }
        oldImageFiles = getExternalFilesDir(Environment.DIRECTORY_DCIM).listFiles();
        if (oldImageFiles != null && oldImageFiles.length > 0) {
            for (File tmp : oldImageFiles) {
                tmp.delete();
            }
        }

        preference.setLoginData(null);

        if (preference.getAccessToken() != null && !preference.getAccessToken().isEmpty()) {
            mHandler.sendEmptyMessageDelayed(1, 1500);
            ApiCall.getInstance().loginWithToken(preference.getAccessToken(), this);
        } else {
            mHandler.sendEmptyMessageDelayed(0, 1500);
        }

    }

    private void goToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSuccess(String type, Response<LoginDataRes> response) {
        if (type.equals("login")) {
            if (response.isSuccessful()) {
                if (response.body().getErrorCode().equals("0")) {
                    preference.setLoginData(response.body().getUserInfo());
                    if (isOver2Sec) {
                        if (preference.getLoginData().getUserType() == 1) {
                            Intent intent = new Intent(this, MainEndUserActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(this, MainVendorActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        finish();
                    }
                } else {
                    preference.setAccessToken("");
//                    showToast(response.body().getErrorMsg());
                    isFail = true;
                    if (isOver2Sec)
                        goToLogin();
                }
            } else {
//                showToast(getString(R.string.something_wrong));
                preference.setAccessToken("");
                isFail = true;
                if (isOver2Sec)
                    goToLogin();
            }
        }
    }

    @Override
    public void onFailure(Object data) {
//        showToast(getString(R.string.something_wrong));
        preference.setAccessToken("");
        isFail = true;
        if (isOver2Sec)
            goToLogin();
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {
            case 0:
                goToLogin();
                break;
            case 1:
                if (preference.getLoginData() != null) {
                    if (preference.getLoginData().getUserType() == 1) {
                        Intent intent = new Intent(this, MainEndUserActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, MainVendorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    isOver2Sec = true;
                    if (isFail)
                        goToLogin();
                }
                break;
        }
        return false;
    }

    private void getNotificationInstanceId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("ID", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        AppSharedPreference.getInstance(SplashActivity.this).setDeviceToken(token);
                        Log.d("MyToken", token);
                    }
                });
    }
}
