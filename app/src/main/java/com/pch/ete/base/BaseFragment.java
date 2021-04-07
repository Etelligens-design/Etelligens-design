package com.pch.ete.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pch.ete.preferences.AppSharedPreference;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    protected AppSharedPreference preference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference=AppSharedPreference.getInstance(getContext());
    }

    protected void bindView(View view){
        ButterKnife.bind(this,view);
    }

    protected void showToast(String mess){
        Toast.makeText(getContext(),mess,Toast.LENGTH_LONG).show();
    }

    protected void showFocus(View view) {
        view.requestFocus();
    }
}
