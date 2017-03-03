package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.ayush.gsim.Activities.ArchiveActivity;
import com.ayush.gsim.BranchWiseChat.BranchWiseChatFragment;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.FirebaseConstants;
import com.ayush.gsim.Utils.LogWrapper;
import com.ayush.gsim.Utils.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by ayush on 14/2/17.
 */

public class ChatGetName extends AsyncTask<Void, Void, String> {

    private String userName, passWord, sessionCookie;
    private Activity activity;
    private String studentName = null;
    private boolean isSuccessful;
    private ProgressDialog mProgressDialog;
    private FragmentManager fragmentManager;


    public ChatGetName(Activity activity, String userName, String passWord, String cookie, FragmentManager fragmentManager) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.sessionCookie = cookie;
        this.fragmentManager = fragmentManager;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(activity.getString(R.string.chat_authentication_progress_dialog));
        mProgressDialog.setMessage(activity.getString(R.string.loading_progress_dialog));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        isSuccessful = false;

    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            String urlCourse = Constants.URL_COURSE;
            Connection.Response response = Jsoup.connect(urlCourse)
                    .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                    .method(Connection.Method.GET).timeout(0)
                    .execute();

            Document detailsPage = response.parse();

            Element name = detailsPage.select("label[id=MCPH1_SCPH_lblName]").first();
            studentName = name.text();
            isSuccessful = true;
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
            isSuccessful = false;
        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


        try {
            mProgressDialog.dismiss();
            if (!isSuccessful) {
                Toast.makeText(activity.getApplicationContext(), "Session Expired", Toast.LENGTH_LONG).show();
                Utils.sessionExpired(activity);
            } else {
                BranchWiseChatFragment branchWiseChatFragment = new BranchWiseChatFragment();
                Bundle arg = new Bundle();

                arg.putString(Constants.USERNAME_INTENT, userName);
                arg.putString(Constants.PASSWORD_INTENT, passWord);
                arg.putString(Constants.COOKIE_INTENT, sessionCookie);
                arg.putString(Constants.STUDENT_NAME_INTENT, studentName);
                arg.putString(Constants.BRANCH_INTENT, userName.toUpperCase().trim().substring(6, 8));

                branchWiseChatFragment.setArguments(arg);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, branchWiseChatFragment).commit();
            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
            Toast.makeText(activity.getApplicationContext(), "Failed Choosing Branch", Toast.LENGTH_LONG).show();
            Utils.sessionExpired(activity);
        }
    }

}


