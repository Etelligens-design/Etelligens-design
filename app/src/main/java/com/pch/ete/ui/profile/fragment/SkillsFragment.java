package com.pch.ete.ui.profile.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseFragment;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.profile.AddSkillActivity;
import com.pch.ete.ui.profile.ViewSkillActivity;
import com.pch.ete.ui.profile.adapter.SkillsAdapter;
import com.pch.ete.ui.profile.model.SkillData;
import com.pch.ete.ui.profile.model.UserSkillData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SkillsFragment extends BaseFragment implements IRecyclerClickListener, Handler.Callback {

    @BindView(R.id.et_search)
    EditText etSearchSkills;
    @BindView(R.id.recycler_skills)
    RecyclerView recyclerViewSkills;

    SkillsAdapter mSkillAdapter;
    Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_skills, container, false);
        bindView(view);
        mHandler = new Handler(this);

        setRecycler(view);
        return view;
    }

    private void setRecycler(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerViewSkills.setLayoutManager(manager);
        mSkillAdapter = new SkillsAdapter(this);
        recyclerViewSkills.setAdapter(mSkillAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSkillAdapter.clear();
        mSkillAdapter.addAllItem(preference.getUserSkillList());
    }

    @OnClick(R.id.btn_add_skill)
    void onClickAddSkill() {
        Intent intent = new Intent(getContext(), AddSkillActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnTextChanged(R.id.et_search)
    void onSearchTxtChange() {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 800);
    }

    void searchSkillByString() {
        String searchTxt = etSearchSkills.getText().toString().toUpperCase();
        if (searchTxt.isEmpty()) {
            mSkillAdapter.clear();
            mSkillAdapter.addAllItem(preference.getUserSkillList());
            return;
        }
        ArrayList<UserSkillData> tmpSkillList = new ArrayList<UserSkillData>();
        for (UserSkillData tmp : preference.getUserSkillList()) {
            if(tmp.getSkillData() == null) continue;
            if (tmp.getSkillData().getSkillName().toUpperCase().contains(searchTxt)) {
                tmpSkillList.add(tmp);
            }
        }
        mSkillAdapter.clear();
        mSkillAdapter.addAllItem(tmpSkillList);
    }

    public static SkillsFragment instance;

    public static SkillsFragment getInstance() {
        if (instance == null) {
            instance = new SkillsFragment();
        }
        return instance;
    }

    @Override
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_view")) {
            Intent intent = new Intent(getContext(), ViewSkillActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("skill_json", new Gson().toJson(mSkillAdapter.getItem(pos)));
            startActivity(intent);
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {
            case 0:
                searchSkillByString();
                break;
        }
        return false;
    }
}