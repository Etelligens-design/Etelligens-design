package com.pch.ete.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.pch.ete.R;
import com.pch.ete.helper.Helper;
import com.pch.ete.preferences.AppSharedPreference;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public abstract class BaseActivity extends AppCompatActivity {
    protected AppSharedPreference preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = AppSharedPreference.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    protected void bindView(Activity activity) {
        ButterKnife.bind(activity);
    }

    @Optional
    @OnClick(R.id.iv_back)
    void back() {
        onBackPressed();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void showFocus(View view) {
        view.requestFocus();
    }

}
