package com.pch.ete.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseFragment;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditLocationActivity extends BaseActivity implements IApiCallback<BaseResponse> {

    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_address_2)
    EditText etAddress2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        bindView(this);
        setViewData();

        setResult(Activity.RESULT_CANCELED);
    }

    void setViewData() {
        etCompanyName.setText(preference.getLoginData().getCompanyName());
        etAddress.setText(preference.getLoginData().getOfficeLocation());
        etAddress2.setText(preference.getLoginData().getCountry());
    }

    @OnClick(R.id.btn_save_location)
    void onClickSaveLocation() {
        if (etCompanyName.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_company_name));
            showFocus(etCompanyName);
            return;
        }

        if (etAddress.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_address));
            showFocus(etAddress);
            return;
        }

        if (etAddress2.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_city_state_country));
            showFocus(etAddress2);
            return;
        }

        saveLocation();
    }

    private void saveLocation() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().updateLocation(preference.getAccessToken(), etCompanyName.getText().toString(), etAddress.getText().toString(), etAddress2.getText().toString(), this);
        }
    }

    @Override
    public void onSuccess(String type, Response<BaseResponse> response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            if (response.body().getErrorCode().equals("0") && type.equals("update_location")) {
                showToast(getString(R.string.saved_successfully));
                LoginData tmpLoginData = preference.getLoginData();
                tmpLoginData.setCompanyName(etCompanyName.getText().toString());
                tmpLoginData.setOfficeLocation(etAddress.getText().toString());
                tmpLoginData.setCountry(etAddress2.getText().toString());
                preference.setLoginData(tmpLoginData);

                Intent intent = new Intent();
                intent.putExtra("office_address", etAddress.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                showToast(response.body().getErrorMsg());
            }
        } else {
            showToast(getString(R.string.something_wrong));
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        showToast("Failed");
    }

    public static EditLocationActivity instance;

    public static EditLocationActivity getInstance() {
        if (instance == null) {
            instance = new EditLocationActivity();
        }
        return instance;
    }
}