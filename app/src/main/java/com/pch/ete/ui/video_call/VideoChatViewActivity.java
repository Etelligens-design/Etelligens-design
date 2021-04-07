package com.pch.ete.ui.video_call;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.Helper;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.helper.RealPathUtil;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.video_call.adapter.MessageAdapter;
import com.pch.ete.ui.video_call.model.MessageBean;
import com.pch.ete.ui.video_call.model.MessageListBean;
import com.pch.ete.ui.video_call.rtmtutorial.ChatManager;
import com.pch.ete.ui.video_call.utils.ImageUtil;
import com.pch.ete.ui.video_call.utils.MessageUtil;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import iamutkarshtiwari.github.io.ananas.editimage.EditImageActivity;
import iamutkarshtiwari.github.io.ananas.editimage.ImageEditorIntentBuilder;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;
import io.agora.rtm.RtmStatusCode;
import retrofit2.Response;

public class VideoChatViewActivity extends BaseActivity implements IApiCallback {

    @BindView(R.id.rl_setting_menu)
    RelativeLayout rlSettingMenu;
    @BindView(R.id.rl_call_menu)
    RelativeLayout rlCallMenu;
    @BindView(R.id.rl_record_menu)
    RelativeLayout rlRecordMenu;
    @BindView(R.id.rl_on_off_camera_menu)
    RelativeLayout rlOnOffCameraMenu;
    @BindView(R.id.rl_mic_menu)
    LinearLayout rlMicMenu;
    @BindView(R.id.rl_annotation_menu)
    RelativeLayout rlAnnotationMenu;
    @BindView(R.id.rl_share_video_menu)
    RelativeLayout rlPlayVideoMenu;
    @BindView(R.id.rl_chat_menu)
    RelativeLayout rlChatMenu;
    @BindView(R.id.rl_image_sharing_menu)
    RelativeLayout rlImagesharingMenu;
    @BindView(R.id.rl_ar_menu)
    RelativeLayout rlARMenu;
    @BindView(R.id.iv_call_menu)
    ImageView ivCallMenu;
    @BindView(R.id.iv_setting_menu)
    ImageView ivSettingMenu;
    @BindView(R.id.iv_on_off_camera_menu)
    ImageView ivCameraOnOff;
    @BindView(R.id.iv_record_menu)
    ImageView ivRecordMenu;
    @BindView(R.id.iv_mic_menu)
    ImageView ivMicMenu;
    @BindView(R.id.iv_arrow_up)
    ImageView ivArrowUp;
    @BindView(R.id.iv_annotation_menu)
    ImageView ivAnnotationMenu;
    @BindView(R.id.iv_play_video_menu)
    ImageView ivVideoMenu;
    @BindView(R.id.iv_chat_menu)
    ImageView ivChatMenu;
    @BindView(R.id.iv_flash_menu)
    ImageView ivFlashMenu;
    @BindView(R.id.iv_image_menu)
    ImageView ivImageMenu;
    @BindView(R.id.iv_ar_menu)
    ImageView ivARMenu;
    @BindView(R.id.iv_switch_camera_menu)
    ImageView ivSwitchCameraMenu;
    @BindView(R.id.switch_mute_other_audio)
    Switch switchOtherMute;
    @BindView(R.id.switch_mute_my_audio)
    Switch switchMyMute;
    @BindView(R.id.rl_chat)
    RelativeLayout rlChatView;
    @BindView(R.id.message_list)
    RecyclerView mChatRecyclerView;
    @BindView(R.id.message_edittiext)
    EditText mMsgEditText;
    @BindView(R.id.iv_chat_image)
    ImageView mBigImage;

    @BindView(R.id.rl_audio_setting)
    RelativeLayout rlAudioSetting;
    @BindView(R.id.li_camera_settings)
    LinearLayout liCameraSettings;
    @BindView(R.id.control_panel)
    LinearLayout liControlPanel;
    @BindView(R.id.rl_video_call_info)
    RelativeLayout rlVideoCallInfo;
    @BindView(R.id.tv_sr_id)
    TextView tvSrId;

    @BindView(R.id.rl_exit)
    RelativeLayout rlSetExitState;
    @BindView(R.id.radio_complete_state)
    RadioButton radioComplete;
    @BindView(R.id.radio_reschedule_state)
    RadioButton radioReschedule;
    @BindView(R.id.radio_interrupted_state)
    RadioButton radioInterrupted;
    @BindView(R.id.radio_end_yes)
    RadioButton radioEndYes;
    @BindView(R.id.radios_end)
    RadioGroup radioEndYesNo;

    @BindView(R.id.rl_record_info)
    RelativeLayout rlRecordInfo;
    @BindView(R.id.rl_camera_info)
    RelativeLayout rlCameraInfo;
    @BindView(R.id.rl_video_chat)
    RelativeLayout rlVideoChat;
    @BindView(R.id.video_list)
    RecyclerView mVideoRecyclerView;
    @BindView(R.id.rl_image_chat)
    RelativeLayout rlImageChat;
    @BindView(R.id.image_list)
    RecyclerView mImageRecyclerView;


    private static final String TAG = VideoChatViewActivity.class.getSimpleName();
    private static final int PERMISSION_REQ_ID = 22;
    private final int PHOTO_EDITOR_REQUEST_CODE = 231;// Any integer value as a request code.
    private final int VIDEO_REQUEST_CODE = 232;
    private final int PICK_IMAGE = 233;
    private final int PICK_IMAGE2 = 234;


    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd;
    private boolean isMyAudioMuted, isOtherAudioMute;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private VideoCanvas mLocalVideo;
    private VideoCanvas mRemoteVideo;

