package com.pch.ete.ui.my_technician;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.my_technician.adapter.TechnicianListAdapter;
import com.pch.ete.ui.my_technician.model.SkillDataRes;
import com.pch.ete.ui.my_technician.model.TechnicianInfo;
import com.pch.ete.ui.my_technician.model.TechnicianInfoRes;
import com.pch.ete.ui.my_technician.view_profile.ViewUserProfileActivity;
import com.pch.ete.ui.my_technician.view_profile.ViewVendorProfileActivity;
import com.pch.ete.ui.profile.model.SkillData;
import com.pch.ete.ui.service_request.adapter.SimpleDictionaryAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pl.sly.dynamicautocomplete.DynamicAutoCompleteTextView;
import pl.sly.dynamicautocomplete.OnDynamicAutocompleteListener;
import retrofit2.Response;

public class MyTechnicianActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, SwipeRefreshLayout.OnRefreshListener, OnDynamicAutocompleteListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_technician)
    RecyclerView recyclerView;
    @BindView(R.id.fl_filters)
    FlowLayout flFilters;
    @BindView(R.id.btn_add_skill)
    Button btnAddSkill;
    @BindView(R.id.auto_technician_name)
    DynamicAutoCompleteTextView autoCompleteTextView;

    private static final int THRESHOLD = 1;
    private TechnicianListAdapter adapter;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;
    ArrayList<SkillData> mFilterArray, mSkillArrayList;
    ArrayList<TechnicianInfo> mAllVendors;
    String[] mSkillStr;
    String[] vendorNames;
    private SimpleDictionaryAdapter mSimpleDictionaryAdapter;
    TechnicianInfo mSelectedVendorInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_list);
        bindView(this);
        mFilterArray = new ArrayList<SkillData>();
        mAllVendors = new ArrayList<TechnicianInfo>();
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    private void setRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new TechnicianListAdapter(this);
        recyclerView.setAdapter(adapter);
        setPageLimit(manager, recyclerView);
    }

    @OnClick(R.id.iv_add_new_vendor)
    void onClickAddNewVendor() {
        String newVendorName = autoCompleteTextView.getText().toString();
        if (newVendorName.isEmpty()) {
            showToast(getString(R.string.p_enter_technician_name));
            return;
        }

        mSelectedVendorInfo = null;
        int i = 0;
        for (TechnicianInfo tmpVendor : mAllVendors) {
            if (newVendorName.equals(tmpVendor.getUserName())) {
                mSelectedVendorInfo = tmpVendor;
                break;
            }
            i++;
        }

        if (mSelectedVendorInfo == null) {
            showToast(getString(R.string.invalid_technician_name));
            return;
        }

        addNewTechnician();
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
                boolean isExisted = false;
                SkillData data = mSkillArrayList.get(which);
                for (SkillData skillData : mFilterArray) {
                    if (skillData.getId().equals(data.getId())) {
                        isExisted = true;
                        break;
                    }
                }
                if (!isExisted) {
                    mFilterArray.add(mSkillArrayList.get(which));
                    initSkillListView();
                    dialog.dismiss();
                } else {
                    showToast(getString(R.string.already_exists));
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

    void initSkillListView() {
        flFilters.removeAllViews();
        for (SkillData skillData : mFilterArray) {
            float px = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    6.0f,
                    getResources().getDisplayMetrics()
            );

            TextView tvSkillName = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) px / 2, (int) px / 2, (int) px / 2, (int) px / 2);
            tvSkillName.setLayoutParams(layoutParams);
            tvSkillName.setText(skillData.getSkillName());
            tvSkillName.setHeight((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    34.0f,
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


            tvSkillName.setPadding((int) (px * 1.5f), 0, (int) (px * 1.5), 0);
            flFilters.addView(tvSkillName);
        }
        flFilters.addView(btnAddSkill);
        flFilters.invalidate();
        onRefresh();
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
                        getMyTechnicians();
                    }
                }
            }
        });
        getMyTechnicians();
    }

    private void getMyTechnicians() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().getMyTechnicianList(preference.getAccessToken(), page, this);
        }
    }

    private void getAllTechnicians() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            String jsonSkillIds = "";
            if (mFilterArray.size() > 0) {
                ArrayList<Integer> skillIds = new ArrayList<Integer>();
                for (SkillData tmpData : mFilterArray) {
                    skillIds.add(Integer.valueOf(tmpData.getId()));
                }
                jsonSkillIds = new Gson().toJson(skillIds);
            }

            ApiCall.getInstance().getAllTechnicianList(preference.getAccessToken(), preference.getLoginData().getId(), jsonSkillIds, this);
        }
    }

    private void addNewTechnician() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().addNewTechnician(preference.getAccessToken(), mSelectedVendorInfo.getId(), this);
        }
    }

    private void deleteTechnician(String vendorId) {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().deleteTechnician(preference.getAccessToken(), vendorId, this);
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        isLoading = false;
        if (type.equals("my_technician_list")) {
            if (response.isSuccessful()) {
                TechnicianInfoRes res = (TechnicianInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    adapter.addAllItem(res.getData());
                    if (res.getData().size() < PAGE_SIZE)
                        isLastPage = true;
                    setEmpty();
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
            getAllTechnicians();
        } else if (type.equals("all_technician_list")) {
            if (response.isSuccessful()) {
                TechnicianInfoRes res = (TechnicianInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    mAllVendors.clear();
                    mAllVendors.addAll(res.getData());
                    vendorNames = new String[mAllVendors.size()];
                    int i = 0;
                    for (TechnicianInfo tmp : mAllVendors) {
                        vendorNames[i] = tmp.getUserName();
                        i++;
                    }
                    mSimpleDictionaryAdapter = new SimpleDictionaryAdapter(
                            this, android.R.layout.simple_list_item_1, vendorNames);
                    autoCompleteTextView.setOnDynamicAutocompleteListener(this);
                    autoCompleteTextView.setAdapter(mSimpleDictionaryAdapter);
                    autoCompleteTextView.setThreshold(THRESHOLD);
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
        } else if (type.equals("add_technician")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(getString(R.string.added_technician));
                    autoCompleteTextView.setText("");
                    autoCompleteTextView.dismissDropDown();
                    onRefresh();
                    mAllVendors.remove(mSelectedVendorInfo);
                    mSelectedVendorInfo = null;
                } else {
                    showToast(res.getErrorMsg());
                }
            } else showToast(getString(R.string.something_wrong));
        } else if (type.equals("delete_technician")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(getString(R.string.deleted_technician));
                    onRefresh();
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

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getMyTechnicians();
    }

    private void setEmpty() {
        tvEmpty.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_view")) {
            if (adapter.getItem(pos).getUserType() == 1) { // end user
                Intent intent = new Intent(this, ViewUserProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("user_data", new Gson().toJson(adapter.getItem(pos), TechnicianInfo.class));
                startActivity(intent);
            } else { // vendor
                Intent intent = new Intent(this, ViewVendorProfileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("user_data", new Gson().toJson(adapter.getItem(pos), TechnicianInfo.class));
                startActivity(intent);
            }

        }

        if (type.toString().equals("click_delete")) {
            deleteTechnician(adapter.getItem(pos).getId());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoCompleteTextView.dismissDropDown();
    }

    @Override
    public void onDynamicAutocompleteStart(AutoCompleteTextView view) {

    }

    @Override
    public void onDynamicAutocompleteStop(AutoCompleteTextView view) {

    }
}
