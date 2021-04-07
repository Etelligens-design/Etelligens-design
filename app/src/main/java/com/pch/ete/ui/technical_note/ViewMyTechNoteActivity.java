package com.pch.ete.ui.technical_note;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.Logger;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

public class ViewMyTechNoteActivity extends BaseActivity implements IApiCallback {

    @BindView(R.id.et_note_title)
    EditText etNoteTitle;
    @BindView(R.id.et_note_description)
    EditText etNoteText;

    String mUrl, mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_tech_note);
        bindView(this);
        setResult(Activity.RESULT_CANCELED);

        mNoteId = getIntent().getStringExtra("note_id");
        if (mNoteId == null) {
            showToast("Invalid Note");
            finish();
            return;
        }
        etNoteTitle.setText(getIntent().getStringExtra("note_title"));
        mUrl = getIntent().getStringExtra("note_url");
        if (mUrl == null || mUrl.isEmpty()) {
            etNoteText.setText(getIntent().getStringExtra("note_text"));
        } else {
            new Thread(new Runnable() {
                public void run() {
                    ArrayList<String> urls = new ArrayList<String>(); //to read each line
                    try {
                        // Create a URL for the desired page
                        URL url = new URL(mUrl); //My text file location
                        //First open the connection
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(60000); // timing out in a minute
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String str;
                        while ((str = in.readLine()) != null) {
                            urls.add(str);
                        }
                        in.close();
                    } catch (Exception e) {
                        Log.d("MyTag", e.toString());
                        showToast(getString(R.string.something_wrong));
                    }
                    ViewMyTechNoteActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            etNoteText.setText(urls.get(0)); // My TextFile has 3 lines
                        }
                    });

                }
            }).start();
        }
    }

    @OnClick(R.id.btn_update_note)
    void onClickUpdateNote() {
        if (etNoteTitle.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.p_enter_title));
            return;
        }

        if (etNoteText.getText().toString().isEmpty()) {
            showToast(getString(R.string.p_enter_note_text));
            return;
        }
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().updateTechNote(preference.getAccessToken(), mNoteId, etNoteTitle.getText().toString(), etNoteText.getText().toString(), this);
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        ProgressHelper.dismiss();
        if (response.isSuccessful()) {
            if (type.equals("update_note")) {
                BaseResponse res = (BaseResponse) response.body();
                if (res.getErrorCode().equals("0")) {
                    showToast(res.getErrorMsg());
                    setResult(Activity.RESULT_OK);
                    finish();
                } else
                    showToast(res.getErrorMsg());
            }
        } else showToast(getString(R.string.something_wrong));
    }

    @Override
    public void onFailure(Object data) {
        ProgressHelper.dismiss();
        Logger.e(String.valueOf(data));
    }

}
