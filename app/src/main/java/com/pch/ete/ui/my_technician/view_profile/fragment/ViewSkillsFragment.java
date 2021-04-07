package com.pch.ete.ui.my_technician.view_profile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseFragment;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.ui.my_technician.view_profile.ViewSkillActivity;
import com.pch.ete.ui.my_technician.view_profile.ViewUserProfileActivity;
import com.pch.ete.ui.my_technician.view_profile.adapter.ViewSkillsAdapter;
import com.pch.ete.ui.profile.model.UserSkillData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSkillsFragment extends BaseFragment implements IRecyclerClickListener, Handler.Callback {

    @BindView(R.id.et_search)
    EditText etSearchSkills;

    ViewSkillsAdapter mSkillAdapter;
    Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_skills, container, false);
        bindView(view);
        mHandler = new Handler(this);

        setRecycler(view);
        return view;
    }

    private void setRecycler(View view) {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView recyclerViewSkills = view.findViewById(R.id.recycler_skills);
        recyclerViewSkills.setLayoutManager(manager);
        mSkillAdapter = new ViewSkillsAdapter(this);
        mSkillAdapter.addAllItem(ViewUserProfileActivity.USER_SKILL_LIST);
        recyclerViewSkills.setAdapter(mSkillAdapter);
    }

    @OnTextChanged(R.id.et_search)
    void onSearchTxtChange(){
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 800);
    }

    void searchSkillByString(){
        String searchTxt = etSearchSkills.getText().toString().toUpperCase();
        if(searchTxt.isEmpty()){
            mSkillAdapter.clear();
            mSkillAdapter.addAllItem(ViewUserProfileActivity.USER_SKILL_LIST);
            return;
        }
        ArrayList<UserSkillData> tmpSkillList = new ArrayList<UserSkillData>();
        for(UserSkillData tmp : ViewUserProfileActivity.USER_SKILL_LIST){
            if(tmp.getSkillData() == null ) continue;
            if(tmp.getSkillData().getSkillName().toUpperCase().contains(searchTxt)) {
                tmpSkillList.add(tmp);
            }
        }
        mSkillAdapter.clear();
        mSkillAdapter.addAllItem(tmpSkillList);
    }

    public static ViewSkillsFragment instance;

    public static ViewSkillsFragment getInstance() {
        if (instance == null) {
            instance = new ViewSkillsFragment();
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
        switch (message.what){
            case 0:
                searchSkillByString();
                break;
        }
        return false;
    }
}