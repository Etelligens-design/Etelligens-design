package com.pch.ete.ui.notification;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.incoming_request.adapter.IncomingRequestListAdapter;
import com.pch.ete.ui.notification.adapter.NotificationAdapter;
import com.pch.ete.ui.notification.model.NotificationInfo;
import com.pch.ete.ui.notification.model.NotificationInfoRes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

public class NotificationActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_notification)
    RecyclerView recyclerView;

    private NotificationAdapter adapter;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;
    int mRemovedNotificationID = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        bindView(this);

        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    private void setRecycler() {
        recyclerView = findViewById(R.id.recycler_notification);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new NotificationAdapter(this);
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
                        getNotifications();
                    }
                }
            }
        });
        getNotifications();
    }

    private void getNotifications() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().getNotificationList(preference.getAccessToken(), page, this);
        }
    }


    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        isLoading = false;
        if (type.equals("notification_list")) {
            if (response.isSuccessful()) {
                NotificationInfoRes res = (NotificationInfoRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    if (page == 1)
                        adapter.clear();
                    adapter.addAllItem(res.getData());
                    isLastPage = res.getData().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        } else if (type.equals("clear_notification")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    if (mRemovedNotificationID >= 0) {
                        adapter.removeItem(mRemovedNotificationID);
                        mRemovedNotificationID = -1;
                    }
                } else showToast(getString(R.string.something_wrong));
            } else showToast(getString(R.string.something_wrong));
        }
    }

    @Override
    public void onFailure(Object data) {
        isLoading = false;
        ProgressHelper.dismiss();
        showToast(getString(R.string.something_wrong));

    }

    private void setEmpty() {
        tvEmpty.setVisibility(adapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_clear")) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            mRemovedNotificationID = pos;
            ApiCall.getInstance().clearNotification(preference.getAccessToken(), adapter.getItem(pos).getId(), this);
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getNotifications();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ApiCall.getInstance().setReadStateNotification(preference.getAccessToken(), preference.getLoginData().getId(), null);
    }
}
