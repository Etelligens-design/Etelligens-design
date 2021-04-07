package com.pch.ete.ui.incoming_request.adapter;

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

public class IncomingRequestHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_profile)
    ImageView ivUserLogo;
    @BindView(R.id.iv_fire)
    ImageView ivFire;
    @BindView(R.id.tv_name)
    TextView ivUserName;
    @BindView(R.id.tv_role)
    TextView tvSkill;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_sr_id)
    TextView tvSRId;
    @BindView(R.id.btn_status)
    Button btnStatus;
    @BindView(R.id.tv_asset_name)
    TextView tvAssetName;
    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.btn_reschedule)
    Button btnDecline;

    public IncomingRequestHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(IncomingRequestInfo data) {
        if (data.getUserData() == null) {
            ivUserLogo.setVisibility(View.GONE);
            ivUserName.setVisibility(View.GONE);
        } else {
            ivUserLogo.setVisibility(View.VISIBLE);
            ivUserName.setVisibility(View.VISIBLE);
            ivUserName.setText(data.getUserData().getFullName());
            Glide.with(itemView.getContext()).load(data.getUserData().getImage())
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_placeholder)
                    .into(ivUserLogo);
        }
        tvSkill.setText(data.getUserData().getUserName());
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
        } else if (data.getState() == 4) { // Closed
            tvDate.setText(DateUtil.formatDate(data.getRejectedAt()));
            btnStatus.setBackgroundResource(R.drawable.round_black_rect);
            btnStatus.setText("Expired");
        }

        tvSRId.setText("SR#" + data.getId());
        tvAssetName.setText(data.getEquipmentInfo().getAssetName());

        if (data.getPreferredTime() == null) {
            ivFire.setVisibility(View.GONE);
        } else {
            ivFire.setVisibility(View.VISIBLE);
        }
    }
}
