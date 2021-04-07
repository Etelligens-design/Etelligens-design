package com.pch.ete.ui.genba;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.genba.adapter.GenbaAdapter;
import com.pch.ete.ui.service_request.MySRDetailActivity;
import com.pch.ete.ui.service_request.model.ServiceRequestData;
import com.pch.ete.ui.service_request.model.ServiceRequestDataRes;
import com.pch.ete.ui.video_call.VideoChatViewActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnTextChanged;
import retrofit2.Response;

public class VendorGenbaListActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, Handler.Callback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_gena)
    RecyclerView recyclerViewMySr;
    @BindView(R.id.et_search_txt)
    EditText etSearchText;


    ArrayList<ServiceRequestData> mMySRArray;
    private GenbaAdapter adapter;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;
    Handler mHandler;
    String mSearchTxt = "", mSRId, mToken, mOpponentId, mOpponentName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gena_list);
        bindView(this);

        mHandler = new Handler(this);
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void setRecycler() {
        mMySRArray = new ArrayList<ServiceRequestData>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewMySr.setLayoutManager(manager);
        adapter = new GenbaAdapter(this);
        recyclerViewMySr.setAdapter(adapter);
        setPageLimit(manager, recyclerViewMySr);
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
                        getMySR();
                    }
                }
            }
        });
//        getMySR();
    }

    @OnTextChanged(R.id.et_search_txt)
    void textChange() {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 800);
    }

    private void getMySR() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().getVendorGenaList(preference.getAccessToken(), mSearchTxt, page, this);
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (type.equals("gena_list")) {
            if (response.isSuccessful()) {
                ServiceRequestDataRes res = (ServiceRequestDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    if (page == 1)
                        adapter.clear();
                    adapter.addAllItem(res.getServiceRequestData());
                    isLastPage = res.getServiceRequestData().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        }

        if (type.equals("token")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    Intent intent = new Intent(this, VideoChatViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("channel", mSRId);
                    intent.putExtra("token", res.getErrorMsg());
                    intent.putExtra("rtm_token", res.getToken());
                    intent.putExtra("opponent_id", mOpponentId);
                    intent.putExtra("opponent_name", mOpponentName);
                    startActivity(intent);
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        }

        if (type.equals("rtm_token")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    Intent intent = new Intent(this, VideoChatViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("channel", mSRId);
                    intent.putExtra("token", mToken);
                    intent.putExtra("rtm_token", res.getErrorMsg());
                    intent.putExtra("opponent_id", mOpponentId);
                    intent.putExtra("opponent_name", mOpponentName);
                    startActivity(intent);
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        }
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        showToast(getString(R.string.something_wrong));
    }

    private void setEmpty() {
        tvEmpty.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_srid")) {
            Intent intent = new Intent(this, MySRDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("sr_detail", new Gson().toJson(adapter.getItem(pos)));
            startActivity(intent);
        }

        if (type.toString().equals("click_join")) {
            ServiceRequestData serviceRequestData = (ServiceRequestData) data;
            if (serviceRequestData.getState() == 3) {
                showToast(getString(R.string.expired_sr));
                return;
            }
            if (serviceRequestData.getState() == 4) {
                showToast(getString(R.string.closed_sr));
                return;
            }
            mOpponentId = serviceRequestData.getId()+"_"+serviceRequestData.getUserId();
            mOpponentName = serviceRequestData.getVendorData().getUserName().substring(0, 2);
            if (serviceRequestData.getToken() == null) {
                getToken(serviceRequestData.getId());
            } else {
                mSRId = serviceRequestData.getId();
                mToken = serviceRequestData.getToken();
                getRTMToken();
            }
        }
    }

    void getToken(String srId) {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            mSRId = srId;
            ApiCall.getInstance().getToken(preference.getAccessToken(), srId, this);
        }
    }

    void getRTMToken() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().getRTMToken(preference.getAccessToken(), mSRId, this);
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getMySR();
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {
            case 1:
                mSearchTxt = etSearchText.getText().toString();
                onRefresh();
                break;
        }
        return false;
    }
}
