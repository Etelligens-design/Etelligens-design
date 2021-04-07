package com.pch.ete.ui.technical_video;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.helper.RealPathUtil;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.technical_note.AddNewTechNoteActivity;
import com.pch.ete.ui.technical_note.ViewMyTechNoteActivity;
import com.pch.ete.ui.technical_video.adapter.MyTechVideoListAdapter;
import com.pch.ete.ui.technical_video.model.MyTechVideoDataRes;
import com.pch.ete.ui.video_call.VideoChatViewActivity;
import com.pch.ete.ui.video_call.model.MessageBean;
import com.pch.ete.ui.video_call.utils.ImageUtil;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import retrofit2.Response;

public class MyTechVideoListActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, Handler.Callback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_my_tech_video)
    RecyclerView recyclerViewMyTechVideo;
    @BindView(R.id.et_search_txt)
    EditText etSearchText;
    @BindView(R.id.et_video_title)
    EditText etVideoTitle;
    @BindView(R.id.li_upload_menu)
    LinearLayout liUploadMenu;

    private static final int PERMISSION_REQ_ID = 22;
    private final int VIDEO_REQUEST_CODE = 232;
    private final int CAPTURE_VIDEO_REQUEST_CODE = 233;
    private MyTechVideoListAdapter adapter;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage, isFromSDCard;
    Handler mHandler;
    String mSearchTxt = "", mTitle;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tech_video_list);
        bindView(this);

        mHandler = new Handler(this);
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    private void setRecycler() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerViewMyTechVideo.setLayoutManager(manager);
        adapter = new MyTechVideoListAdapter(this);
        recyclerViewMyTechVideo.setAdapter(adapter);
        setPageLimit(manager, recyclerViewMyTechVideo);
    }

    private void setPageLimit(final GridLayoutManager manager, RecyclerView recyclerView) {
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
                        getMyTechVideo();
                    }
                }
            }
        });
        getMyTechVideo();
    }

    @OnTextChanged(R.id.et_search_txt)
    void textChange() {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 800);
    }

    @OnClick(R.id.btn_add_tech_video)
    void onClickAddTechNote() {
        etVideoTitle.setText("");
        liUploadMenu.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_upload_from_local)
    void onClickUploadFromLocal() {
        if (etVideoTitle.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.p_enter_title));
            return;
        }
        mTitle = etVideoTitle.getText().toString().trim();
        isFromSDCard = true;
        if (checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[3], PERMISSION_REQ_ID)) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_REQUEST_CODE);
        }
    }

    @OnClick(R.id.tv_upload_from_record)
    void onClickUploadFromRecord() {
        if (etSearchText.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.p_enter_title));
            return;
        }
        isFromSDCard = false;
        mTitle = etVideoTitle.getText().toString().trim();
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[3], PERMISSION_REQ_ID)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            startActivityForResult(intent, CAPTURE_VIDEO_REQUEST_CODE);
        }
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showToast("Need permissions");
                return;
            }
            if (isFromSDCard) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_REQUEST_CODE);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, CAPTURE_VIDEO_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        liUploadMenu.setVisibility(View.GONE);
        if (requestCode == VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                final String file = RealPathUtil.getRealPath(this, resultUri);
                File tmpFile = new File(file);
                if (tmpFile.length() > 1024 * 1024 * 20) {
                    showToast(getString(R.string.big_video_file));
                    return;
                }
                uploadVideoFile(tmpFile);
            }
        }

        if (requestCode == CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                final String file = RealPathUtil.getRealPath(this, resultUri);
                File tmpFile = new File(file);
                if (tmpFile.length() > 1024 * 1024 * 20) {
                    showToast(getString(R.string.big_video_file));
                    return;
                }
                uploadVideoFile(tmpFile);
            }
        }
    }

    void uploadVideoFile(File videoFile) {
        if (!videoFile.isFile() || !videoFile.exists()) {
            showToast(getString(R.string.invalid_file));
        }
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().uploadMyTechVideo(preference.getAccessToken(), videoFile, mTitle, this);
        }
    }

    private void getMyTechVideo() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().getMyTechVideo(preference.getAccessToken(), mSearchTxt, page, this);
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        isLoading = false;
        ProgressHelper.dismiss();
        if (type.equals("my_tech_video_list")) {
            if (response.isSuccessful()) {
                MyTechVideoDataRes res = (MyTechVideoDataRes) response.body();
                if (res.getErrorCode().equals("0")) {
                    if (page == 1)
                        adapter.clear();
                    adapter.addAllItem(res.getMyTechVideoDataArrayList());
                    isLastPage = res.getMyTechVideoDataArrayList().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        }
        if (type.equals("upload_video")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(getString(R.string.uploaded_successfully));
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
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_video")) {
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("video_data", new Gson().toJson(adapter.getItem(pos)));
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getMyTechVideo();
    }

    @Override
    public void onBackPressed() {
        if (liUploadMenu.getVisibility() == View.VISIBLE) {
            liUploadMenu.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
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
