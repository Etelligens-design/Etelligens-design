package com.pch.ete.ui.incoming_request;

import android.app.Activity;
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
import com.pch.ete.ui.incoming_request.adapter.IncomingRequestListAdapter;
import com.pch.ete.ui.incoming_request.model.IncomingRequestInfoRes;
import com.pch.ete.ui.incoming_request.model.IncomingRequestInfo;
import com.pch.ete.ui.service_request.AcceptRequestActivity;
import com.pch.ete.ui.service_request.MySRDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTextChanged;
import retrofit2.Response;

public class IncomingRequestActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, Handler.Callback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_incoming_request)
    RecyclerView recyclerView;
    @BindView(R.id.et_search_txt)
    EditText etSearchText;

    private IncomingRequestListAdapter adapter;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;
    Handler mHandler;
    String mSearchTxt = "";

    final static int ACCEPT_REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_request);
        bindView(this);

        mHandler = new Handler(this);
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    private void setRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new IncomingRequestListAdapter(this);
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
                        getIncomingSR();
                    }
                }
            }
        });
        getIncomingSR();
    }

    private void getIncomingSR() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().getIncomingSR(preference.getAccessToken(), mSearchTxt, page, this);
        }
    }

    @OnTextChanged(R.id.et_search_txt)
    void textChange() {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 800);
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getIncomingSR();
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        isLoading = false;
        if (type.equals("incoming_sr_list")) {
            if (response.isSuccessful()) {
                IncomingRequestInfoRes res = (IncomingRequestInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    if (page == 1)
                        adapter.clear();
                    adapter.addAllItem(res.getData());
                    isLastPage = res.getData().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        } else if (type.equals("decline_sr")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(res.getErrorMsg());
                    onRefresh();
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
    public boolean handleMessage(@NonNull Message message) {
        switch (message.what) {
            case 1:
                mSearchTxt = etSearchText.getText().toString();
                onRefresh();
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACCEPT_REQUEST && resultCode == Activity.RESULT_OK) {
            onRefresh();
        }
    }

    @Override
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_srid")) {
            Intent intent = new Intent(this, MySRDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("sr_detail", new Gson().toJson(adapter.getItem(pos)));
            startActivity(intent);
        }
        if (type.toString().equals("click_accept")) {
            Intent intent = new Intent(this, AcceptRequestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("sr_detail", new Gson().toJson(adapter.getItem(pos)));
            startActivityForResult(intent, ACCEPT_REQUEST);
        }
        if (type.toString().equals("click_decline")) {
            if (InternetCheck.isConnectedToInternet(this)) {
                ProgressHelper.dismiss();
                ProgressHelper.showDialog(this);
                ApiCall.getInstance().declineSR(preference.getAccessToken(), ((IncomingRequestInfo) data).getId(), this);
            }
        }
    }
}
