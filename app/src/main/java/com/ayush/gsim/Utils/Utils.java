package com.ayush.gsim.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ayush.gsim.Activities.LoginActivity;
import com.ayush.gsim.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by ayush on 23/1/17.
 */

public class Utils {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public static void noInternetDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.no_internet_message))
                .setCancelable(false)
                .setPositiveButton(activity.getResources().getString(R.string.alert_message_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static String getVersionCode(Context context) {
        String ver = "16";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            ver = pInfo.versionCode + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ver;
    }

    public static void errorDialog(final Activity activity, final Class target) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Error Connecting to The Server")
                .setCancelable(false).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(activity, target);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
                activity.finish();

            }
        })
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        activity.finish();//do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static void deletePreviousData(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.OLD_SHARED_PREF_LOGIN_TABLE, 0);
        sharedPreferences.edit().clear().commit();
        SharedPreferences sharedPreferences1 = activity.getSharedPreferences(Constants.OLD_SHARED_PREF_LOGIN_TABLE_1, 0);
        sharedPreferences1.edit().clear().commit();
        SharedPreferences sharedPreferences2 = activity.getSharedPreferences(Constants.OLD_SHARED_PREF_LOGIN_TABLE_2, 0);
        sharedPreferences2.edit().clear().commit();
        SharedPreferences sharedPreferences3 = activity.getSharedPreferences(Constants.OLD_SHARED_PREF_ADS_COUNT_TABLE, 0);
        sharedPreferences3.edit().clear().commit();
        SharedPreferences sharedPreferences4 = activity.getSharedPreferences(Constants.OLD_SHARED_PREF_DIALOG_COUNT_TABLE, 0);
        sharedPreferences4.edit().clear().commit();
        SharedPreferences sharedPreferences5 = activity.getSharedPreferences(Constants.OLD_SHARED_PREF_ATTENDANCE_1_TABLE, 0);
        sharedPreferences5.edit().clear().commit();
        SharedPreferences sharedPreferences6 = activity.getSharedPreferences(Constants.OLD_SHARED_PREF_ATTENDANCE_2_TABLE, 0);
        sharedPreferences6.edit().clear().commit();

    }

    public static void logout(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.SHARED_PREF_LOGIN_TABLE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARED_PREF_AUTO_LOGIN, Constants.AUTO_LOGIN_FALSE_CASE);
        editor.commit();

        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
        activity.finish();
    }

    public static void sessionExpired(Activity activity) {
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.overridePendingTransition(R.anim.right_slide_in2, R.anim.right_slide_out2);
        activity.finish();
    }
}
