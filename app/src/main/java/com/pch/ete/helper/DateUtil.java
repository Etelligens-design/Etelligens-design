package com.pch.ete.helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pch.ete.interfaces.SelectDate;
import com.pch.ete.interfaces.SelectTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static String formatDate(String date) {
        try {
            return new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(
                    new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }


    public static String formatTime(String date) {
        try {
            return new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
                    new SimpleDateFormat("HH:mm", Locale.ENGLISH).parse(date)
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String formatDateddMMMyyyyForLocal(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        output.setTimeZone(Calendar.getInstance().getTimeZone());

        Date d = null;
        String formatted;
        try {
            d = input.parse(date);
            formatted = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return formatted;
    }

    public static String formatDateHHMMForLocal(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        output.setTimeZone(Calendar.getInstance().getTimeZone());

        Date d = null;
        String formatted;
        try {
            d = input.parse(date);
            formatted = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return formatted;
    }

    public static String formatDateddMMMyyyyForUtc(String date) {
        SimpleDateFormat input = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
        input.setTimeZone(Calendar.getInstance().getTimeZone());
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        output.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date d = null;
        String formatted;
        try {
            d = input.parse(date);
            formatted = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return formatted;
    }

    public static String formatDateHHMMForUtc(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        input.setTimeZone(Calendar.getInstance().getTimeZone());
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        output.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date d = null;
        String formatted;
        try {
            d = input.parse(date);
            formatted = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return formatted;
    }

    public static String formatDateFromServer(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("dd/MMM/yyyy");
        output.setTimeZone(Calendar.getInstance().getTimeZone());

        Date d = null;
        String formatted;
        try {
            d = input.parse(date);
            formatted = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return formatted;
    }

    public static String formatDateTimeFromServer(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        output.setTimeZone(Calendar.getInstance().getTimeZone());

        Date d = null;
        String formatted;
        try {
            d = input.parse(date);
            formatted = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return formatted;
    }

    public static void showCalender(TextView textView, Activity context) {
        final Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, textView);
            }

        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        //
        // datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datePickerDialog.show();

    }

    public static void updateLabel(Calendar myCalendar, TextView textView) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(sdf.format(myCalendar.getTime()));
    }

    public static String formatCreatedDateTime(String date) {
        try {
            return new SimpleDateFormat("dd MMM yyyy '|' h:mm a", Locale.ENGLISH).format(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date)
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    public static String formatDate1(String date) {
        try {
            return new SimpleDateFormat("dd MMM", Locale.ENGLISH).format(
                    new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return date;
        }
    }


    public static String changeNotificationDateType(String date) {
        String reformattedStr = "";
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yy");

        try {
            reformattedStr = myFormat.format(fromUser.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return reformattedStr;
    }

    public static boolean isFutureDate(String date) {
        SimpleDateFormat dateFormat = null;
        Date formattedDate = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            formattedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            return false;
        }

        if (formattedDate != null) {
            if (formattedDate.compareTo(new Date(System.currentTimeMillis())) > 0) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static void selectTime(Context context, SelectTime onSelectTime, String currentTime) {
        Calendar calendar = Calendar.getInstance();
        if (!TextUtils.isEmpty(currentTime)) {
            String[] hourAndMin = currentTime.split(":");
            calendar.set(Calendar.HOUR, Integer.parseInt(hourAndMin[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hourAndMin[1]));
        }
        int hour;
        if (calendar.get(Calendar.AM_PM) == 1) {
            hour = calendar.get(Calendar.HOUR);
        } else {
            hour = calendar.get(Calendar.HOUR) + 12;
        }
        int minute = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, hourOfDay, minute1) -> {
                    if (hourOfDay < 10) {
                        if (minute1 < 10) {
                            onSelectTime.onTimeSelected("0" + hourOfDay + ":0" + minute1);
                        } else {
                            onSelectTime.onTimeSelected("0" + hourOfDay + ":" + minute1);
                        }
                    } else {
                        if (minute1 < 10) {
                            onSelectTime.onTimeSelected(hourOfDay + ":0" + minute1);
                        } else {
                            onSelectTime.onTimeSelected(hourOfDay + ":" + minute1);
                        }
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public static void selectDate(Context context, SelectDate selectDate, String startDate, String endDate) {
        Calendar calendar = Calendar.getInstance();

        if (!TextUtils.isEmpty(endDate)) {
            try {
                calendar.setTimeInMillis(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(endDate).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        DatePickerDialog dialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            selectDate.onDateSelected(new SimpleDateFormat("yyyy-MM-dd",
                    Locale.ENGLISH).format(calendar.getTime()), "");
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        if (!TextUtils.isEmpty(startDate)) {
            try {
                dialog.getDatePicker().setMinDate(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(startDate).getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

}
