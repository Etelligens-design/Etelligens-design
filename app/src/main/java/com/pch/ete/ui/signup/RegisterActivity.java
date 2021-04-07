package com.pch.ete.ui.signup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.Helper;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.model.LoginData;
import com.pch.ete.ui.login.model.LoginDataRes;
import com.pch.ete.ui.main.MainEndUserActivity;
import com.pch.ete.ui.main.MainVendorActivity;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements IApiCallback {

    @BindView(R.id.dropdown_menu)
    TextView tvRole;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_full_name)
    EditText etFullName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_confirm_email)
    EditText etConfirmEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.check_terms)
    CheckBox checkTerms;

    @BindView(R.id.et_otp)
    EditText etOtp;
    @BindView(R.id.rl_confirm_otp)
    RelativeLayout rlConfirmOtp;

    String mOtp;
    ArrayList<String> mRoleArray;
    MySelectAdapter mMySelectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindView(this);


        mRoleArray = new ArrayList<>();
        mRoleArray.add("Equipment End User");
        mRoleArray.add("Equipment Vendor");
        mRoleArray.add("Individual Technician");
        mMySelectAdapter = new MySelectAdapter(mRoleArray);

    }

    @OnClick(R.id.dropdown_menu)
    void onClickUserType() {
        openDialog();
    }

    public void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        String userType[] = {"Equipment End User", "Equipment Vendor", "Individual Technician"};
        builder.setSingleChoiceItems(mMySelectAdapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 2) {
                    showToast("This is not valid now.");
                } else {
                    tvRole.setAlpha(1.0f);
                    tvRole.setText(userType[which]);
                    tvRole.setTag(String.valueOf(which + 1));
                    dialog.dismiss();
                }
            }
        });

        //Set Neutral Button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //Creating AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @OnTextChanged(R.id.et_password)
    void checkSpaceInPassword() {
        if (etPassword.getText().toString().contains(" ")) {
            showToast(getString(R.string.space_not_use_in_password));
        }
    }

    @OnClick(R.id.tv_terms)
    void onClickTerms() {
        Intent intent = new Intent(this, TermsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.btn_register)
    void onClickRegister() {
        Helper.hideSoftKeyboard(this, etUserName);
        if (tvRole.getText().toString().isEmpty()) {
            showFocus(tvRole);
            showToast(getString(R.string.p_select_user_type));
            return;
        }

        if (tvRole.getAlpha() == 0.5f) {
            showFocus(tvRole);
            showToast(getString(R.string.p_select_other_user_type));
            return;
        }

        if (TextUtils.isEmpty(etUserName.getText().toString().trim())) {
            showFocus(etUserName);
            showToast(getString(R.string.p_enter_user_name));
            return;
        }
        if (TextUtils.isEmpty(etFullName.getText().toString().trim())) {
            showFocus(etFullName);
            showToast(getString(R.string.p_enter_full_name));
            return;
        }
        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            showFocus(etEmail);
            showToast(getString(R.string.p_enter_email));
            return;
        }
        if (Helper.checkValidEmail(etEmail.getText().toString())) {
            showFocus(etEmail);
            showToast(getString(R.string.p_enter_valid_email));
            return;
        }
       /* if (TextUtils.isEmpty(etConfirmEmail.getText().toString().trim())) {
            showFocus(etConfirmEmail);
            showToast(getString(R.string.p_enter_confirm_email));
            return;
        }*/

        else if (Helper.checkValidEmail(etConfirmEmail.getText().toString())) {
            showFocus(etConfirmEmail);
            showToast(getString(R.string.p_enter_valid_email));
            return;
        }

        if (!(etEmail.getText().toString().trim().equals(etConfirmEmail.getText().toString().trim()))) {
            showFocus(etEmail);
            showToast(getString(R.string.email_not_match));
            return;
        }

        if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
            showFocus(etPhone);
            showToast(getString(R.string.p_enter_phone));
            return;
        }

        if (!isValidMobile(etPhone.getText().toString())) {
            showToast(getString(R.string.p_enter_valid_phone));
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            showFocus(etPassword);
            showToast(getString(R.string.p_enter_password));
            return;
        }

        if (etPassword.getText().toString().contains(" ")) {
            showFocus(etPassword);
            showToast(getString(R.string.space_not_use_in_password));
            return;
        }

        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            showFocus(etConfirmPassword);
            showToast(getString(R.string.confirm_password_not_match));
            return;
        }

        if (!checkTerms.isChecked()) {
            showFocus(checkTerms);
            showToast(getString(R.string.p_accept_terms));
            return;
        }
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().sendOtp(etUserName.getText().toString(), etEmail.getText().toString(), this);
        }
    }

    private boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    @OnClick(R.id.iv_back_otp)
    void onClickBackOtp() {
        rlConfirmOtp.setVisibility(View.GONE);
        etOtp.setText("");
    }

    @OnClick(R.id.btn_resend)
    void onClickResend() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().sendOtp(etUserName.getText().toString(), etEmail.getText().toString(), this);
        }
    }

    @OnClick(R.id.btn_confirm_otp)
    void onClickConfirmOtp() {
        if (etOtp.getText().toString().equals(mOtp)) {
            if (InternetCheck.isConnectedToInternet(this)) {
                ProgressHelper.dismiss();
                ProgressHelper.showDialog(this);
                ApiCall.getInstance().register(
                        Integer.valueOf(tvRole.getTag().toString()),
                        etUserName.getText().toString(),
                        etFullName.getText().toString(),
                        etEmail.getText().toString(),
                        etPhone.getText().toString(),
                        etPassword.getText().toString(),
                        this
                );
            }
        } else {
            showToast(getString(R.string.p_enter_correct_otp));
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (type.equals("register")) {
            if (response.isSuccessful()) {
                LoginDataRes res = (LoginDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    preference.setLoginData(res.getUserInfo());
                    preference.setAccessToken(res.getToken());
                    preference.setKeepLogin(false);
                    preference.setEmail("");
                    preference.setPassword("");
                    Intent intent = new Intent(this, WelcomeActivity.class);
                    intent.putExtra("role", tvRole.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else
                    showToast(res.getErrorMsg());
            }
        } else if (type.equals("send_otp")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    mOtp = res.getErrorMsg();
                    rlConfirmOtp.setVisibility(View.VISIBLE);
                } else
                    showToast(res.getErrorMsg());
            }
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }

    class MySelectAdapter extends BaseAdapter {

        ArrayList<String> mRolesArray;

        public MySelectAdapter(ArrayList<String> rolesArray) {
            mRolesArray = rolesArray;
        }

        @Override
        public int getCount() {
            return mRolesArray.size();
        }

        @Override
        public String getItem(int i) {
            return mRoleArray.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            TextView tvRoleName = (TextView) LayoutInflater.from(RegisterActivity.this).inflate(R.layout.row_role, null);
            tvRoleName.setText(mRoleArray.get(position));
            if (position == 2) {
                tvRoleName.setTextColor(RegisterActivity.this.getColor(R.color.grey));
            } else {
                tvRoleName.setTextColor(RegisterActivity.this.getColor(R.color.black));
            }
            return tvRoleName;
        }
    }
}
