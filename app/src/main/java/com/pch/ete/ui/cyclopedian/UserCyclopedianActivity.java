package com.pch.ete.ui.cyclopedian;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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
import com.pch.ete.ui.login.LoginActivity;
import com.pch.ete.ui.my_equipment.MyEquipmentActivity;
import com.pch.ete.ui.my_technician.MyTechnicianActivity;
import com.pch.ete.ui.notification.NotificationActivity;
import com.pch.ete.ui.profile.ProfileUserActivity;
import com.pch.ete.ui.profile.model.UserSkillDataRes;
import com.pch.ete.ui.service_request.MySRListActivity;
import com.pch.ete.ui.service_request.SendServiceRequestActivity;
import com.pch.ete.ui.signup.PasswordResetActivity;
import com.pch.ete.ui.technical_note.MyTechNoteListActivity;
import com.pch.ete.ui.technical_video.MyTechVideoListActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class UserCyclopedianActivity extends BaseActivity {
    @BindView(R.id.tv_ar_menu)
    TextView tvArMenu;
    @BindView(R.id.tv_tech_forum)
    TextView tvTechForum;
    @BindView(R.id.tv_tech_video)
    TextView tvTechVideo;
    @BindView(R.id.tv_my_techical_note)
    TextView tvMyTechNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclopedian_user);
        bindView(this);

        tvArMenu.setText(getString(R.string.ar_procedures) + "\n(Coming Soon)");
        tvTechForum.setText(getString(R.string.tech_forum) + "\n(Coming Soon)");
        tvTechVideo.setText(getString(R.string.technical_video));
        tvMyTechNote.setText(getString(R.string.my_technical_note));
    }

    @OnClick(R.id.card_ar)
    void onClickNotification() {
//        showToast(getString(R.string.coming_soon));
    }

    @OnClick(R.id.card_technical_video)
    void onClickProfile() {
        Intent intent = new Intent(this, MyTechVideoListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.card_my_technical_note)
    void onClickMyTechnician() {
        Intent intent = new Intent(this, MyTechNoteListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.card_forum)
    void onClickMyEquipment() {
//        showToast(getString(R.string.coming_soon));
    }
}
