package com.pch.ete.ui.notification.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.ui.notification.model.NotificationInfo;

public class NotificationHolder extends RecyclerView.ViewHolder {

    TextView btnClear;

    public NotificationHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setData(NotificationInfo notificationInfo) {
//        Glide.with(itemView.getContext()).load(notificationInfo.getImage())
//                .placeholder(R.drawable.icon_placeholder)
//                .error(itemView.getContext().getDrawable(R.drawable.icon_placeholder))
//                .into(((ImageView) itemView.findViewById(R.id.iv_image)));

        ((TextView) itemView.findViewById(R.id.tv_date_order_id)).setText(DateUtil.formatDateTimeFromServer(notificationInfo.getCreatedAt()));
        ((TextView) itemView.findViewById(R.id.tv_notification)).setText(notificationInfo.getNotification());
        btnClear = itemView.findViewById(R.id.btn_clear);
//        if(notificationInfo.getReadState() == 1){
//            itemView.findViewById(R.id.btn_clear).setVisibility(View.INVISIBLE);
//        }else{
//            itemView.findViewById(R.id.btn_clear).setVisibility(View.VISIBLE);
//        }
    }
}
