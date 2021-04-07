package com.pch.ete.ui.service_request;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.my_technician.adapter.TechnicianListAdapter;
import com.pch.ete.ui.my_technician.model.TechnicianInfo;
import com.pch.ete.ui.my_technician.model.TechnicianInfoRes;
import com.pch.ete.ui.service_request.adapter.MySRListAdapter;
import com.pch.ete.ui.service_request.model.ServiceRequestData;
import com.pch.ete.ui.service_request.model.ServiceRequestDataRes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTextChanged;
import retrofit2.Response;

public class MySRListActivity extends BaseActivity implements IApiCallback<ServiceRequestDataRes>, IRecyclerClickListener, Handler.Callback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_my_sr)
    RecyclerView recyclerViewMySr;
    @BindView(R.id.et_search_txt)
    EditText etSearchText;


    ArrayList<ServiceRequestData> mMySRArray;
    private MySRListAdapter adapter;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;
    Handler mHandler;
    String mSearchTxt = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sr_list);
        bindView(this);

        mHandler = new Handler(this);
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    private void setRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewMySr.setLayoutManager(manager);
        adapter = new MySRListAdapter(this);
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
        getMySR();
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
            if (isDateFormat(mSearchTxt)) {
                String date = DateUtil.formatDateddMMMyyyyForUtc(mSearchTxt);
                if (date != null)
                    ApiCall.getInstance().getMySR(preference.getAccessToken(), date, 1, page, this);
                else
                    ApiCall.getInstance().getMySR(preference.getAccessToken(), mSearchTxt, 0, page, this);
            } else {
                ApiCall.getInstance().getMySR(preference.getAccessToken(), mSearchTxt, 0, page, this);
            }
        }
    }

    boolean isDateFormat(String text) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
            date = sdf.parse(text);
            if (!text.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onSuccess(String type, Response<ServiceRequestDataRes> response) {
        isLoading = false;
        ProgressHelper.dismiss();
        if (type.equals("my_sr_list")) {
            if (response.isSuccessful()) {
                if (response.body().getErrorCode().equals("0")) {
                    if (page == 1)
                        adapter.clear();
                    adapter.addAllItem(response.body().getServiceRequestData());
                    isLastPage = response.body().getServiceRequestData().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(response.body().getErrorMsg());
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
        if (type.toString().equals("click_srid")) {
            Intent intent = new Intent(this, MySRDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("sr_detail", new Gson().toJson(adapter.getItem(pos)));
            startActivity(intent);
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
