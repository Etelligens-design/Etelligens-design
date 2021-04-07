package com.pch.ete.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.cyclopedian.UserCyclopedianActivity;
import com.pch.ete.ui.login.LoginActivity;
import com.pch.ete.ui.my_equipment.MyEquipmentActivity;
import com.pch.ete.ui.my_technician.MyTechnicianActivity;
import com.pch.ete.ui.notification.NotificationActivity;
import com.pch.ete.ui.profile.ProfileUserActivity;
import com.pch.ete.ui.profile.model.UserSkillDataRes;
import com.pch.ete.ui.genba.UserGenbaListActivity;
import com.pch.ete.ui.service_request.MySRListActivity;
import com.pch.ete.ui.service_request.SendServiceRequestActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class MainEndUserActivity extends BaseActivity implements IApiCallback {

    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.iv_notification)
    ImageView ivNotification;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_role)
    TextView tvUserRole;
    @BindView(R.id.tv_functions)
    TextView tvFunctions;
    @BindView(R.id.li_side_menu)
    LinearLayout liSideMenu;
    @BindView(R.id.rl_parent)
    ScrollView rlParent;
    public static TextView tvNewNotification;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getNewNotificationCount();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_end_user);
        bindView(this);

        tvNewNotification = findViewById(R.id.tv_new_notification);

        rlParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    liSideMenu.setVisibility(View.GONE);
                }
                return false;
            }
        });
//        getUserSkills();
        updateDeviceToken();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("new_notification"));

        if (preference.isFromNotification()) {
            preference.setFromNotification(false);
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        getNewNotificationCount();

        setViewData();

    }

    void getUserSkills() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getUserSkills(preference.getAccessToken(), preference.getLoginData().getId(), this);
        }
    }

    void setViewData() {
        tvUserName.setText(preference.getLoginData().getFullName());
        tvUserRole.setText(preference.getLoginData().getRole());
        if (!preference.getLoginData().getCompanyName().isEmpty() && !preference.getLoginData().getOfficeLocation().isEmpty()) {
            tvFunctions.setVisibility(View.VISIBLE);
            tvFunctions.setText(preference.getLoginData().getCompanyName() + " | " + preference.getLoginData().getOfficeLocation());
        } else if (!preference.getLoginData().getCompanyName().isEmpty()) {
            tvFunctions.setVisibility(View.VISIBLE);
            tvFunctions.setText(preference.getLoginData().getCompanyName());
        } else if (!preference.getLoginData().getOfficeLocation().isEmpty()) {
            tvFunctions.setVisibility(View.VISIBLE);
            tvFunctions.setText(preference.getLoginData().getOfficeLocation());
        } else {
            tvFunctions.setVisibility(View.GONE);
        }
        Glide.with(getApplicationContext()).load(preference.getLoginData().getImage())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_placeholder)
                .into(ivProfile);

        Log.d("fmsdfng: ", "" + preference.getLoginData().getImage());

        if (getIntent().getBooleanExtra("is_first", false)) {
            Intent intent = new Intent(this, ProfileUserActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getIntent().putExtra("is_first", false);
        }
    }

    @OnClick(R.id.iv_menu)
    void onClickMenu() {
        liSideMenu.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_new_notification, R.id.iv_notification})
    void onClickNotification() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.menu_account_profile)
    void onClickProfile() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, ProfileUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.menu_tech_network)
    void onClickMyTechnician() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, MyTechnicianActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.menu_my_equipment)
    void onClickMyEquipment() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, MyEquipmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.menu_log_out)
    void onClickLogOut() {
        liSideMenu.setVisibility(View.GONE);
        preference.setLoginData(null);
        preference.setAccessToken("");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.card_create_sr)
    void onClickCreateServiceRequest() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, SendServiceRequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.card_my_sr)
    void onClickMyServiceRequest() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, MySRListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.card_genba)
    void onClickGenba() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, UserGenbaListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.card_cyclopedian)
    void onClickCyclopedian() {
        liSideMenu.setVisibility(View.GONE);
        Intent intent = new Intent(this, UserCyclopedianActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void getNewNotificationCount() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ApiCall.getInstance().getNewNotificationCount(preference.getAccessToken(), preference.getLoginData().getId(), this);
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (type.equals("user_skills")) {
            if (response.isSuccessful()) {
                UserSkillDataRes res = (UserSkillDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    preference.setUserSkillList(res.getUserSkillDataList());
                } else
                    showToast(res.getErrorMsg());
            }
        } else if (type.equals("new_notification")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    if (!res.getErrorMsg().isEmpty() && Integer.parseInt(res.getErrorMsg()) > 0) {
                        tvNewNotification.setVisibility(View.VISIBLE);
                        tvNewNotification.setText(res.getErrorMsg());
                    } else {
                        tvNewNotification.setVisibility(View.GONE);
                    }
                } else {
                    tvNewNotification.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }

    void updateDeviceToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("ID", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        AppSharedPreference.getInstance(MainEndUserActivity.this).setDeviceToken(token);
                        ApiCall.getInstance().updateDeviceToken(preference.getAccessToken(), token, MainEndUserActivity.this);
                        Log.d("MyToken", token);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (liSideMenu.getVisibility() == View.VISIBLE) {
            liSideMenu.setVisibility(View.GONE);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainEndUserActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
