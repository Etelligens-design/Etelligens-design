package com.pch.ete.ui.technical_note;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pch.ete.R;
import com.pch.ete.base.BaseActivity;
import com.pch.ete.base.BaseResponse;
import com.pch.ete.helper.DateUtil;
import com.pch.ete.helper.InternetCheck;
import com.pch.ete.helper.ProgressHelper;
import com.pch.ete.interfaces.IRecyclerClickListener;
import com.pch.ete.retrofit.ApiCall;
import com.pch.ete.retrofit.IApiCallback;
import com.pch.ete.ui.main.MainVendorActivity;
import com.pch.ete.ui.my_technician.model.TechnicianInfo;
import com.pch.ete.ui.technical_note.adapter.MyTechNoteListAdapter;
import com.pch.ete.ui.technical_note.model.MyTechNoteData;
import com.pch.ete.ui.technical_note.model.MyTechNoteDataRes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Response;

public class MyTechNoteListActivity extends BaseActivity implements IApiCallback, IRecyclerClickListener, Handler.Callback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_my_tech_note)
    RecyclerView recyclerViewMyTechNote;
    @BindView(R.id.et_search_txt)
    EditText etSearchText;

    @BindView(R.id.li_text_note_info)
    LinearLayout liTextInfoNote;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_path)
    TextView tvUrl;

    private MyTechNoteListAdapter adapter;
    int PAGE_SIZE = 10, page = 1;
    boolean isLoading, isLastPage;
    Handler mHandler;
    String mSearchTxt = "";
    int mDeletePos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tech_note_list);
        bindView(this);

        mHandler = new Handler(this);
        setRecycler();
        refreshLayout.setOnRefreshListener(this);
    }

    private void setRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerViewMyTechNote.setLayoutManager(manager);
        adapter = new MyTechNoteListAdapter(this);
        recyclerViewMyTechNote.setAdapter(adapter);
        setPageLimit(manager, recyclerViewMyTechNote);
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
                        getMyTechNote();
                    }
                }
            }
        });
        getMyTechNote();
    }

    @OnTextChanged(R.id.et_search_txt)
    void textChange() {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 800);
    }

    @OnClick(R.id.btn_add_tech_note)
    void onClickAddTechNote() {
        Intent intent = new Intent(this, AddNewTechNoteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            onRefresh();
        }
    }

    private void getMyTechNote() {
        if (InternetCheck.isConnectedToInternet(this)) {
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            isLoading = true;
            ApiCall.getInstance().getMyTechNote(preference.getAccessToken(), mSearchTxt, page, this);
        }
    }

    @Override
    public void onSuccess(String type, Response response) {
        isLoading = false;
        ProgressHelper.dismiss();
        if (type.equals("my_tech_note_list")) {
            if (response.isSuccessful()) {
                MyTechNoteDataRes res = (MyTechNoteDataRes)response.body();
                if (res.getErrorCode().equals("0")) {
                    if (page == 1)
                        adapter.clear();
                    adapter.addAllItem(res.getMyTechNoteDataArrayList());
                    isLastPage = res.getMyTechNoteDataArrayList().size() != PAGE_SIZE;
                    setEmpty();
                } else showToast(res.getErrorMsg());
            } else showToast(getString(R.string.something_wrong));
        }

        if (type.equals("delete_note")) {
            if (response.isSuccessful()) {
                BaseResponse res = (BaseResponse)response.body();
                if (res.getErrorCode().equals("0")) {
                    adapter.removeItem(mDeletePos);
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

    @OnClick(R.id.iv_close_info)
    void onClickCloseInfo() {
        if (liTextInfoNote.getVisibility() == View.VISIBLE) {
            liTextInfoNote.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRecyclerClick(int pos, Object data, Object type) {
        if (type.toString().equals("click_info")) {
            MyTechNoteData info = adapter.getItem(pos);
            tvName.setText(info.getTitle());
            tvSize.setText(info.getSize());
            tvUrl.setText(info.getUrl());
            tvDate.setText(DateUtil.formatDateTimeFromServer(info.getDate()));
            liTextInfoNote.setVisibility(View.VISIBLE);
        }
        if (type.toString().equals("click_note") && adapter.getItem(pos).getUrl().isEmpty()) {
            Intent intent = new Intent(this, ViewMyTechNoteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("note_id", adapter.getItem(pos).getId());
            intent.putExtra("note_title", adapter.getItem(pos).getTitle());
            intent.putExtra("note_text", adapter.getItem(pos).getText());
            startActivityForResult(intent, 1);

        }

        if (type.toString().equals("click_download")) {
            new DownloadFileFromURL().execute(adapter.getItem(pos).getUrl());
        }

        if (type.toString().equals("click_delete")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to delete?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mDeletePos = pos;
                    deleteNoteFile(adapter.getItem(mDeletePos).getId());
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
    }

    void deleteNoteFile(String noteId){
        if(InternetCheck.isConnectedToInternet(this)){
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(this);
            ApiCall.getInstance().deleteNoteFile(preference.getAccessToken(), noteId, this);
        }
    }

    @Override
    public void onRefresh() {
        adapter.clear();
        page = 1;
        refreshLayout.setRefreshing(false);
        getMyTechNote();
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

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        File textTempFile;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressHelper.dismiss();
            ProgressHelper.showDialog(getApplicationContext());
            textTempFile = new File(Environment
                    .getExternalStorageDirectory().toString()
                    + "/tmp.txt");
            textTempFile.delete();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(textTempFile.getAbsolutePath());

                byte data[] = new byte[4096];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
//                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
//            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);
            ProgressHelper.dismiss();
            if (textTempFile.exists()) {
//                Uri uri = Uri.fromFile(textTempFile);
                Uri uri = FileProvider.getUriForFile(MyTechNoteListActivity.this, getApplicationContext().getPackageName() + ".provider", textTempFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(uri, "text/plain");
                startActivity(intent);
            } else {
                showToast(getString(R.string.download_failed));
            }

        }

    }
}
