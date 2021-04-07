package com.pch.ete.ui.my_equipment.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pch.ete.R;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;

import org.w3c.dom.Text;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EquipmentHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_asset_name)
    TextView tvAssetName;
    @BindView(R.id.tv_make_model)
    TextView tvMakeModel;
    @BindView(R.id.btn_edit)
    TextView btnEdit;

    public EquipmentHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(EquipmentInfo data) {
        tvAssetName.setText(data.getAssetName());
        String makeName = "", modelName = "";
        if (data.getMake() != null)
            makeName = data.getMake().getMakeName();
        if (data.getModel() != null)
            modelName = data.getModel().getModelName();
        tvMakeModel.setText(makeName + " " + modelName);
    }
}
