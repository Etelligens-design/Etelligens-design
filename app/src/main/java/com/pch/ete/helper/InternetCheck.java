package com.pch.ete.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pch.ete.R;

public class InternetCheck {

    public static Boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
            assert connectivity != null;
            activeNetwork = connectivity.getActiveNetworkInfo();


        if (activeNetwork != null && activeNetwork.isConnected())
            return true;
        else {
            showNoInternetDialog(context);
        }
        return false;
    }


    private static void showNoInternetDialog(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(context.getString(R.string.app_name));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(context.getString(R.string.internet_connection));
        alertDialog.setPositiveButton(
                context.getString(android.R.string.ok), (dialog, which) -> {
                    dialog.dismiss();
                });
        alertDialog.show();
    }


}
