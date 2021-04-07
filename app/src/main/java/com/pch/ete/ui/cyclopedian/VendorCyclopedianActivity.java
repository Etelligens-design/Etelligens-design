package com.pch.ete.ui.cyclopedian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.ui.my_equipment.MyEquipmentActivity;
import com.pch.ete.ui.my_technician.MyTechnicianActivity;
import com.pch.ete.ui.profile.ProfileUserActivity;
import com.pch.ete.ui.signup.PasswordResetActivity;
import com.pch.ete.ui.technical_note.MyTechNoteListActivity;
import com.pch.ete.ui.technical_video.MyTechVideoListActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class VendorCyclopedianActivity extends BaseActivity {

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
