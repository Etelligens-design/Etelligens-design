package com.pch.ete.ui.signup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.ui.main.MainEndUserActivity;
import com.pch.ete.ui.main.MainVendorActivity;

import butterknife.OnClick;

public class TermsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        bindView(this);
    }

}
