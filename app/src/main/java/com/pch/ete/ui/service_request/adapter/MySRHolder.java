package com.pch.ete.ui.service_request.adapter;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySRHolder extends RecyclerView.ViewHolder {

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

    public MySRHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(ServiceRequestData data) {
        tvRole.setText("Location:\n" + data.getEquipmentInfo().getLocation());
        if (data.getServiceTypeData() != null)
            tvTechnicianName.setText("Service Type:\n" + data.getServiceTypeData().getServiceName());

        ivTechnicianLogo.setVisibility(View.GONE);

        if (data.getState() == 1 || data.getState() == 6 || data.getState() == 4 || data.getState() == 5) { // accepted or rescheduled, closed or done
            if (data.getVendorData() != null) {
                ivTechnicianLogo.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext()).load(data.getVendorData().getImage())
                        .placeholder(R.drawable.icon_placeholder)
                        .error(R.drawable.icon_placeholder)
                        .into(ivTechnicianLogo);
                tvRole.setText(data.getVendorData().getUserName());
                tvTechnicianName.setText(data.getVendorData().getFullName());
            }
        }

        tvSRId.setPaintFlags(tvSRId.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (data.getState() == 0 || data.getState() == 2) { // opening
            tvDate.setText(DateUtil.formatDateFromServer(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_gold_rect);
            btnStatus.setText("Open");
        } else if (data.getState() == 1) { // accepted
//            tvDate.setText(DateUtil.formatDate(data.getAcceptedAt()));
            tvDate.setText(DateUtil.formatDateFromServer(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_dark_green_rect);
            btnStatus.setText("Accepted");
        } else if (data.getState() == 6) { // reschedule
            tvDate.setText(DateUtil.formatDate(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_orange_rect);
            btnStatus.setText("Reschedule");
        } else if (data.getState() == 3) { // expired
//            tvDate.setText(DateUtil.formatDate(data.getRejectedAt()));
            tvDate.setText(DateUtil.formatDateFromServer(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_grey_rect);
            btnStatus.setText("Failed");
        } else if (data.getState() == 4) { // expired
//            tvDate.setText(DateUtil.formatDate(data.getRejectedAt()));
            tvDate.setText(DateUtil.formatDateFromServer(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_black_rect);
            btnStatus.setText("Expired");
        } else if (data.getState() == 5) { // done
//            tvDate.setText(DateUtil.formatDate(data.getCompletedAt()));
            tvDate.setText(DateUtil.formatDateFromServer(data.getCreatedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_red_rect);
            btnStatus.setText("Completed");
        }

        tvSRId.setText("SR#" + data.getId());
        tvAssetName.setText(data.getEquipmentInfo().getAssetName());
        if (data.getState() == 1) {
            tvAppointmentTitle.setVisibility(View.VISIBLE);
            tvAppointmentDate.setVisibility(View.VISIBLE);
            tvAppointmentDate.setText(DateUtil.formatDateddMMMyyyyForLocal(data.getAppointmentDate()));
        } else if (data.getPreferredTime() != null && !data.getPreferredTime().isEmpty()) {
            tvAppointmentTitle.setVisibility(View.VISIBLE);
            tvAppointmentDate.setVisibility(View.VISIBLE);
            tvAppointmentDate.setText(DateUtil.formatDateddMMMyyyyForLocal(data.getPreferredTime()));
        } else {
            tvAppointmentTitle.setVisibility(View.GONE);
            tvAppointmentDate.setVisibility(View.GONE);
        }

        if (data.getState() == 6) { // reschedule does not have appointment
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
