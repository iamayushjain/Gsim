package com.ayush.gsim.AsyncTasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.ayush.gsim.Activities.MyActivity;
import com.ayush.gsim.R;
import com.ayush.gsim.Utils.Constants;
import com.ayush.gsim.Utils.LogWrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by ayush on 25/1/17.
 */
public class Details extends AsyncTask<Void, Void, String> {

    private String userName, passWord, sessionCookie;
    private Activity activity;

    private String studentProgram = null;
    private String studentSemester = null;
    private String studentName = null;


    public Details(Activity activity, String userName, String passWord, String cookie) {
        this.activity = activity;
        this.userName = userName;
        this.passWord = passWord;
        this.sessionCookie = cookie;

    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {


        try {
            String urlLibrary = Constants.URL_LIBRARY;

            Connection.Response response = Jsoup.connect(urlLibrary)
                    .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                    .method(Connection.Method.GET).timeout(0)
                    .execute();


            Document libraryPage = response.parse();

            Element studentProgramElement = libraryPage.select("label[id=MCPH1_SCPH_lblprogram]").first();
            Element studentProgramSemester = libraryPage.select("label[id=MCPH1_SCPH_lblsem]").first();

            studentProgram = studentProgramElement.text();
            studentSemester = studentProgramSemester.text();
        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
        try {
            String urlCourse = Constants.URL_COURSE;
            Connection.Response response = Jsoup.connect(urlCourse)
                    .cookie(Constants.COOKIE_ASP_NET, sessionCookie)
                    .method(Connection.Method.GET).timeout(0)
                    .execute();


            Document detailsPage = response.parse();

            Element name = detailsPage.select("label[id=MCPH1_SCPH_lblName]").first();
            studentName = name.text();

        } catch (Exception e) {
            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {//
        Intent i = new Intent(activity, MyActivity.class);
        i.putExtra(Constants.USERNAME_INTENT, userName.toUpperCase().trim());
        i.putExtra(Constants.PASSWORD_INTENT, passWord);
        i.putExtra(Constants.COOKIE_INTENT, sessionCookie);
        i.putExtra(Constants.BRANCH_INTENT, userName.toUpperCase().trim().substring(5, 7));
        i.putExtra(Constants.STUDENT_NAME_INTENT, studentName);

        activity.startActivity(i);
        activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        activity.finish();

//        try {
//            DetailFragment.view1.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.right_slide_in));
//            DetailFragment.view1.setText(studentName);
//            DetailFragment.view1.setGravity(Gravity.CENTER);
//            DetailFragment.welcome.setVisibility(View.VISIBLE);
//            FragmentDrawer.studentNameTextView.setText(studentName);
//            FragmentDrawer.userNameTextView.setText(userName);
//        } catch (Exception e) {
//            LogWrapper.printStackTrace(activity.getApplicationContext(), e);
//            Utils.sessionExpired(activity);
//        }
    }
}
