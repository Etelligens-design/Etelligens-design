package com.pch.ete.ui.accepted_request.adapter;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pch.ete.R;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.ui.incoming_request.model.IncomingRequestInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AcceptedRequestHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_profile)
    ImageView ivUsernLogo;
    @BindView(R.id.iv_fire)
    ImageView ivFire;
    @BindView(R.id.tv_name)
    TextView tvUserName;
    @BindView(R.id.tv_role)
    TextView tvRole;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_sr_id)
    TextView tvSRId;
    @BindView(R.id.btn_status)
    Button btnStatus;
    @BindView(R.id.tv_asset_name)
    TextView tvAssetName;
    @BindView(R.id.tv_appointment_title)
    TextView tvAppointmentTitle;
    @BindView(R.id.tv_appointment_date)
    TextView tvAppointmentDate;
    @BindView(R.id.btn_reschedule)
    Button btnReschedule;

    public AcceptedRequestHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(IncomingRequestInfo data) {
        if (data.getUserData() == null) {
            ivUsernLogo.setVisibility(View.GONE);
            tvUserName.setVisibility(View.GONE);
        } else {
            ivUsernLogo.setVisibility(View.VISIBLE);
            tvUserName.setVisibility(View.VISIBLE);
            tvUserName.setText(data.getUserData().getFullName());
            Glide.with(itemView.getContext()).load(data.getUserData().getImage())
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_placeholder)
                    .into(ivUsernLogo);
        }
        tvRole.setText(data.getUserData().getUserName());
        tvSRId.setPaintFlags(tvSRId.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (data.getState() == 0 || data.getState() == 2) { // opening
            tvDate.setText(DateUtil.formatDateFromServer(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_gold_rect);
            btnStatus.setText("Open");
        } else if (data.getState() == 1) { // accepted
            tvDate.setText(DateUtil.formatDate(data.getAcceptedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_dark_green_rect);
            btnStatus.setText("Accepted");
        } else if (data.getState() == 6) { // reassigning
            tvDate.setText(DateUtil.formatDate(data.getReassignAt()));
            btnStatus.setBackgroundResource(R.drawable.round_orange_rect);
            btnStatus.setText("Reschedule");
        } else if (data.getState() == 3) { // expired
            tvDate.setText(DateUtil.formatDate(data.getRejectedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_grey_rect);
            btnStatus.setText("Failed");
        }else if (data.getState() == 4) { // expired
            tvDate.setText(DateUtil.formatDate(data.getRejectedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_black_rect);
            btnStatus.setText("Expired");
        }

        tvSRId.setText("SR#" + data.getId());
        tvAssetName.setText(data.getEquipmentInfo().getAssetName());
        if (data.getState() == 1 || data.getState() == 2) {
            tvAppointmentTitle.setVisibility(View.VISIBLE);
            tvAppointmentDate.setVisibility(View.VISIBLE);
            tvAppointmentDate.setText(DateUtil.formatDateddMMMyyyyForLocal(data.getAppointmentDate()));
        } else {
            tvAppointmentTitle.setVisibility(View.GONE);
            tvAppointmentDate.setVisibility(View.GONE);
        }

        if (data.getPreferredTime() == null) {
            ivFire.setVisibility(View.GONE);
        } else {
            ivFire.setVisibility(View.VISIBLE);
        }
    }
}
