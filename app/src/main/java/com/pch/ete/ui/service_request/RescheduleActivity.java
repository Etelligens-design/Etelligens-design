package com.pch.ete.ui.service_request;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.SelectDate;
import com.pch.ete.interfaces.SelectTime;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class RescheduleActivity extends BaseActivity implements IApiCallback<BaseResponse>, SelectDate, SelectTime {

    @BindView(R.id.tv_assign_date)
    TextView tvAssignDate;
    @BindView(R.id.tv_assign_time)
    TextView tvAssignTime;
    @BindView(R.id.et_duration_time)
    EditText etDuration;

    String mSRID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_service);
        bindView(this);

        mSRID = getIntent().getStringExtra("sr_id");
        String dateTime = getIntent().getStringExtra("date_time");
        setResult(Activity.RESULT_CANCELED);
        if (mSRID == null) {
            showToast(getString(R.string.invalid_date_time));
            finish();
            return;
        }
        if (dateTime != null) {
            dateTime = DateUtil.formatDateHHMMForLocal(dateTime);
            tvAssignDate.setText(dateTime.split(" ")[0]);
            tvAssignTime.setText(dateTime.split(" ")[1]);
        }
        etDuration.setText(String.valueOf(getIntent().getIntExtra("duration", 0)));
    }

    @OnClick(R.id.tv_assign_date)
    void onClickPreferredDate() {
        DateUtil.selectDate(this, this, "", "");
    }

    @OnClick(R.id.tv_assign_time)
    void onClickPreferredTime() {
        DateUtil.selectTime(this, this, "");
    }

    @OnClick(R.id.btn_reschedule)
    void onClickReschedule() {
        if (etDuration.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_duration));
            return;
        }

        if (Float.parseFloat(etDuration.getText().toString()) <= 0) {
            showToast(getString(R.string.p_enter_valid_duration));
            return;
        }

        String utcTime = tvAssignDate.getText().toString() + " " + tvAssignTime.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = (Date) formatter.parse(utcTime);
            if (date.getTime() <= System.currentTimeMillis()) {
                showToast(getString(R.string.input_valid_date_time));
                return;
            }
        } catch (Exception e) {
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            if (utcTime.trim().length() > 10) {
                utcTime = DateUtil.formatDateHHMMForUtc(utcTime);
            }
            ApiCall.getInstance().rescheduleSR(preference.getAccessToken(), mSRID, utcTime, etDuration.getEditableText().toString(), this);
        }
    }

    @Override
    public void onSuccess(String type, Response<BaseResponse> response) {
        ProgressHelper.dismiss();
        if (type.equals("reschedule_sr")) {
            if (response.isSuccessful()) {
                if (response.body().getErrorCode().equals("0")) {
                    showToast(response.body().getErrorMsg());
                    setResult(Activity.RESULT_OK);
                    finish();
                } else
                    showToast(response.body().getErrorMsg());
            } else {
                showToast(getString(R.string.something_wrong));
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        showToast(getString(R.string.something_wrong));
    }

    @Override
    public void onDateSelected(String date, String sendDate) {
        tvAssignDate.setText(date);
    }

    @Override
    public void onTimeSelected(String time) {
        tvAssignTime.setText(time);
    }
}
