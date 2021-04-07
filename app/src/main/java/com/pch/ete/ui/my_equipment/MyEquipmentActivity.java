package com.pch.ete.ui.my_equipment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.pch.ete.ui.my_equipment.adapter.EquipmentListAdapter;
import com.pch.ete.ui.my_equipment.model.EquipmentInfo;
import com.pch.ete.ui.my_equipment.model.EquipmentInfoRes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;

public class MyEquipmentActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, Handler.Callback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_equipment)
    RecyclerView recyclerView;
    @BindView(R.id.fl_filters)
    FlowLayout flFilters;
    @BindView(R.id.btn_add)
    ImageView btnAddNewEq;
    @BindView(R.id.et_search_txt)
    EditText etSearchText;

    private EquipmentListAdapter adapter;
    ArrayList<EquipmentInfo> mMyEquipmentList;
    Handler mHandler;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_list);
        bindView(this);
        mHandler = new Handler(this);
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    void getMakeModelList() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getMakeModelSkillList(preference.getAccessToken(), preference.getLoginData().getId(), this);
        }
    }

    private void setRecycler() {
        mMyEquipmentList = new ArrayList<EquipmentInfo>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new EquipmentListAdapter(this);
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
                        getMyEquipmentList();
                    }
                }
            }
        });
        getMyEquipmentList();
    }

    private void getMyEquipmentList() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().getMyEquipment(preference.getAccessToken(), etSearchText.getText().toString(), page, this);
        }
    }

    @OnTextChanged(R.id.et_search_txt)
    void textChange() {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 800);
    }

    @OnClick(R.id.btn_add)
    void onClickAddEq(){
        Intent intent = new Intent(this, EditEquipmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            getMyEquipmentList();
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        isLoading = false;
        if (type.equals("equipment_list")) {
            if (response.isSuccessful()) {
                EquipmentInfoRes res = (EquipmentInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    adapter.clear();
                    adapter.addAllItem(res.getData());
                    isLastPage = res.getData().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(res.getErrorMsg());
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
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_edit")) {
            Intent intent = new Intent(this, EditEquipmentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("eq_data", new Gson().toJson(adapter.getItem(pos)));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getMyEquipmentList();
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {
            case 1:
                if (!isLoading) {
                    page = 1;
                    getMyEquipmentList();
                }
                break;
        }
        return false;
    }
}
