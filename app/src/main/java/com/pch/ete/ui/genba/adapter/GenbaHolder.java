package com.pch.ete.ui.genba.adapter;

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
import com.pch.ete.ui.service_request.model.ServiceRequestData;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenbaHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_profile)
    ImageView ivTechnicianLogo;
    @BindView(R.id.iv_fire)
    ImageView ivFire;
    @BindView(R.id.tv_name)
    TextView tvTechnicianName;
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
    @BindView(R.id.btn_join)
    Button btnJoin;

    public GenbaHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(ServiceRequestData data) {
        btnJoin.setBackgroundResource(R.drawable.round_light_grey_rect);

        if (data.getVendorData() == null) {
            ivTechnicianLogo.setVisibility(View.GONE);
            tvTechnicianName.setVisibility(View.GONE);
        } else {
            ivTechnicianLogo.setVisibility(View.VISIBLE);
            tvTechnicianName.setVisibility(View.VISIBLE);
            tvTechnicianName.setText(data.getVendorData().getFullName());
            Glide.with(itemView.getContext()).load(data.getVendorData().getImage())
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_placeholder)
                    .into(ivTechnicianLogo);
        }
        tvRole.setText(data.getVendorData().getUserName());
        tvSRId.setPaintFlags(tvSRId.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (data.getState() == 0 || data.getState() == 2) { // opening
            tvDate.setText(DateUtil.formatDateFromServer(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_gold_rect);
            btnStatus.setText("Open");
        } else if (data.getState() == 1) { // accepted
            tvDate.setText(DateUtil.formatDate(data.getAcceptedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_dark_green_rect);
            btnStatus.setText("Accepted");

            String localTime = DateUtil.formatDateHHMMForLocal(data.getAppointmentDate());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = (Date) formatter.parse(localTime);
                if ((System.currentTimeMillis() - date.getTime()) >= -(15 * 60 * 1000)) {
                    btnJoin.setBackgroundResource(R.drawable.round_red_rect);
                }
            } catch (Exception e) {
            }

        } else if (data.getState() == 6) { // reassigning
            tvDate.setText(DateUtil.formatDate(data.getAppointmentDate()));
            btnStatus.setBackgroundResource(R.drawable.round_orange_rect);
            btnStatus.setText("Reschedule");
        } else if (data.getState() == 3) { // expired
            tvDate.setText(DateUtil.formatDate(data.getRejectedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_grey_rect);
            btnStatus.setText("Failed");
        } else if (data.getState() == 4) { // Closed
            tvDate.setText(DateUtil.formatDate(data.getRejectedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_black_rect);
            btnStatus.setText("Expired");
        }

        tvSRId.setText("SR#" + data.getId());
        tvAssetName.setText(data.getEquipmentInfo().getAssetName());
        if (data.getState() != 0) {
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
