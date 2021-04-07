package com.pch.ete.ui.technical_video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.preferences.AppSharedPreference;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.login.LoginActivity;
import com.pch.ete.ui.login.model.LoginDataRes;
import com.pch.ete.ui.main.MainEndUserActivity;
import com.pch.ete.ui.main.MainVendorActivity;
import com.pch.ete.ui.technical_video.model.MyTechVideoData;
import com.potyvideo.library.AndExoPlayerView;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;


public class VideoPlayerActivity extends BaseActivity implements Handler.Callback {

    @BindView(R.id.exo_player)
    AndExoPlayerView andExoPlayerView;
    @BindView(R.id.li_video_info)
    LinearLayout liVideoInfo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_path)
    TextView tvUrl;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        bindView(this);
        mHandler = new Handler(this);

        String jsonData = getIntent().getStringExtra("video_data");
        if (jsonData == null || jsonData.isEmpty()) {
            showToast(getString(R.string.invalid_file));
            finish();
            return;
        }

        MyTechVideoData videoData = new Gson().fromJson(jsonData, MyTechVideoData.class);
        tvTitle.setText(videoData.getTitle());
        tvSize.setText(videoData.getSize());
        tvUrl.setText(videoData.getUrl());
        tvDate.setText(DateUtil.formatDateTimeFromServer(videoData.getDate()));

        HashMap<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("foo", videoData.getTitle());
        andExoPlayerView.setSource(videoData.getUrl(), extraHeaders);
    }

    @OnClick(R.id.iv_video_info)
    void onClickVideoInfo() {
        liVideoInfo.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_close_info)
    void onClickCloseInfo() {
        if (liVideoInfo.getVisibility() == View.VISIBLE) {
            liVideoInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        return false;
    }
}
