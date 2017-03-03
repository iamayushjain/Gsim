package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ayush.gsim.Fragments.CoursePlanFragment;
import com.ayush.gsim.Fragments.LibraryFragment;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 25/1/17.
 */
public class CoursePlan extends AsyncTask<Void, Void, String> {

    private String userName, passWord, cookie;
    private ProgressDialog mProgressDialog;
    private Activity activity;
    private FragmentManager fragmentManager;

    private List<String> subjectIDs;
    private List<String> subjectsName;

    public CoursePlan(Activity activity, String userName, String passWord, String cookie, FragmentManager fragmentManager) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.cookie = cookie;
        this.fragmentManager = fragmentManager;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle(activity.getResources().getString(R.string.course_plan_progress_dialog));
        mProgressDialog.setMessage(activity.getResources().getString(R.string.loading_progress_dialog));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        subjectIDs = new ArrayList<>();
        subjectsName = new ArrayList<>();
    }

    @Override
    protected String doInBackground(Void... params) {


        try {
            String urlCoursePlan = Constants.URL_COURSE_PLAN;

            Document libraryPage = Jsoup.connect(urlCoursePlan)
                    .cookie(Constants.COOKIE_ASP_NET, cookie)
                    .get();

            Elements optionsElements = libraryPage.select("option");
            for (Element element : optionsElements) {
                subjectIDs.add(element.attr("value"));
                LogWrapper.d(activity.getApplicationContext(), "page", "id" + element.attr("value"));

                subjectsName.add(element.text());
            }
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {//

        try {
            mProgressDialog.dismiss();
            Bundle arg = new Bundle();


            arg.putString(Constants.USERNAME_INTENT, userName);
            arg.putString(Constants.PASSWORD_INTENT, passWord);
            arg.putString(Constants.COOKIE_INTENT, cookie);

            arg.putStringArray(Constants.COURSE_PLAN_SUBJECTS_INTENT, subjectsName.toArray(new String[subjectsName.size()]));
            arg.putStringArray(Constants.COURSE_PLAN_SUBJECT_IDS_INTENT, subjectIDs.toArray(new String[subjectIDs.size()]));

            CoursePlanFragment coursePlanFragment = new CoursePlanFragment();
            coursePlanFragment.setArguments(arg);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, coursePlanFragment, LibraryFragment.TAG).commit();

        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
    }


}
