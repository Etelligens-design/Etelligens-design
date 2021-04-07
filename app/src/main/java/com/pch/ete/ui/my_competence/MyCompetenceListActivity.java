package com.pch.ete.ui.my_competence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.nex3z.flowlayout.FlowLayout;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.my_competence.adapter.CompetenceListAdapter;
import com.pch.ete.ui.my_competence.model.CompetenceInfo;
import com.pch.ete.ui.my_competence.model.CompetenceInfoRes;
import com.pch.ete.ui.my_technician.model.SkillDataRes;
import com.pch.ete.ui.profile.model.SkillData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class MyCompetenceListActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_competence)
    RecyclerView recyclerView;
    @BindView(R.id.fl_filters)
    FlowLayout flFilters;
    @BindView(R.id.btn_add)
    ImageView btnAddNewEq;
    @BindView(R.id.btn_add_skill)
    Button btnAddSkill;

    private CompetenceListAdapter adapter;
    ArrayList<CompetenceInfo> mMyEquipmentList;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;
    ArrayList<SkillData> mFilterArray, mSkillArrayList;
    String[] mSkillStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_competence_list);
        bindView(this);
        mFilterArray = new ArrayList<SkillData>();
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    @OnClick(R.id.btn_add_skill)
    void onClickAddSkill() {
        if (mSkillStr == null || mSkillStr.length == 0) {
            getSkillList();
        } else {
            openSkillDialog();
        }
    }

    void getSkillList() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getSkillList(preference.getAccessToken(), preference.getLoginData().getId(), this);
        }
    }

    public void openSkillDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Skill");
        builder.setSingleChoiceItems(mSkillStr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFilterArray.add(mSkillArrayList.get(which));
                initSkillListView();
                dialog.dismiss();
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

    void initSkillListView() {
        flFilters.removeAllViews();
        for (SkillData skillData : mFilterArray) {
            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10.0f,
                    getResources().getDisplayMetrics()
            );

            TextView tvSkillName = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) px / 2, (int) px / 2, (int) px / 2, (int) px / 2);
            tvSkillName.setLayoutParams(layoutParams);
            tvSkillName.setText(skillData.getSkillName());
            tvSkillName.setHeight((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    36.0f,
                    getResources().getDisplayMetrics()
            ));
            tvSkillName.setTextSize(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8.0f,
                    getResources().getDisplayMetrics()
            ));
            tvSkillName.setTextColor(getResources().getColor(R.color.text_color));
            tvSkillName.setBackgroundResource(R.drawable.round_light_grey_rect);
            tvSkillName.setGravity(Gravity.CENTER);


            tvSkillName.setPadding((int) px * 2, 0, (int) px * 2, 0);
            flFilters.addView(tvSkillName);

        }
        flFilters.addView(btnAddSkill);
        flFilters.invalidate();
        onRefresh();
    }

    private void setRecycler() {
        mMyEquipmentList = new ArrayList<CompetenceInfo>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new CompetenceListAdapter(this);
        recyclerView.setAdapter(adapter);
        setPageLimit(manager, recyclerView);
    }

    private void setPageLimit(final LinearLayoutManager manager, RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                        page = page + 1;
                        getMyCompetenceList();
                    }
                }
            }
        });
        getMyCompetenceList();
    }

    private void getMyCompetenceList() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;

            String jsonSkillIds = "";
            if (mFilterArray.size() > 0) {
                ArrayList<Integer> skillIds = new ArrayList<Integer>();
                int i = 0;
                for (SkillData tmpData : mFilterArray) {
                    skillIds.add(Integer.valueOf(tmpData.getId()));
                    i++;
                }
                jsonSkillIds = new Gson().toJson(skillIds);
            }

            ApiCall.getInstance().getMyCompetence(preference.getAccessToken(), jsonSkillIds, page, this);
        }
    }

    @OnClick(R.id.btn_add)
    void onClickAddCompetence() {
        Intent intent = new Intent(this, AddCompetenceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            getMyCompetenceList();
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        isLoading = false;
        ProgressHelper.dismiss();
        if (type.equals("competence_list")) {
            if (response.isSuccessful()) {
                CompetenceInfoRes res = (CompetenceInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    adapter.clear();
                    adapter.addAllItem(res.getData());
                    isLastPage = res.getData().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        } else if (type.equals("skill_list")) {
            if (response.isSuccessful()) {
                SkillDataRes res = (SkillDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    mSkillArrayList = res.getSkillList();
                    mSkillStr = new String[mSkillArrayList.size()];
                    int i = 0;
                    for (SkillData tmp : mSkillArrayList) {
                        mSkillStr[i] = tmp.getSkillName();
                        i++;
                    }
                    openSkillDialog();
                } else {
                    showToast(res.getErrorMsg());
                }
            } else showToast(getString(R.string.something_wrong));
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        isLoading = false;
        showToast(getString(R.string.something_wrong));
    }

    private void setEmpty() {
        tvEmpty.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getMyCompetenceList();
    }

    @Override
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_edit")) {
            Intent intent = new Intent(this, AddCompetenceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("competence_data", new Gson().toJson(adapter.getItem(pos)));
            startActivityForResult(intent, 1);
        }
    }
}
