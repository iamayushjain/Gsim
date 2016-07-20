package com.ayush.gsim;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {
    TextView text;
    TextView view1;
    String title;
    private View mFragmentContainerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserLearnedDrawer = false;
    private boolean mFromSavedInstanceState = false;
    public static final String TAG = DetailFragment.class.getSimpleName();


    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    String username;
    String pass;
    String cokie;

    public static DetailFragment newInstance() {
        return new DetailFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {
        String menu = getArguments().getString("Menu");
        username = getArguments().getString("User");
        pass = getArguments().getString("Pass");
        cokie = getArguments().getString("Cook");
        //Toast.makeText(getActivity(),cokie,3000).show();
        View view;
        view = inflater.inflate(R.layout.layout2, container, false);
        // String menu = getArguments().getString("Menu");
        //text= (TextView) view.findViewById(R.id.detail);
        //text.setText(menu);
        view1 = (TextView) view.findViewById(R.id.textView1);
        view1.setTypeface(Typeface
                .createFromAsset(getActivity().getAssets(), "robotothin.ttf"));

        new MyTask().execute();

        return view;

    }

    private class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            Document doc;

            // String username=e1.getText().toString();
            //String pass=e2.getText().toString();


            //Document doc;
            try {
                String urlLogin = "http://122.160.168.157/isimgc/login";
                String urlCourse = "http://122.160.168.157/iSIMGC/Student/Course";
                String urlHome = "http://122.160.168.157/isimgc/home";
                String urlTimeTable = "http://122.160.168.157/iSIMGC/Student/TimeTable";

                Connection.Response response = Jsoup.connect(urlCourse)
                        .cookie("ASP.NET_SessionId",cokie)
                        .method(Connection.Method.GET).timeout(0)
                        .execute();


                Document loginPage = response.parse();

//               Element lastFocus = 		loginPage.select("input[name=__LASTFOCUS]").first();
//                Element eventTarget = 		loginPage.select("input[name=__EVENTTARGET]").first();
//                Element eventArgument = 		loginPage.select("input[name=__EVENTARGUMENT]").first();
//                Element vKey = 		loginPage.select("input[name=__VSKEY]").first();
//                Element viewState = 		loginPage.select("input[name=__VIEWSTATE]").first();
//                Element eventValidation = 	loginPage.select("input[name=__EVENTVALIDATION]").first();
//                response = Jsoup.connect(urlCourse)
//                        .cookie("ASP.NET_SessionId", cokie)
//                        .data("__LASTFOCUS","")// lastFocus.attr("value")+"")
//                        .data("__EVENTTARGET","")// eventTarget.attr("value")+"")
//                        .data("__EVENTARGUMENT", "")//eventArgument.attr("value")+"")
//                        .data("__VSKEY", vKey.attr("value")+"")
//                        .data("__VIEWSTATE","")// viewState.attr("value")+"")
//                        .data("__EVENTVALIDATION", eventValidation.attr("value")+"")
//
//                        .method(Connection.Method.GET)
//                        .timeout(0)
//                        .execute();

                Document doc2 = response.parse();

                title = doc2.title();
                Element name = doc2.select("label[id=MCPH1_SCPH_lblName]").first();
                title = name.text();
                System.out.println(title);
                return title;

            } catch (Exception e) {
                e.printStackTrace();
                title = "Error In Login";
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {//

            try {
                view1.setText(title);
                view1.setGravity(Gravity.CENTER);
            } catch (Exception e) {
                ErrorDialog();
            }

        }
    }

    void ErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Error Connecting to The Server")
                .setCancelable(false).setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), startpage.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

                getActivity().finish();

            }
        })
                .setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        getActivity().finish();//do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

}