    String mChannelName, mToken, mRTMToken;
    private RtmClient mRtmClient;

    // Customized logger view
    private LoggerRecyclerView mLogView;
    ChatManager mChatManager;
    boolean isFrontCamera = true, isCameraOn = true;
    String mOpponentId, mOpponentName;

    // chat side
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private List<MessageBean> mVideoBeanList = new ArrayList<>();
    private List<MessageBean> mImageBeanList = new ArrayList<>();
    private MessageAdapter mMessageAdapter, mVideoAdapter, mImageAdapter;

    private RtmClientListener mClientListener;
    private boolean mIsPeerToPeerMode = false;
    String mUserId, mRecordId, outPath;
    private int mChatMemberCount = 1;
    int mOtherUID = 0, mMyUID;

    private RtmChannel mRtmChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);
        bindView(this);

        mChannelName = getIntent().getStringExtra("channel");
        mToken = getIntent().getStringExtra("token");
        Log.d("sdfnsdmf: ", "" + mToken);
        mRTMToken = getIntent().getStringExtra("rtm_token");
        mOpponentId = getIntent().getStringExtra("opponent_id");
        mOpponentName = getIntent().getStringExtra("opponent_name");
        tvSrId.setText("SR#" + mChannelName);
        mUserId = preference.getLoginData().getId();

        if (mChannelName == null || mToken == null || mRTMToken == null || mOpponentId == null) {
            showToast("Invalid channel or token, please try again");
            finish();
            return;
        }

        mChatManager = new ChatManager(this);
        mChatManager.init();
        mRtmClient = mChatManager.getRtmClient();

        initUI();

        // Ask for permissions at runtime.
        // This is just an example set of permissions. Other permissions
        // may be needed, and please refer to our online documents.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[3], PERMISSION_REQ_ID)) {
            doLogin();
            initEngineAndJoinChannel();

        }
    }

    private void initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        mLogView = findViewById(R.id.log_recycler_view);
    }

    /**
     * API CALL: login RTM server
     */
    private void doLogin() {
        mRtmClient.login(mRTMToken, mUserId, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.i(TAG, "login success");
                runOnUiThread(() -> {
//                    showToast("login success");
                    initChatEngine();
                });
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.i(TAG, "login failed: " + errorInfo.getErrorCode());
                Log.i(TAG, "login failed: " + errorInfo.getErrorDescription());
                switch (errorInfo.getErrorCode()) {
                    case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT: {
                       // doLogin();
                        break;
                    }
                    case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE: {
                       // doLogin();
                        break;
                    }
                }
//                Toast.makeText(VideoChatViewActivity.this, errorInfo.getErrorDescription(), Toast.LENGTH_LONG).show();
                runOnUiThread(() -> {
                    showToast(getString(R.string.login_chat_failed));
                    finish();
                });
            }
        });
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
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            doLogin();
            initEngineAndJoinChannel();
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.private_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    private void initChatEngine() {
        mRtmClient = mChatManager.getRtmClient();
        mClientListener = new MyRtmClientListener();
        mChatManager.registerListener(mClientListener);

        if (mIsPeerToPeerMode) {
            // load history chat records
            MessageListBean messageListBean = MessageUtil.getExistMessageListBean(mOpponentId);
            if (messageListBean != null) {
                mMessageBeanList.addAll(messageListBean.getMessageBeanList());
            }

            // load offline messages since last chat with this peer.
            // Then clear cached offline messages from message pool
            // since they are already consumed.
            MessageListBean offlineMessageBean = new MessageListBean(mOpponentId, mChatManager);
            mMessageBeanList.addAll(offlineMessageBean.getMessageBeanList());
            mChatManager.removeAllOfflineMessages(mOpponentId);
        } else {
            createAndJoinChannel();
        }

        mMessageAdapter = new MessageAdapter(this, mMessageBeanList, message -> {
        }, preference.getLoginData().getUserName().substring(0, 2), mOpponentName);
        mImageAdapter = new MessageAdapter(this, mImageBeanList, message -> {
            if (message.getMessage().getMessageType() == RtmMessageType.IMAGE) {
                if (!TextUtils.isEmpty(message.getCacheFile())) {
                    Glide.with(this).load(message.getCacheFile()).into(mBigImage);
                    mBigImage.setVisibility(View.VISIBLE);
                } else {
                    ImageUtil.cacheImage(this, mRtmClient, (RtmImageMessage) message.getMessage(), new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String file) {
                            message.setCacheFile(file);
                            runOnUiThread(() -> {
                                Glide.with(VideoChatViewActivity.this).load(file).into(mBigImage);
                                mBigImage.setVisibility(View.VISIBLE);
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {
                            runOnUiThread(() -> {
                                showToast("Failed to get image");
                            });
                        }
                    });
                }
            }
        }, preference.getLoginData().getUserName().substring(0, 2), mOpponentName);
        mVideoAdapter = new MessageAdapter(this, mVideoBeanList, message -> {
            if (message.getMessage().getMessageType() == RtmMessageType.FILE) {
                ImageUtil.cacheVideo(this, mRtmClient, (RtmFileMessage) message.getMessage(), new ResultCallback<String>() {
                    @Override
                    public void onSuccess(String file) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(file), "video/*");
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        runOnUiThread(() -> {
                            showToast("Failed to get image");
                        });
                    }
                });
            }
        }, preference.getLoginData().getUserName().substring(0, 2), mOpponentName);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mChatRecyclerView.setLayoutManager(layoutManager);
        mChatRecyclerView.setAdapter(mMessageAdapter);
        mImageRecyclerView.setLayoutManager(layoutManager2);
        mImageRecyclerView.setAdapter(mImageAdapter);
        mVideoRecyclerView.setLayoutManager(layoutManager3);
        mVideoRecyclerView.setAdapter(mVideoAdapter);
    }

    @OnClick(R.id.iv_flash_menu)
    void onClickFlash() {
        if (mRtcEngine != null && mRtcEngine.isCameraTorchSupported() && !isFrontCamera) {
            if (ivFlashMenu.isSelected()) {
                ivFlashMenu.setSelected(false);
                mRtcEngine.setCameraTorchOn(false);
                ivFlashMenu.setImageResource(R.drawable.icon_flash_off);
            } else {
                ivFlashMenu.setSelected(true);
                mRtcEngine.setCameraTorchOn(true);
                ivFlashMenu.setImageResource(R.drawable.icon_flash_on);
            }
        } else {
            showToast(getString(R.string.not_available_operation));
        }
    }

    @OnClick(R.id.rl_record_menu)
    void onClickRecordMenu() {
        if (mCallEnd || mOtherUID == 0) {
            showToast(getString(R.string.not_available_operation));
        } else {
            if (mRecordId == null) { // start record
                startRecordVideo();
            } else { // camera off
                stopRecordVideo();
            }
        }
    }

    void startRecordVideo() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().startRecordVideo(preference.getAccessToken(), mChannelName, "" + (mMyUID & 0xFFFFFFFFL), this);
        }
    }

    void stopRecordVideo() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().stopRecordVideo(preference.getAccessToken(), mRecordId, this);
        }
    }

    @OnClick(R.id.rl_call_menu)
    void onClickCallMenu() {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            rlCallMenu.setBackgroundResource(R.color.red);
            ivCallMenu.setImageResource(R.drawable.icon_call_start);
        } else {
            rlSetExitState.setVisibility(View.VISIBLE);
            radioEndYes.setChecked(true);
        }
    }

    @OnClick(R.id.iv_switch_camera_menu)
    void onClickSwitchCameraMenu() {
        if (mCallEnd) {
            showToast(getString(R.string.not_available_operation));
        } else {
            mRtcEngine.switchCamera();
            isFrontCamera = !isFrontCamera;
            if (isFrontCamera) { // front camera
                //refresh flash state
                ivSwitchCameraMenu.setSelected(false);
                mRtcEngine.setCameraTorchOn(false);
                ivFlashMenu.setImageResource(R.drawable.icon_flash_off);
            }
        }
    }

    @OnClick(R.id.iv_fullscreen)
    void onClickFullScreen() {
        liControlPanel.setVisibility(View.GONE);
        liCameraSettings.setVisibility(View.GONE);
    }

    @OnClick(R.id.remote_video_view_container)
    void onClickShowControllers() {
        liControlPanel.setVisibility(View.VISIBLE);
        liCameraSettings.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.rl_on_off_camera_menu)
    void onClickOnOffCameraMenu() {
        if (mCallEnd) {
            showToast(getString(R.string.not_available_operation));
        } else {
            isCameraOn = !isCameraOn;
            if (isCameraOn) { // camera on
                mRtcEngine.muteLocalVideoStream(false);
                rlOnOffCameraMenu.setBackgroundResource(R.color.grey_light);
                ivCameraOnOff.setImageResource(R.drawable.icon_videocam_off);
                rlCameraInfo.setVisibility(View.GONE);
            } else { // camera off
                mRtcEngine.muteLocalVideoStream(true);
                rlOnOffCameraMenu.setBackgroundResource(R.color.grey);
                ivCameraOnOff.setImageResource(R.drawable.icon_videocam_on);
                rlCameraInfo.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.rl_mic_menu)
    void onClickMicMenu() {
        if (mCallEnd) {
            showToast(getString(R.string.not_available_operation));
        } else {
            if (rlAudioSetting.getVisibility() == View.VISIBLE) {
                rlAudioSetting.setVisibility(View.GONE);
                if (isMyAudioMuted || isOtherAudioMute) {
                    rlMicMenu.setBackgroundResource(R.color.grey);
                    ivMicMenu.setImageResource(R.drawable.icon_mic_off_white);
                    ivArrowUp.setImageResource(R.drawable.icon_arrow_up_white);
                } else {
                    rlMicMenu.setBackgroundResource(R.color.grey_light);
                    ivMicMenu.setImageResource(R.drawable.icon_mic_on_grey);
                    ivArrowUp.setImageResource(R.drawable.icon_arrow_up);
                }
            } else {
                hideAllPopupViews();
                rlAudioSetting.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.rl_setting_menu)
    void onClickChannelInfo() {
        if (rlVideoCallInfo.getVisibility() == View.VISIBLE) {
            rlVideoCallInfo.setVisibility(View.GONE);
            rlSettingMenu.setBackgroundResource(R.color.grey_light);
            ivSettingMenu.setImageResource(R.drawable.icon_setting_grey);
        } else {
            hideAllPopupViews();
            rlVideoCallInfo.setVisibility(View.VISIBLE);
            rlSettingMenu.setBackgroundResource(R.color.grey);
            ivSettingMenu.setImageResource(R.drawable.icon_setting_white);
        }
    }

    @OnClick(R.id.iv_copy_sr_id)
    void onClickCopySRID() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(tvSrId.getText().toString());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", tvSrId.getText().toString());
            clipboard.setPrimaryClip(clip);
        }
    }

    @OnCheckedChanged(R.id.switch_mute_my_audio)
    void onCheckSwitchMyAudio(CompoundButton switchButton, boolean checked) {
        if (checked) {
            isMyAudioMuted = true;
        } else {
            isMyAudioMuted = false;
        }
        mRtcEngine.muteLocalAudioStream(isMyAudioMuted);
    }

    @OnCheckedChanged(R.id.switch_mute_other_audio)
    void onCheckSwitchOtherAudio(CompoundButton switchButton, boolean checked) {
        if (mOtherUID == 0) {
            showToast(getString(R.string.not_available_operation));
            switchButton.setChecked(false);
            return;
        }

        if (checked) {
            isOtherAudioMute = true;
        } else {
            isOtherAudioMute = false;
        }
        mRtcEngine.muteRemoteAudioStream(mOtherUID, isOtherAudioMute);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.rl_annotation_menu)
    void onClickAnnotation() {
        if (mRemoteVideo == null) {
            showToast(getString(R.string.not_available_operation));
            return;
        }
        Bitmap bm = Bitmap.createBitmap(360, 640, Bitmap.Config.ARGB_8888);
        PixelCopy.request(mRemoteVideo.view, bm, new PixelCopy.OnPixelCopyFinishedListener() {
            @Override
            public void onPixelCopyFinished(int i) {
                try {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/annotation.jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    if (file.exists())
                        file.delete();
                    FileOutputStream fOut = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream

                    Intent intent = new ImageEditorIntentBuilder(VideoChatViewActivity.this, getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/annotation.jpg", getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/annotation_result" + System.currentTimeMillis() + ".jpg")
                            .withAddText() // Add the features you need
                            .withPaintFeature()
                            .withFilterFeature()
                            .withRotateFeature()
                            .withCropFeature()
                            .withBrightnessFeature()
                            .withSaturationFeature()
                            .withBeautyFeature()
                            .forcePortrait(true)  // Add this to force portrait mode (It's set to false by default)
                            .setSupportActionBarVisibility(false) // To hide app's default action bar
                            .build();

                    EditImageActivity.start(VideoChatViewActivity.this, intent, PHOTO_EDITOR_REQUEST_CODE);
                } catch (Exception e) {
                    showToast("Failed, try again");
                }
            }
        }, new Handler());
    }

    @OnClick(R.id.rl_chat_menu)
    void onClickChatMenu() {
        if (mCallEnd) {
            showToast(getString(R.string.not_available_operation));
            return;
        }
        if (rlChatView.getVisibility() == View.VISIBLE) {
            rlChatView.setVisibility(View.GONE);
            rlChatMenu.setBackgroundResource(R.color.grey_light);
            ivChatMenu.setImageResource(R.drawable.icon_chat_grey);
        } else {
            if (rlImageChat.getVisibility() == View.VISIBLE) {
                rlImageChat.setVisibility(View.GONE);
                rlImagesharingMenu.setBackgroundResource(R.color.grey_light);
                ivImageMenu.setImageResource(R.drawable.icon_image_grey);
            }
            if (rlVideoChat.getVisibility() == View.VISIBLE) {
                rlVideoChat.setVisibility(View.GONE);
                rlPlayVideoMenu.setBackgroundResource(R.color.grey_light);
                ivVideoMenu.setImageResource(R.drawable.icon_video_grey);
            }
            rlChatView.setVisibility(View.VISIBLE);
            rlChatMenu.setBackgroundResource(R.color.grey_dark);
            ivChatMenu.setImageResource(R.drawable.icon_chat_white);
        }
    }

    void hideAllPopupViews() {
        rlSettingMenu.setBackgroundResource(R.color.grey_light);
        ivSettingMenu.setImageResource(R.drawable.icon_setting_grey);
        rlVideoCallInfo.setVisibility(View.GONE);

        if (isMyAudioMuted || isOtherAudioMute) {
            rlMicMenu.setBackgroundResource(R.color.grey);
            ivMicMenu.setImageResource(R.drawable.icon_mic_off_white);
            ivArrowUp.setImageResource(R.drawable.icon_arrow_up_white);
        } else {
            rlMicMenu.setBackgroundResource(R.color.grey_light);
            ivMicMenu.setImageResource(R.drawable.icon_mic_on_grey);
            ivArrowUp.setImageResource(R.drawable.icon_arrow_up);
        }
        rlAudioSetting.setVisibility(View.GONE);
    }

    private void joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
//        String token = getString(R.string.agora_access_token);
//        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
//            token = null; // default, no token
//        }
        mRtcEngine.joinChannel(mToken, mChannelName, "Extra Optional Data", 0);
    }

    /**
     * API CALL: create and join channel
     */
    private void createAndJoinChannel() {
        // step 1: create a channel instance
        mRtmChannel = mRtmClient.createChannel(mChannelName, new MyChannelListener());
        if (mRtmChannel == null) {
            showToast(getString(R.string.join_channel_failed));
            finish();
            return;
        }

        Log.e("channel", mRtmChannel + "");

        // step 2: join the channel
        mRtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.i(TAG, "join channel success");
                getChannelMemberList();
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "join channel failed");
                runOnUiThread(() -> {
                    showToast(getString(R.string.join_channel_failed));
                    finish();
                });
            }
        });
    }

    /**
     * API CALL: get channel member list
     */
    private void getChannelMemberList() {
        mRtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
            @Override
            public void onSuccess(final List<RtmChannelMember> responseInfo) {
                runOnUiThread(() -> {
                    mChatMemberCount = responseInfo.size();
//                    showToast(String.valueOf(mChannelMemberCount));
                });
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "failed to get channel members, err: " + errorInfo.getErrorCode());
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_chat:
                if (mRtmChannel == null || mChatMemberCount != 2) {
                    showToast(getString(R.string.not_ready_try_again));
                    return;
                }
                String msg = mMsgEditText.getText().toString();
                if (!msg.equals("")) {
                    RtmMessage message = mRtmClient.createMessage();
                    message.setText(msg);

                    MessageBean messageBean = new MessageBean(mUserId, message, true);
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mChatRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);

                    if (mIsPeerToPeerMode) {
                        sendPeerMessage(message);
                    } else {
                        sendChannelMessage(message);
                    }
                }
                mMsgEditText.setText("");
                break;
            case R.id.iv_chat_image:
                mBigImage.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick(R.id.rl_share_video_menu)
    void onClickChatVideo() {
        if (mCallEnd) {
            showToast(getString(R.string.not_available_operation));
            return;
        }
        if (rlVideoChat.getVisibility() == View.VISIBLE) {
            rlVideoChat.setVisibility(View.GONE);
            rlPlayVideoMenu.setBackgroundResource(R.color.grey_light);
            ivVideoMenu.setImageResource(R.drawable.icon_video_grey);
        } else {
            if (rlChatView.getVisibility() == View.VISIBLE) {
                rlChatView.setVisibility(View.GONE);
                Helper.hideSoftKeyboard(this, mMsgEditText);
                rlChatMenu.setBackgroundResource(R.color.grey_light);
                ivChatMenu.setImageResource(R.drawable.icon_chat_grey);
            }
            if (rlImageChat.getVisibility() == View.VISIBLE) {
                rlImageChat.setVisibility(View.GONE);
                rlImagesharingMenu.setBackgroundResource(R.color.grey_light);
                ivImageMenu.setImageResource(R.drawable.icon_image_grey);
            }
            rlVideoChat.setVisibility(View.VISIBLE);
            rlPlayVideoMenu.setBackgroundResource(R.color.grey_dark);
            ivVideoMenu.setImageResource(R.drawable.icon_video_white);
        }

    }

    @OnClick(R.id.btn_send_video)
    void onClickSendVideo() {
        if (mRtmChannel == null || mChatMemberCount != 2) {
            showToast(getString(R.string.not_ready_try_again));
            return;
        }
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO_REQUEST_CODE);
    }

    @OnClick(R.id.rl_ar_menu)
    void onClickARMenu() {
        showToast(getString(R.string.coming_soon_ar));
    }

    @OnClick(R.id.rl_image_sharing_menu)
    void onClickChatImage() {
        if (mCallEnd) {
            showToast(getString(R.string.not_available_operation));
            return;
        }

        if (rlImageChat.getVisibility() == View.VISIBLE) {
            rlImageChat.setVisibility(View.GONE);
            rlImagesharingMenu.setBackgroundResource(R.color.grey_light);
            ivImageMenu.setImageResource(R.drawable.icon_image_grey);
        } else {
            if (rlChatView.getVisibility() == View.VISIBLE) {
                rlChatView.setVisibility(View.GONE);
                Helper.hideSoftKeyboard(this, mMsgEditText);
                rlChatMenu.setBackgroundResource(R.color.grey_light);
                ivChatMenu.setImageResource(R.drawable.icon_chat_grey);
            }
            if (rlVideoChat.getVisibility() == View.VISIBLE) {
                rlVideoChat.setVisibility(View.GONE);
                rlPlayVideoMenu.setBackgroundResource(R.color.grey_light);
                ivVideoMenu.setImageResource(R.drawable.icon_video_grey);
            }
            rlImageChat.setVisibility(View.VISIBLE);
            rlImagesharingMenu.setBackgroundResource(R.color.grey_dark);
            ivImageMenu.setImageResource(R.drawable.icon_image_white);
        }

    }

    @OnClick(R.id.btn_send_image)
    void onClickSendImage() {
        if (mRtmChannel == null || mChatMemberCount != 2) {
            showToast(getString(R.string.not_ready_try_again));
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void sendPeerMessage(final RtmMessage message) {
        mRtmClient.sendMessageToPeer(mOpponentId, message, mChatManager.getSendMessageOptions(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // do nothing
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                // refer to RtmStatusCode.PeerMessageState for the message state
                final int errorCode = errorInfo.getErrorCode();
                Log.d("dsnfsdf: ",""+errorInfo.getErrorDescription());
                Log.d("sfskdfns: ", "" + errorInfo.getErrorDescription());
                runOnUiThread(() -> {
                    switch (errorCode) {
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT:{
                            sendPeerMessage(message);
                            break;
                        }
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE:{
                            sendPeerMessage(message);

                        }
                            showToast(getString(R.string.send_msg_failed));
                            break;
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE:
                            showToast(getString(R.string.peer_offline));
                            break;
                        case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER:
                            showToast(getString(R.string.message_cached));
                            break;
                    }
                });
            }
        });
    }

    private void sendChannelMessage(RtmMessage message) {
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                // refer to RtmStatusCode.ChannelMessageState for the message state
                final int errorCode = errorInfo.getErrorCode();
                Log.d("dsnfsdf: ",""+errorInfo.getErrorDescription());
                runOnUiThread(() -> {
                    switch (errorCode) {
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_TIMEOUT:{
                            sendChannelMessage(message);
                            break;
                        }
                        case RtmStatusCode.ChannelMessageError.CHANNEL_MESSAGE_ERR_FAILURE:{
                            sendChannelMessage(message);
                        }
                            showToast(getString(R.string.send_msg_failed));
                            break;
                    }
                });
            }
        });
    }

    private void leaveAndReleaseChannel() {
        if (mRtmChannel != null) {
            mRtmChannel.leave(null);
            mRtmChannel.release();
            mRtmChannel = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE2) {
            if (resultCode == RESULT_OK) {
                outPath = data.getStringExtra("output_path");
                File outFile = new File(outPath);
                if (!outFile.exists() || outFile.length() <= 0) {
                    outPath = data.getStringExtra("source_path");
                }

                showToast(getString(R.string.image_sharing_via_chat));
                ImageUtil.uploadImage(this, mRtmClient, outPath, new ResultCallback<RtmImageMessage>() {
                    @Override
                    public void onSuccess(final RtmImageMessage rtmImageMessage) {
                        runOnUiThread(() -> {
                            MessageBean messageBean = new MessageBean(mUserId, rtmImageMessage, true);
                            messageBean.setCacheFile(outPath);
                            mImageBeanList.add(messageBean);
                            mImageAdapter.notifyItemRangeChanged(mImageBeanList.size(), 1);
                            mImageRecyclerView.scrollToPosition(mImageBeanList.size() - 1);

                            if (mIsPeerToPeerMode) {
                                sendPeerMessage(rtmImageMessage);
                            } else {
                                sendChannelMessage(rtmImageMessage);
                            }
                            showToast(getString(R.string.image_shared_via_chat));
                        });
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        runOnUiThread(() -> {
                            Toast.makeText(VideoChatViewActivity.this, "sending image failed", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        }

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();

                final String file = RealPathUtil.getRealPath(this, resultUri);
                Intent intent = null;
                try {
                    intent = new ImageEditorIntentBuilder(VideoChatViewActivity.this, file, getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/share_image" + System.currentTimeMillis() + ".jpg")
                            .withRotateFeature()
                            .withCropFeature()
                            .withBrightnessFeature()
                            .forcePortrait(true)  // Add this to force portrait mode (It's set to false by default)
                            .setSupportActionBarVisibility(false) // To hide app's default action bar
                            .build();
                    EditImageActivity.start(VideoChatViewActivity.this, intent, PICK_IMAGE2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = data.getData();
                final String file = RealPathUtil.getRealPath(this, resultUri);
                File tmpFile = new File(file);
                if (tmpFile.length() > 1024 * 1024 * 10) {
                    showToast(getString(R.string.big_video_file));
                    return;
                }

                showToast(getString(R.string.video_sharing_via_chat));

                ImageUtil.uploadVideo(this, mRtmClient, file, new ResultCallback<RtmFileMessage>() {
                    @Override
                    public void onSuccess(final RtmFileMessage rtmFileMessage) {
                        runOnUiThread(() -> {
                            MessageBean messageBean = new MessageBean(mUserId, rtmFileMessage, true);
                            mVideoBeanList.add(messageBean);
                            mVideoAdapter.notifyItemRangeChanged(mVideoBeanList.size(), 1);
                            mVideoRecyclerView.scrollToPosition(mVideoBeanList.size() - 1);

                            if (mIsPeerToPeerMode) {
                                sendPeerMessage(rtmFileMessage);
                            } else {
                                sendChannelMessage(rtmFileMessage);
                            }
                            showToast(getString(R.string.video_shared_via_chat));
                        });
                    }

                    @Override
                    public void onFailure(ErrorInfo errorInfo) {
                        Toast.makeText(VideoChatViewActivity.this, "sending video failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        if (requestCode == PHOTO_EDITOR_REQUEST_CODE) { // same code you used while starting

            outPath = data.getStringExtra("output_path");
            File outFile = new File(outPath);
            if (!outFile.exists() || outFile.length() <= 0) {
                outPath = data.getStringExtra("source_path");
            }
            showToast(getString(R.string.annotation_sharing_via_chat));

            ImageUtil.uploadImage(this, mRtmClient, outPath, new ResultCallback<RtmImageMessage>() {
                @Override
                public void onSuccess(final RtmImageMessage rtmImageMessage) {
                    runOnUiThread(() -> {
                        MessageBean messageBean = new MessageBean(mUserId, rtmImageMessage, true);
                        messageBean.setCacheFile(outPath);
                        mImageBeanList.add(messageBean);
                        mImageAdapter.notifyItemRangeChanged(mImageBeanList.size(), 1);
                        mImageRecyclerView.scrollToPosition(mImageBeanList.size() - 1);

                        if (mIsPeerToPeerMode) {
                            sendPeerMessage(rtmImageMessage);
                        } else {
                            sendChannelMessage(rtmImageMessage);
                        }
                        showToast(getString(R.string.annotation_shared_via_chat));
                    });
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    runOnUiThread(() -> {
                        Toast.makeText(VideoChatViewActivity.this, "sending img failed", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();
        }
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy();

        mRtmClient.logout(null);
        if (mIsPeerToPeerMode) {
            MessageUtil.addMessageListBeanList(new MessageListBean(mOpponentId, mMessageBeanList));
        } else {
            leaveAndReleaseChannel();
        }
        mChatManager.unregisterListener(mClientListener);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (!mCallEnd) {
//            leaveChannel();
//        }
//        RtcEngine.destroy();
//        mRtmClient.logout(null);
//        if (mIsPeerToPeerMode) {
//            MessageUtil.addMessageListBeanList(new MessageListBean(mOpponentId, mMessageBeanList));
//        } else {
//            leaveAndReleaseChannel();
//        }
//    }

    private void leaveChannel() {
        if (mRtcEngine != null)
            mRtcEngine.leaveChannel();
    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        mCallEnd = true;
        rlCallMenu.setBackgroundResource(R.color.grey_light);
        ivCallMenu.setImageResource(R.drawable.icon_call_end);
        isMyAudioMuted = false;
        isOtherAudioMute = false;
        rlAudioSetting.setVisibility(View.GONE);
        rlMicMenu.setBackgroundResource(R.color.grey_light);
        ivMicMenu.setImageResource(R.drawable.icon_mic_on_grey);
        ivArrowUp.setImageResource(R.drawable.icon_arrow_up);
        switchMyMute.setChecked(false);
        switchOtherMute.setChecked(false);

        rlOnOffCameraMenu.setBackgroundResource(R.color.grey_light);
        ivCameraOnOff.setImageResource(R.drawable.icon_videocam_off);
        isCameraOn = true;

        removeFromParent(mLocalVideo);
        mLocalVideo = null;
        removeFromParent(mRemoteVideo);
        mRemoteVideo = null;
        leaveChannel();
    }

    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemoteContainer.addView(canvas.view);
        } else if (parent == mRemoteContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalContainer.addView(canvas.view);
        }
    }

    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }

    /**
     * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        /**
         * Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         *
         * @param channel Channel name.
         * @param uid User ID.
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered.
         */
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                    mMyUID = uid;
                }
            });
        }

        /**
         * Occurs when the first remote video frame is received and decoded.
         * This callback is triggered in either of the following scenarios:
         *
         *     The remote user joins the channel and sends the video stream.
         *     The remote user stops sending the video stream and re-sends it after 15 seconds. Possible reasons include:
         *         The remote user leaves channel.
         *         The remote user drops offline.
         *         The remote user calls the muteLocalVideoStream method.
         *         The remote user calls the disableVideo method.
         *
         * @param uid User ID of the remote user sending the video streams.
         * @param width Width (pixels) of the video stream.
         * @param height Height (pixels) of the video stream.
         * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
         */
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                    mOtherUID = uid; // second is other's uid
                }
            });
        }

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a
         *     goodbye message. When this message is received, the SDK determines that the
         *     user/host leaves the channel.
         *
         *     Drop offline: When no data packet of the user or host is received for a certain
         *     period of time (20 seconds for the communication profile, and more for the live
         *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
         *     network connection may lead to false detections, so we recommend using the
         *     Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("User offline, uid: " + (uid & 0xFFFFFFFFL));
                    onRemoteUserLeft(uid);
                    mOtherUID = 0;
                    if (mRecordId != null) {
                        stopRecordVideo();
                    }
                }
            });
        }
    };

    private void setupRemoteVideo(int uid) {
        ViewGroup parent = mRemoteContainer;
        if (parent.indexOfChild(mLocalVideo.view) > -1) {
            parent = mLocalContainer;
        }

        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        if (mRemoteVideo != null) {
            return;
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);
        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(mRemoteVideo);
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            // Destroys remote view
            mRemoteVideo = null;
        }
    }

    private int getMessageColor(String account) {
        for (int i = 0; i < mMessageBeanList.size(); i++) {
            if (account.equals(mMessageBeanList.get(i).getAccount())) {
                return mMessageBeanList.get(i).getBackground();
            }
        }
        return MessageUtil.COLOR_ARRAY[MessageUtil.RANDOM.nextInt(MessageUtil.COLOR_ARRAY.length)];
    }

    @Override
    public void onBackPressed() {
        if (mBigImage.getVisibility() == View.VISIBLE) {
            mBigImage.setVisibility(View.GONE);
        } else if (rlVideoCallInfo.getVisibility() == View.VISIBLE) {
            rlVideoCallInfo.setVisibility(View.GONE);
            rlSettingMenu.setBackgroundResource(R.color.grey_light);
            ivSettingMenu.setImageResource(R.drawable.icon_setting_grey);
        } else if (rlAudioSetting.getVisibility() == View.VISIBLE) {
            rlAudioSetting.setVisibility(View.GONE);
            if (isMyAudioMuted || isOtherAudioMute) {
                rlMicMenu.setBackgroundResource(R.color.grey);
                ivMicMenu.setImageResource(R.drawable.icon_mic_off_white);
                ivArrowUp.setImageResource(R.drawable.icon_arrow_up_white);
            } else {
                rlMicMenu.setBackgroundResource(R.color.grey_light);
                ivMicMenu.setImageResource(R.drawable.icon_mic_on_grey);
                ivArrowUp.setImageResource(R.drawable.icon_arrow_up);
            }
        } else if (rlChatView.getVisibility() == View.VISIBLE) {
            rlChatView.setVisibility(View.GONE);
            Helper.hideSoftKeyboard(this, mMsgEditText);
        } else if (rlImageChat.getVisibility() == View.VISIBLE) {
            rlImageChat.setVisibility(View.GONE);
        } else if (rlVideoChat.getVisibility() == View.VISIBLE) {
            rlVideoChat.setVisibility(View.GONE);
        } else if (rlSetExitState.getVisibility() == View.VISIBLE) {
            rlSetExitState.setVisibility(View.GONE);
        } else if (!mCallEnd) {
            showToast(getString(R.string.please_end_call_before_exit));
        } else if (mCallEnd) {
            if (mRecordId != null) {
                showToast(getString(R.string.p_end_record));
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    exitVideoCall();
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }

    @OnCheckedChanged(R.id.radio_end_no)
    void onClickExit(CompoundButton switchButton, boolean checked) {
        if (checked) {
            endCall();
            switchButton.setChecked(false);
            rlSetExitState.setVisibility(View.GONE);
//            exitVideoCall();
        }
    }

    @OnCheckedChanged({R.id.radio_interrupted_state, R.id.radio_complete_state, R.id.radio_reschedule_state})
    void onChangeExitState(CompoundButton switchButton, boolean checked) {
        radioInterrupted.setChecked(false);
        radioComplete.setChecked(false);
        radioReschedule.setChecked(false);
        switchButton.setChecked(checked);
    }

    @OnClick(R.id.btn_submit_state)
    void onClickSubmitState() {
        if (radioEndYesNo.getCheckedRadioButtonId() != R.id.radio_end_yes) {
            showToast(getString(R.string.p_select_yes_option));
            return;
        }

        if (mRecordId != null) {
            showToast(getString(R.string.p_end_record));
            return;
        }

        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            if (radioEndYesNo.getCheckedRadioButtonId() == R.id.radio_end_yes) {
                int state = 1;
                if (radioComplete.isChecked()) {
                    state = 1;
                } else if (radioReschedule.isChecked()) {
                    state = 2;
                } else if (radioInterrupted.isChecked()) {
                    state = 3;
                }
                ApiCall.getInstance().submitState(preference.getAccessToken(), mChannelName, state, this);
            }
        }
    }


    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            if (type.equals("set_state")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    exitVideoCall();
                } else showToast(res.getErrorMsg());
            } else if (type.equals("start_record")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    mRecordId = res.getErrorMsg();
                    showToast(getString(R.string.record_started));
                    rlRecordMenu.setBackgroundResource(R.color.grey_dark);
                    rlRecordInfo.setVisibility(View.VISIBLE);
                } else showToast(res.getErrorMsg());
            } else if (type.equals("stop_record")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    mRecordId = null;
                    showToast(getString(R.string.record_ended));
                    rlRecordMenu.setBackgroundResource(R.color.grey_light);
                    rlRecordInfo.setVisibility(View.GONE);
                } else showToast(res.getErrorMsg());
            }
        } else {
            showToast(getString(R.string.something_wrong));
        }

    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        showToast(getString(R.string.something_wrong));
    }

    void exitVideoCall() {
        if (!mCallEnd) {
            leaveChannel();
        }
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy();

        mChatManager.unregisterListener(mClientListener);
        mRtmClient.logout(null);

        if (mIsPeerToPeerMode) {
            MessageUtil.addMessageListBeanList(new MessageListBean(mOpponentId, mMessageBeanList));
        } else {
            leaveAndReleaseChannel();
        }

        finish();
    }

    /**
     * API CALLBACK: rtm event listener
     */
    class MyRtmClientListener implements RtmClientListener {

        @Override
        public void onConnectionStateChanged(final int state, int reason) {
            runOnUiThread(() -> {
                switch (state) {
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                        showToast(getString(R.string.reconnecting));
                        break;
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
                        showToast(getString(R.string.account_offline));
                        finish();
                        break;
                }
            });
        }

        @Override
        public void onMessageReceived(final RtmMessage message, final String peerId) {
            runOnUiThread(() -> {
                if (peerId.equals(mOpponentId)) {
                    MessageBean messageBean = new MessageBean(peerId, message, false);
                    messageBean.setBackground(getMessageColor(peerId));
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mChatRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, message);
                }
            });
        }

        @Override
        public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {
            runOnUiThread(() -> {
                if (peerId.equals(mOpponentId)) {
                    MessageBean messageBean = new MessageBean(peerId, rtmImageMessage, false);
                    messageBean.setBackground(getMessageColor(peerId));
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mChatRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, rtmImageMessage);
                }
            });
        }

        @Override
        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

        }

        @Override
        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

        }
    }

    /**
     * API CALLBACK: rtm channel event listener
     */
    class MyChannelListener implements RtmChannelListener {
        @Override
        public void onMemberCountUpdated(int i) {

        }

        @Override
        public void onAttributesUpdated(List<RtmChannelAttribute> list) {

        }

        @Override
        public void onMessageReceived(final RtmMessage message, final RtmChannelMember fromMember) {
            runOnUiThread(() -> {
                String account = fromMember.getUserId();
                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + message);
                MessageBean messageBean = new MessageBean(account, message, false);
                messageBean.setBackground(getMessageColor(account));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mChatRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);

                if (rlChatView.getVisibility() == View.GONE) {
                    onClickChatMenu();
                    showToast(getString(R.string.new_message_came));
                }
            });
        }

        @Override
        public void onImageMessageReceived(final RtmImageMessage rtmImageMessage, final RtmChannelMember rtmChannelMember) {
            runOnUiThread(() -> {
                String account = rtmChannelMember.getUserId();
                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + rtmImageMessage);
                MessageBean messageBean = new MessageBean(account, rtmImageMessage, false);
                messageBean.setBackground(getMessageColor(account));
                mImageBeanList.add(messageBean);
                mImageAdapter.notifyItemRangeChanged(mImageBeanList.size(), 1);
                mImageRecyclerView.scrollToPosition(mImageBeanList.size() - 1);

                if (rlImageChat.getVisibility() == View.GONE) {
                    onClickChatImage();
                    showToast(getString(R.string.new_image_came));
                }
            });
        }

        @Override
        public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {
            runOnUiThread(() -> {
                String account = rtmChannelMember.getUserId();
                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + rtmFileMessage);
                MessageBean messageBean = new MessageBean(account, rtmFileMessage, false);
                messageBean.setBackground(getMessageColor(account));
                mVideoBeanList.add(messageBean);
                mVideoAdapter.notifyItemRangeChanged(mVideoBeanList.size(), 1);
                mVideoRecyclerView.scrollToPosition(mVideoBeanList.size() - 1);

                if (rlVideoChat.getVisibility() == View.GONE) {
                    onClickChatVideo();
                    showToast(getString(R.string.new_video_came));
                }
            });
        }

        @Override
        public void onMemberJoined(RtmChannelMember member) {
            runOnUiThread(() -> {
                mChatMemberCount++;
//                showToast(String.valueOf(mChannelMemberCount));
            });
        }

        @Override
        public void onMemberLeft(RtmChannelMember member) {
            runOnUiThread(() -> {
                mChatMemberCount--;
//                showToast(String.valueOf(mChannelMemberCount));
            });

        }
    }
}
