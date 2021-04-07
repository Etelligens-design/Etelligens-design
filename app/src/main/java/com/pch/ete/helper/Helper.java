package com.pch.ete.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static boolean detectSpecialCharPattern(String s) {
        return !Pattern.compile("\\w+", Pattern.UNICODE_CASE).matcher(s).matches();
    }

    public static boolean checkValidEmail(String email) {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidMobile(String mobile) {
        return !Patterns.PHONE.matcher(mobile).matches();
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,30}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return !matcher.matches();
    }

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

    public static String currencyFormatNaria(float amount){
        DecimalFormat myFormatter = new DecimalFormat("\u20a6 ###,###.##");
        return myFormatter.format(amount);

    }

    public static String currencyFormat(float amount){
        DecimalFormat myFormatter = new DecimalFormat(" ###,###.##");
        return myFormatter.format(amount);

    }

}
